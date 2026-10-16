package de.fuballer.mcendgame.test.main

import de.fuballer.mcendgame.main.component.block.CustomBlocks
import de.fuballer.mcendgame.main.component.block.blocks.dungeon_device.DungeonDeviceBlockEntity
import de.fuballer.mcendgame.main.component.portal.PortalEntity
import de.fuballer.mcendgame.main.messaging.dungeon.OpenDungeonButtonPressedEvent
import de.fuballer.mcendgame.main.messaging.misc.GetRespawnCommand
import de.fuballer.mcendgame.main.messaging.portal.PortalUsedEvent
import de.fuballer.mcendgame.main.util.extension.WorldExtension.isDungeonWorld
import de.fuballer.mcendgame.main.util.extension.mixin.PlayerEntityMixinExtension.getDungeonLevel
import de.fuballer.mcendgame.main.util.extension.mixin.PlayerEntityMixinExtension.getDungeonSeed
import de.fuballer.mcendgame.main.util.extension.mixin.PlayerEntityMixinExtension.isInsideDungeon
import de.fuballer.mcendgame.main.util.extension.mixin.WorldMixinExtension.getDungeonLevel
import de.fuballer.mcendgame.main.util.extension.mixin.WorldMixinExtension.getOpener
import de.fuballer.mcendgame.main.util.extension.mixin.WorldMixinExtension.getTotalBossCount
import de.fuballer.mcendgame.test.main.DungeonOpenAndJoinGameTest.testPlayerOpensAndJoinsDungeon
import de.maucon.mauconframework.command.CommandGateway
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
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.Mob
import net.minecraft.world.level.GameType
import net.minecraft.world.level.entity.EntityTypeTest
import net.minecraft.world.level.portal.TeleportTransition
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import org.slf4j.LoggerFactory
import kotlin.math.abs

/**
 * End-to-end server gametests for the dungeon entry/exit flow at the Dungeon Device:
 *  - a player opens a dungeon and joins it through an entry portal
 *  - when leaving the dungeon the player returns to the entry portal they used (position + rotation)
 *  - players without a known entry portal fall back to the top of the dungeon device
 *
 * The tests drive the same server-side seams the real client uses:
 *  - opening: [OpenDungeonButtonPressedEvent], which the `OpenDungeonPayloadRegisterer`
 *    publishes after the client presses the "open dungeon" button
 *  - joining/leaving: [PortalUsedEvent], which `PortalEntity.interact` publishes when the
 *    player interacts with a portal
 *
 * This class is not a gametest entrypoint itself; the main gametest entrypoint
 * ([MCEndgameGameTest]) invokes these functions.
 */
object DungeonOpenAndJoinGameTest {
    private val LOG = LoggerFactory.getLogger(this::class.java.simpleName)

    // Covers the whole generated layout (rooms start at the origin and extend a few hundred blocks)
    private val DUNGEON_AREA = AABB.ofSize(Vec3(0.0, 0.0, 0.0), 2000.0, 500.0, 2000.0)

    private val DEVICE_POS = BlockPos(0, 1, 0)

    private const val PORTAL_SEARCH_RADIUS = 6.0
    private const val POSITION_TOLERANCE = 0.5
    private const val ROTATION_TOLERANCE = 0.01f

    fun testPlayerOpensAndJoinsDungeon(helper: GameTestHelper) {
        val originWorld = helper.level

        // Place a Dungeon Device inside the test structure
        helper.setBlock(DEVICE_POS, CustomBlocks.DUNGEON_DEVICE)
        val dungeonDevice = helper.getBlockEntity(DEVICE_POS, DungeonDeviceBlockEntity::class.java)

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
            dungeonWorld.dimension().identifier().path,
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
            val entryPortal = findEntryPortal(originWorld, helper.absolutePos(DEVICE_POS))
                ?: throw helper.assertionException("Entry portals should spawn around the dungeon device")
            LOG.info("Entry portal found, player joins the dungeon via PortalUsedEvent")

            // JOIN: interact with the entry portal (what PortalEntity.interact does on the server)
            EventGateway.publish(PortalUsedEvent(player, entryPortal, entryPortal.teleportLocation))

            // --- join assertions ---
            helper.assertTrue(
                player.level() === dungeonWorld,
                "Using the entry portal should move the player into the generated dungeon world"
            )
            helper.assertTrue(player.level().isDungeonWorld(), "The player should be inside a dungeon world after joining")
            helper.assertTrue(player.isInsideDungeon(), "The player should be tracked as inside a dungeon")
            helper.assertTrue(dungeonWorld.getOpener() === player, "The dungeon world should remember its opener")

            LOG.info("Player joined the dungeon world {}, test succeeded", dungeonWorld.dimension().identifier().path)
            helper.succeed()
        }
    }

    /**
     * A player that entered a dungeon through an entry portal should be teleported back to that exact
     * portal (position and rotation) when leaving through the dungeon's leave portal.
     */
    fun testPlayerExitsAtEntryPortal(helper: GameTestHelper) {
        val originWorld = helper.level
        val devicePos = helper.absolutePos(DEVICE_POS)
        val player = openDungeon(helper)

        helper.runAfterDelay(1) {
            val entryPortal = findEntryPortal(originWorld, devicePos)
                ?: throw helper.assertionException("Entry portals should spawn around the dungeon device")

            val entryPosition = entryPortal.position()
            val entryXRot = entryPortal.xRot
            val entryYRot = entryPortal.yRot
            LOG.info("Player joins through the entry portal at {} (yRot {}, xRot {})", entryPosition, entryYRot, entryXRot)

            // JOIN: interacting with the portal publishes the used-portal event, which records the entry location
            entryPortal.interact(player, InteractionHand.MAIN_HAND, entryPosition)
            helper.assertTrue(entryPortal.isRemoved, "Using a single-use entry portal should remove it")
            helper.assertTrue(player.level().isDungeonWorld(), "Using an entry portal should move the player into the dungeon")

            val dungeonWorld = player.level()
            val leavePortal = findLeavePortal(dungeonWorld, entryPortal.teleportLocation!!.coordinates)
                ?: throw helper.assertionException("The dungeon should contain a leave portal near its start")

            // LEAVE: the leave portal should send the player back to the entry portal they used
            leavePortal.interact(player, InteractionHand.MAIN_HAND, leavePortal.position())

            helper.assertTrue(
                player.level() === originWorld,
                "Leaving through the dungeon portal should return the player to the origin world, but was in ${player.level().dimension().identifier()}"
            )
            assertNear(helper, player.position(), entryPosition, "the entry portal the player used")
            assertRotation(helper, player.yRot, entryYRot, "yaw")
            assertRotation(helper, player.xRot, entryXRot, "pitch")

            LOG.info("Player left the dungeon at the entry portal {} (yRot {}, xRot {})", player.position(), player.yRot, player.xRot)
            helper.succeed()
        }
    }

    /**
     * A player that has no recorded entry portal (e.g. teleported in or after a server restart) must
     * still leave the dungeon, falling back to the top of the dungeon device.
     */
    fun testPlayerExitsAtDeviceWithoutEntryPortal(helper: GameTestHelper) {
        val originWorld = helper.level
        val devicePos = helper.absolutePos(DEVICE_POS)
        val player = openDungeon(helper)

        helper.runAfterDelay(1) {
            val entryPortal = findEntryPortal(originWorld, devicePos)
                ?: throw helper.assertionException("Entry portals should spawn around the dungeon device")
            val dungeonTarget = entryPortal.teleportLocation
                ?: throw helper.assertionException("Entry portals should have a dungeon teleport target")
            val dungeonWorld = dungeonTarget.world

            // Move the player into the dungeon without using an entry portal, so no entry location is recorded
            player.teleport(TeleportTransition(dungeonWorld, dungeonTarget.coordinates, Vec3.ZERO, 0.0F, 0.0F) {})
            helper.assertTrue(player.level().isDungeonWorld(), "The player should be inside the dungeon")

            // Death respawn must also fall back to the top of the device
            val respawn = CommandGateway.apply(GetRespawnCommand(player)).respawn
                ?: throw helper.assertionException("A player inside a dungeon should get a forced respawn")
            val respawnData = respawn.respawnData()
            helper.assertTrue(
                respawnData.dimension() == originWorld.dimension(),
                "Death respawn should stay in the origin world, but was ${respawnData.dimension().identifier()}"
            )
            helper.assertTrue(
                respawnData.pos() == devicePos.offset(0, 1, 0),
                "Death respawn should fall back to the top of the dungeon device at ${devicePos.offset(0, 1, 0)}, but was ${respawnData.pos()}"
            )

            val leavePortal = findLeavePortal(dungeonWorld, dungeonTarget.coordinates)
                ?: throw helper.assertionException("The dungeon should contain a leave portal near its start")
            val fallback = leavePortal.teleportLocation
                ?: throw helper.assertionException("The leave portal should have a fallback teleport location")

            EventGateway.publish(PortalUsedEvent(player, leavePortal, fallback))

            helper.assertTrue(
                player.level() === originWorld,
                "Leaving should return the player to the origin world, but was in ${player.level().dimension().identifier()}"
            )
            assertNear(helper, player.position(), fallback.coordinates, "the portal's fallback destination")
            // The fallback must be on top of the dungeon device
            assertNear(
                helper,
                player.position(),
                Vec3.atLowerCornerOf(devicePos).add(0.5, 1.0, 0.5),
                "the top of the dungeon device",
            )

            LOG.info("Player without an entry portal left the dungeon on top of the device at {}", player.position())
            helper.succeed()
        }
    }

    /**
     * A player that logs back in while still inside a dungeon (e.g. after disconnecting mid-run) is
     * ejected through the same entry portal they used, instead of the top of the dungeon device.
     */
    fun testRejoiningPlayerIsEjectedAtEntryPortal(helper: GameTestHelper) {
        val originWorld = helper.level
        val devicePos = helper.absolutePos(DEVICE_POS)

        // A throwaway opener so the entry portals exist before the rejoining player logs in
        openDungeon(helper)

        helper.runAfterDelay(1) {
            val entryPortal = findEntryPortal(originWorld, devicePos)
                ?: throw helper.assertionException("Entry portals should spawn around the dungeon device")
            val entryPosition = entryPortal.position()
            val entryYRot = entryPortal.yRot

            // Join a player whose join handling is still pending, then put them into the dungeon in the
            // same tick - like a player whose saved position is inside the dungeon they logged out in.
            val player = spawnMockServerPlayer(helper)
            EventGateway.publish(PortalUsedEvent(player, entryPortal, entryPortal.teleportLocation))
            helper.assertTrue(player.level().isDungeonWorld(), "The player should be inside the dungeon")

            helper.runAfterDelay(2) {
                helper.assertTrue(
                    player.level() === originWorld,
                    "A player rejoining inside a dungeon should be sent back to the origin world, but was in ${player.level().dimension().identifier()}"
                )
                assertNear(helper, player.position(), entryPosition, "the entry portal the player used")
                assertRotation(helper, player.yRot, entryYRot, "yaw")

                LOG.info("Rejoining player was ejected to the entry portal at {}", player.position())
                helper.succeed()
            }
        }
    }

    /**
     * A player that dies inside a dungeon respawns at the entry portal they used, keeping its rotation.
     */
    fun testDeathRespawnPrefersEntryPortal(helper: GameTestHelper) {
        val originWorld = helper.level
        val devicePos = helper.absolutePos(DEVICE_POS)
        val player = openDungeon(helper)

        helper.runAfterDelay(1) {
            val entryPortal = findEntryPortal(originWorld, devicePos)
                ?: throw helper.assertionException("Entry portals should spawn around the dungeon device")
            val entryPosition = entryPortal.position()
            val entryBlockPos = BlockPos.containing(entryPosition)

            entryPortal.interact(player, InteractionHand.MAIN_HAND, entryPosition)
            helper.assertTrue(player.level().isDungeonWorld(), "Using an entry portal should move the player into the dungeon")

            // What ServerPlayerRespawnMixin asks for when the player dies inside the dungeon
            val respawn = CommandGateway.apply(GetRespawnCommand(player)).respawn
                ?: throw helper.assertionException("A player inside a dungeon should get a forced respawn")
            val respawnData = respawn.respawnData()

            helper.assertTrue(
                respawnData.dimension() == originWorld.dimension(),
                "The player should respawn in the origin world, but was ${respawnData.dimension().identifier()}"
            )
            helper.assertTrue(
                respawnData.pos() == entryBlockPos,
                "The player should respawn at the entry portal block $entryBlockPos, but was ${respawnData.pos()}"
            )
            assertRotation(helper, respawnData.yaw(), entryPortal.yRot, "yaw")
            assertRotation(helper, respawnData.pitch(), entryPortal.xRot, "pitch")

            LOG.info("Death respawn resolved to the entry portal block {} (yaw {}, pitch {})", respawnData.pos(), respawnData.yaw(), respawnData.pitch())
            helper.succeed()
        }
    }

    /**
     * Places a dungeon device, spawns a connected mock player and opens a dungeon through it.
     */
    private fun openDungeon(helper: GameTestHelper): ServerPlayer {
        helper.setBlock(DEVICE_POS, CustomBlocks.DUNGEON_DEVICE)
        val dungeonDevice = helper.getBlockEntity(DEVICE_POS, DungeonDeviceBlockEntity::class.java)
        val player = spawnMockServerPlayer(helper)

        EventGateway.publish(OpenDungeonButtonPressedEvent(dungeonDevice, player))
        return player
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
        val searchArea = AABB.ofSize(Vec3.atCenterOf(devicePos), 12.0, 6.0, 12.0)
        return findPortals(world, searchArea).firstOrNull()
    }

    private fun findLeavePortal(dungeonWorld: ServerLevel, spawnPos: Vec3): PortalEntity? {
        val searchArea = AABB.ofSize(spawnPos, PORTAL_SEARCH_RADIUS * 2, PORTAL_SEARCH_RADIUS * 2, PORTAL_SEARCH_RADIUS * 2)
        return findPortals(dungeonWorld, searchArea).firstOrNull()
    }

    private fun findPortals(world: ServerLevel, area: AABB): List<PortalEntity> {
        val portalTest: EntityTypeTest<Entity, PortalEntity> = EntityTypeTest.forClass(PortalEntity::class.java)
        return world.getEntities(portalTest, area) { true }
    }

    private fun assertNear(helper: GameTestHelper, actual: Vec3, expected: Vec3, description: String) {
        helper.assertTrue(
            actual.distanceToSqr(expected) < POSITION_TOLERANCE * POSITION_TOLERANCE,
            "The player should be teleported to $description at $expected, but was at $actual"
        )
    }

    private fun assertRotation(helper: GameTestHelper, actual: Float, expected: Float, axis: String) {
        helper.assertTrue(
            abs(actual - expected) < ROTATION_TOLERANCE,
            "The player should keep the portal's $axis rotation ($expected), but was $actual"
        )
    }
}
