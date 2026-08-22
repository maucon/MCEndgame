package de.fuballer.mcendgame.test.main

import de.fuballer.mcendgame.main.component.block.CustomBlocks
import de.fuballer.mcendgame.main.component.block.blocks.dungeon_device.DungeonDeviceBlockEntity
import de.fuballer.mcendgame.main.component.portal.PortalEntity
import de.fuballer.mcendgame.main.messaging.dungeon.OpenDungeonButtonPressedEvent
import de.fuballer.mcendgame.main.messaging.portal.PortalUsedEvent
import de.fuballer.mcendgame.main.util.extension.WorldExtension.isDungeonWorld
import de.fuballer.mcendgame.main.util.extension.mixin.PlayerEntityMixinExtension.getDungeonLevel
import de.fuballer.mcendgame.main.util.extension.mixin.PlayerEntityMixinExtension.getDungeonSeed
import de.fuballer.mcendgame.main.util.extension.mixin.PlayerEntityMixinExtension.isInsideDungeon
import de.fuballer.mcendgame.main.util.extension.mixin.WorldMixinExtension.getDungeonLevel
import de.fuballer.mcendgame.main.util.extension.mixin.WorldMixinExtension.getOpener
import de.fuballer.mcendgame.main.util.extension.mixin.WorldMixinExtension.getTotalBossCount
import de.fuballer.mcendgame.test.main.DungeonOpenAndJoinGameTest.testPlayerOpensAndJoinsDungeon
import de.maucon.mauconframework.event.EventGateway
import io.netty.channel.embedded.EmbeddedChannel
import net.minecraft.core.BlockPos
import net.minecraft.gametest.framework.GameTestHelper
import net.minecraft.network.Connection
import net.minecraft.network.protocol.PacketFlow
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.server.network.CommonListenerCookie
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.Mob
import net.minecraft.world.level.GameType
import net.minecraft.world.level.entity.EntityTypeTest
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import org.slf4j.LoggerFactory

/**
 * End-to-end server gametest for the dungeon entry flow:
 * a player opens a dungeon at the Dungeon Device and then joins it through the entry portal.
 *
 * The test drives the same server-side seams the real client uses:
 *  - opening: [OpenDungeonButtonPressedEvent], which the `OpenDungeonPayloadRegisterer`
 *    publishes after the client presses the "open dungeon" button
 *  - joining: [PortalUsedEvent], which `PortalEntity.interact` publishes when the
 *    player interacts with an entry portal
 *
 * This class is not a gametest entrypoint itself; the main gametest entrypoint
 * ([MCEndgameGameTest]) invokes [testPlayerOpensAndJoinsDungeon].
 */
object DungeonOpenAndJoinGameTest {
    private val LOG = LoggerFactory.getLogger(this::class.java.simpleName)

    // Covers the whole generated layout (rooms start at the origin and extend a few hundred blocks)
    private val DUNGEON_AREA = AABB.ofSize(Vec3(0.0, 0.0, 0.0), 2000.0, 500.0, 2000.0)

    fun testPlayerOpensAndJoinsDungeon(helper: GameTestHelper) {
        val originWorld = helper.level
        val deviceRelativePos = BlockPos(0, 1, 0)

        // Place a Dungeon Device inside the test structure
        helper.setBlock(deviceRelativePos, CustomBlocks.DUNGEON_DEVICE)
        val dungeonDevice = helper.getBlockEntity(deviceRelativePos, DungeonDeviceBlockEntity::class.java)

        // A real, connected ServerPlayer inside the test level (same seam as a logged-in player)
        val player = spawnMockServerPlayer(helper)
        LOG.info("Mock server player spawned: {}", player.gameProfile.name)

        // OPEN: publish what the network payload handler publishes on button press
        LOG.info("Publishing OpenDungeonButtonPressedEvent to open a dungeon")
        EventGateway.publish(OpenDungeonButtonPressedEvent(dungeonDevice, player))

        // Generation runs synchronously on the server thread, so the dungeon world exists now.
        val dungeonWorld = findDungeonWorld(originWorld.server)
            ?: throw helper.assertionException("Opening the dungeon should generate a dungeon world")
        LOG.info(
            "Dungeon world generated: {} (dungeon level {}, boss count {}, opener {})",
            dungeonWorld.dimension().toString(),
            dungeonWorld.getDungeonLevel(),
            dungeonWorld.getTotalBossCount(),
            dungeonWorld.getOpener().gameProfile.name,
        )

        // --- generation assertions ---
        helper.assertTrue(dungeonWorld.isDungeonWorld(), "The generated world should be marked as a dungeon world")
        helper.assertValueEqual(
            dungeonWorld.getDungeonLevel(),
            player.getDungeonLevel().level,
            "The dungeon world level should match the opening player's dungeon level"
        )
        helper.assertTrue(dungeonWorld.getTotalBossCount() > 0, "The generated dungeon should contain bosses")

        helper.assertTrue(player.getDungeonSeed() != null, "Opening should roll a dungeon seed for the player")
        helper.assertTrue(player.getDungeonSeed()?.hasBeenUsed == true, "The rolled dungeon seed should be marked as used")
        LOG.info(
            "Player dungeon seed rolled: seed {}, type {}, already used {}",
            player.getDungeonSeed()?.seed,
            player.getDungeonSeed()?.type,
            player.getDungeonSeed()?.hasBeenUsed,
        )

        val mobs = dungeonWorld.getEntities(EntityTypeTest.forClass(Mob::class.java), DUNGEON_AREA) { true }
        helper.assertTrue(mobs.isNotEmpty(), "The generated dungeon should contain spawned enemies")
        LOG.info("Dungeon contains {} spawned enemies", mobs.size)

        helper.runAfterDelay(1) {
            // Entry portals spawn around the device in the origin world; the player joins by using one
            val entryPortal = findEntryPortal(originWorld, helper.absolutePos(deviceRelativePos))
                ?: throw helper.assertionException("Entry portals should spawn around the dungeon device")
            LOG.info("Entry portal found, player joins the dungeon via PortalUsedEvent")

            // JOIN: interact with the entry portal (what PortalEntity.interact does on the server)
            EventGateway.publish(PortalUsedEvent(player, entryPortal.teleportLocation))

            // --- join assertions ---
            helper.assertTrue(
                player.level() === dungeonWorld,
                "Using the entry portal should move the player into the generated dungeon world"
            )
            helper.assertTrue(player.level().isDungeonWorld(), "The player should be inside a dungeon world after joining")
            helper.assertTrue(player.isInsideDungeon(), "The player should be tracked as inside a dungeon")
            helper.assertTrue(dungeonWorld.getOpener() === player, "The dungeon world should remember its opener")

            LOG.info("Player joined the dungeon world {}, test succeeded", dungeonWorld.dimension().toString())
            helper.succeed()
        }
    }

    private fun findDungeonWorld(server: MinecraftServer): ServerLevel? =
        server.allLevels.firstOrNull { it.isDungeonWorld() }

    /**
     * Creates a connected ServerPlayer inside the test level.
     *
     * Replaces the deprecated `GameTestHelper.makeMockServerPlayerInLevel()` using only
     * public API: [GameTestHelper.makeMockServerPlayer] creates the player and
     * [net.minecraft.server.players.PlayerList.placeNewPlayer] joins it into the level
     * over an embedded (in-memory) connection.
     */
    private fun spawnMockServerPlayer(helper: GameTestHelper): ServerPlayer {
        val server = helper.level.server
        val player = helper.makeMockServerPlayer(GameType.SURVIVAL) as ServerPlayer

        val connection = Connection(PacketFlow.SERVERBOUND)
        // Registering the connection as a handler binds connection.channel
        EmbeddedChannel(connection)

        val cookie = CommonListenerCookie.createInitial(player.gameProfile, false)
        server.playerList.placeNewPlayer(connection, player, cookie)
        return player
    }

    private fun findEntryPortal(world: ServerLevel, devicePos: BlockPos): PortalEntity? {
        val portalTest: EntityTypeTest<Entity, PortalEntity> = EntityTypeTest.forClass(PortalEntity::class.java)
        val searchArea = AABB.ofSize(Vec3.atCenterOf(devicePos), 12.0, 6.0, 12.0)
        return world.getEntities(portalTest, searchArea) { true }.firstOrNull()
    }
}