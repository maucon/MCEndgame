package de.fuballer.mcendgame.test.main

import io.netty.channel.embedded.EmbeddedChannel
import net.fabricmc.fabric.api.gametest.v1.GameTest
import net.minecraft.core.BlockPos
import net.minecraft.gametest.framework.GameTestHelper
import net.minecraft.network.Connection
import net.minecraft.network.protocol.PacketFlow
import net.minecraft.network.protocol.game.ServerboundPlayerLoadedPacket
import net.minecraft.server.level.ServerPlayer
import net.minecraft.server.network.CommonListenerCookie
import net.minecraft.world.entity.EntitySpawnReason
import net.minecraft.world.entity.EntityTypes
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.GameType
import net.minecraft.world.level.entity.EntityTypeTest
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import org.slf4j.LoggerFactory

private val LOG = LoggerFactory.getLogger(CompatibilityGameTest::class.java)

/**
 * Regression tests for third-party mod compatibility (GitHub issue #377).
 *
 * The real mods cannot be loaded in this test source set, so this class reproduces the exact hooks they use:
 * Pufferfish's Skills injects at the tail of `LivingEntity#dropAllDeathLoot` to grant kill XP, and Better Revive
 * registers Fabric's `ALLOW_DEATH` / `AFTER_DAMAGE` events. All of them live inside methods MCEndgame used to
 * replace wholesale.
 */
class CompatibilityGameTest {
    init {
        CompatibilityProbe.register()
    }

    @GameTest(maxTicks = 200)
    fun testMobDeathStillRunsThirdPartyDeathLootTailHooks(helper: GameTestHelper) {
        helper.prepareNormalDifficulty()

        val victim = helper.spawnWithNoFreeWill(EntityTypes.ZOMBIE, Vec3(0.5, 2.0, 0.5))
        prepareVictim(victim)
        victim.health = 0.5f
        CompatibilityProbe.reset(victim)

        val player = helper.createProbePlayer()
        player.setItemSlot(EquipmentSlot.MAINHAND, ItemStack(Items.DIAMOND_SWORD))
        player.getAttribute(Attributes.ATTACK_SPEED)?.baseValue = 100.0
        player.resetAttackStrengthTicker()

        player.attack(victim)
        LOG.info(
            "Lethal probe hit: victimHealth={}/{}, deadOrDying={}, removed={}, lastDamageSource={}",
            victim.health, victim.maxHealth, victim.isDeadOrDying, victim.isRemoved, victim.lastDamageSource,
        )

        helper.assertTrue(
            CompatibilityProbe.dropAllDeathLootTail.get() > 0,
            "A TAIL hook on LivingEntity#dropAllDeathLoot never ran; Pufferfish's Skills awards kill skill XP there"
        )
        helper.succeed()
    }

    @GameTest(maxTicks = 200)
    fun testFabricDamageEventsStillFire(helper: GameTestHelper) {
        helper.prepareNormalDifficulty()

        val victim = helper.spawnWithNoFreeWill(EntityTypes.ZOMBIE, Vec3(0.5, 2.0, 0.5))
        prepareVictim(victim)
        CompatibilityProbe.reset(victim)

        val hurt = victim.hurtServer(helper.level, helper.level.damageSources().generic(), 1.0f)

        helper.assertTrue(hurt, "The non-lethal hit should be accepted")
        helper.assertTrue(
            CompatibilityProbe.allowDamage.get() > 0,
            "ServerLivingEntityEvents.ALLOW_DAMAGE never fired, so other mods cannot veto or observe damage"
        )
        helper.assertTrue(
            CompatibilityProbe.afterDamage.get() > 0,
            "ServerLivingEntityEvents.AFTER_DAMAGE never fired; Better Revive uses it to track player damage"
        )
        helper.succeed()
    }

    @GameTest(maxTicks = 200)
    fun testFabricAllowDeathStillFiresForPlayers(helper: GameTestHelper) {
        helper.prepareNormalDifficulty()

        val player = helper.createProbePlayer()
        CompatibilityProbe.reset(player)

        player.hurtServer(helper.level, helper.level.damageSources().genericKill(), 1_000.0f)

        helper.assertTrue(
            CompatibilityProbe.allowDeath.get() > 0,
            "ServerLivingEntityEvents.ALLOW_DEATH never fired; Better Revive relies on it to enter the downed state " +
                    "and create a grave instead of the vanilla death screen"
        )
        helper.succeed()
    }

    @GameTest(maxTicks = 200)
    fun testAllowDeathVetoPreventsDeath(helper: GameTestHelper) {
        helper.prepareNormalDifficulty()

        val player = helper.createProbePlayer()
        // The probe's ALLOW_DEATH listener (registered once) vetoes the death and heals the player,
        // which is what Better Revive does before entering its downed state.
        CompatibilityProbe.reset(player, vetoDeath = true)

        player.hurtServer(helper.level, helper.level.damageSources().genericKill(), 1_000.0f)

        helper.assertTrue(
            !player.isDeadOrDying,
            "A veto of ServerLivingEntityEvents.ALLOW_DEATH must prevent LivingEntity#die from being called"
        )
        helper.assertTrue(player.health > 0.0f, "The player should still be alive after the veto")
        helper.succeed()
    }

    @GameTest(maxTicks = 400)
    fun testDungeonMobDeathKeepsTailHookAndLootSuppression(helper: GameTestHelper) {
        helper.prepareNormalDifficulty()
        val player = DungeonGameTest.openDungeon(helper)
        val dungeonWorld = DungeonGameTest.findDungeonWorld(helper.level.server)
            ?: throw helper.assertionException("Opening the dungeon should generate a dungeon world")
        val pig = EntityTypes.PIG.spawn(dungeonWorld, BlockPos(0, 100, 0), EntitySpawnReason.COMMAND)
            ?: throw helper.assertionException("Could not spawn the probe mob in the dungeon world")
        pig.isNoGravity = true
        pig.health = 0.01f
        CompatibilityProbe.reset(pig)
        pig.hurtServer(dungeonWorld, dungeonWorld.damageSources().playerAttack(player), 1.0f)
        helper.assertTrue(pig.isDeadOrDying, "The dungeon probe mob should have died")
        helper.assertTrue(
            CompatibilityProbe.dropAllDeathLootTail.get() > 0,
            "A TAIL hook on LivingEntity#dropAllDeathLoot never ran in a dungeon world; kill skill XP would be lost there"
        )
        helper.runAfterDelay(1) {
            val drops = dungeonWorld.getEntities(
                EntityTypeTest.forClass(ItemEntity::class.java),
                AABB.ofSize(pig.position(), 8.0, 8.0, 8.0),
            ) { true }
            helper.assertTrue(
                drops.isEmpty(),
                "Dungeon loot suppression must still skip vanilla drops, but found ${drops.map { it.item }}"
            )
            helper.succeed()
        }
    }

    private fun GameTestHelper.createProbePlayer(): ServerPlayer {
        val player = makeMockServerPlayer(GameType.SURVIVAL) as ServerPlayer
        val cookie = CommonListenerCookie.createInitial(player.gameProfile, false)
        val connection = Connection(PacketFlow.SERVERBOUND)
        EmbeddedChannel(connection)
        level.server.playerList.placeNewPlayer(connection, player, cookie)
        // The mock player has no real client, so ServerPlayer.isInvulnerableTo() reports it as
        // invulnerable until the connection is marked as client-loaded.
        ServerboundPlayerLoadedPacket().handle(player.connection)

        return player.apply {
            abilities.invulnerable = false
            health = maxHealth
            absorptionAmount = 0.0f
            removeAllEffects()
            snapTo(absoluteVec(Vec3(1.5, 2.0, 0.5)))
            isNoGravity = true
        }
    }
}
