package de.fuballer.mcendgame.test.main

import de.fuballer.mcendgame.main.component.dungeon.world.DungeonWorldSettings
import de.fuballer.mcendgame.main.runtime_worlds.RuntimeClockManager
import de.fuballer.mcendgame.main.runtime_worlds.RuntimeLevelHandle
import de.fuballer.mcendgame.main.runtime_worlds.RuntimeWorlds
import de.fuballer.mcendgame.main.runtime_worlds.ServerClockManagerExtension
import de.fuballer.mcendgame.main.util.extension.WorldExtension.isDungeonWorld
import net.minecraft.gametest.framework.GameTestHelper
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.EntityTypes
import net.minecraft.world.entity.monster.skeleton.Skeleton
import net.minecraft.world.entity.monster.zombie.Zombie
import net.minecraft.world.level.Level
import net.minecraft.world.level.biome.Biomes
import org.slf4j.LoggerFactory

/**
 * Regression tests for the mixins that had to be re-ported from Minecraft 26.2 to 26.3.
 *
 * The tests intentionally drive the patched methods directly instead of relying on the server game
 * loop: gametest levels have no players, so nothing would tick entities or their clocks for us.
 * Every test therefore calls the same method the game would call and asserts on the result.
 *
 * Like [DungeonOpenAndJoinGameTest] this is not a gametest entrypoint itself; the main entrypoint
 * ([MCEndgameGameTest]) wires each function up to a `@GameTest` method.
 */
object MixinPortGameTest {
    private val LOG = LoggerFactory.getLogger(this::class.java.simpleName)

    /** Comfortably past the skeleton's 140 tick affliction time. */
    private const val AFFLICTION_TICKS = 200

    /**
     * `SkeletonConversionMixin` must keep dungeon skeletons from freeze-converting into strays
     * **without** aborting the rest of `Skeleton.tick()`.
     *
     * In 26.3 the conversion state lives in `ConversionTracker`, which `Skeleton.tick()` runs as its
     * first statement. Cancelling an injection at that call aborts the whole method, so the skeleton
     * would stop ticking entirely: no AI, no movement, no attacks.
     *
     * `Entity.baseTick()` (reached only through the `super.tick()` that follows the tracker) clears
     * `isInPowderSnow`, so leaving the flag untouched proves the tick never got that far.
     */
    fun testDungeonSkeletonTicksButNeverConverts(helper: GameTestHelper) {
        withDungeonWorld(helper) { dungeonWorld ->
            val skeleton = Skeleton(EntityTypes.SKELETON, dungeonWorld)
            skeleton.snapTo(0.5, 64.0, 0.5)
            // The tests only care about the tick chain; without this the skeleton would fall
            // through the void dungeon world and die long before the conversion timer matters.
            skeleton.isNoGravity = true

            repeat(AFFLICTION_TICKS) {
                // Powder snow is what ordinarily drives the stray conversion; 26.3 tracks it through
                // ConversionTracker.tick(), so a guarded tracker never accumulates it.
                skeleton.setIsInPowderSnow(true)
                skeleton.tick()
            }

            helper.assertTrue(
                !skeleton.isInPowderSnow,
                "SkeletonConversionMixin must not abort Skeleton.tick(): the tick never reached " +
                        "Entity.baseTick(), which clears isInPowderSnow"
            )
            helper.assertTrue(
                !skeleton.isShaking,
                "A skeleton inside a dungeon world must never start converting into a stray"
            )
            helper.assertTrue(
                skeleton.isAlive,
                "A dungeon skeleton must stay alive while its conversion is suppressed"
            )

            LOG.info("Dungeon skeleton ticked {} times, converting: {}", AFFLICTION_TICKS, skeleton.isShaking)
        }

        helper.succeed()
    }

    /**
     * The other half of [testDungeonSkeletonTicksButNeverConverts]: outside a dungeon the mixin must
     * stay out of the way, so the same powder-snow ticks still start the stray conversion. Without
     * this control a mixin that simply disabled `Skeleton.tick()` everywhere would pass.
     */
    fun testNonDungeonSkeletonStillConverts(helper: GameTestHelper) {
        val skeleton = Skeleton(EntityTypes.SKELETON, helper.level)
        skeleton.snapTo(0.5, 64.0, 0.5)
        // Otherwise the fall damage from the test structure kills it before it can convert.
        skeleton.isNoGravity = true

        repeat(AFFLICTION_TICKS) {
            skeleton.setIsInPowderSnow(true)
            skeleton.tick()
        }

        helper.assertTrue(
            !skeleton.isInPowderSnow,
            "A skeleton outside a dungeon must still run its own tick"
        )
        helper.assertTrue(
            skeleton.isShaking,
            "A skeleton outside a dungeon world should still freeze-convert after $AFFLICTION_TICKS afflicted ticks"
        )

        helper.succeed()
    }

    /**
     * `ZombieConversionMixin` reports `convertsInWater() == false` inside dungeon worlds, which is
     * what stops dungeon zombies from turning into drowned.
     */
    fun testDungeonZombieDoesNotConvertInWater(helper: GameTestHelper) {
        withDungeonWorld(helper) { dungeonWorld ->
            val dungeonZombie = TestZombie(EntityTypes.ZOMBIE, dungeonWorld)
            helper.assertTrue(
                !dungeonZombie.callConvertsInWater(),
                "A zombie inside a dungeon world must not convert into a drowned"
            )
        }

        val overworldZombie = TestZombie(EntityTypes.ZOMBIE, helper.level)
        helper.assertTrue(
            overworldZombie.callConvertsInWater(),
            "A zombie outside a dungeon world must still convert into a drowned"
        )

        helper.succeed()
    }

    /**
     * `ServerClockManagerMixin` backs [ServerClockManagerExtension], which the runtime dungeon worlds
     * use to drive their own clocks. Without the mixin the extension falls back to its default method
     * and throws, so this test fails loudly if the shadowed `clocks` field stops matching vanilla.
     */
    fun testDungeonClockManagerExposesClockInstances(helper: GameTestHelper) {
        withDungeonWorld(helper) { dungeonWorld ->
            val clockManager = dungeonWorld.clockManager()
            helper.assertTrue(
                clockManager is RuntimeClockManager,
                "A runtime dungeon world should use the mod's RuntimeClockManager, but got ${clockManager.javaClass.name}"
            )

            val clocks = (clockManager as ServerClockManagerExtension).`mcendgame$getClocks`()
            helper.assertTrue(
                clocks.isNotEmpty(),
                "The mixin-backed clock map must expose the world clocks of a dungeon world"
            )

            // createFullSyncPacket() routes through the same mixin-backed map.
            val packet = (clockManager as RuntimeClockManager).createFullSyncPacket()
            helper.assertTrue(
                packet.clockUpdates().isNotEmpty(),
                "A dungeon world's full clock sync packet should carry the world clock states"
            )

            // Dungeon worlds freeze time (ADVANCE_TIME = false), and the re-ported packNetworkState
            // is what has to advertise that as a stopped clock.
            val rates = packet.clockUpdates().values.map { it.rate() }
            helper.assertTrue(
                rates.all { it == 0.0F },
                "A dungeon world's clocks should be advertised as paused, but got $rates"
            )
        }

        helper.succeed()
    }

    /**
     * `LivingEntityDamageMixin` replaces `hurtServer` wholesale. 26.3 renamed the invulnerability
     * accessors that the port had to adopt, so this asserts both that damage is still applied and
     * that the hurt cooldown is still armed.
     *
     * The source is a real mob attack: the mod derives the damage from the attacker's attack damage
     * attribute, so an attacker-less source such as `damageSources().generic()` resolves to 0.
     */
    fun testDamageStillAppliesThroughMixin(helper: GameTestHelper) {
        val level = helper.level
        val attacker = Zombie(EntityTypes.ZOMBIE, level)
        attacker.snapTo(0.5, 64.0, 0.5)

        val victim = Zombie(EntityTypes.ZOMBIE, level)
        victim.snapTo(0.5, 64.0, 0.5)
        victim.isNoGravity = true

        val healthBefore = victim.health
        val source = level.damageSources().mobAttack(attacker)
        val applied = victim.hurtServer(level, source, 5.0f)

        helper.assertTrue(applied, "hurtServer should report that the damage was applied")
        helper.assertTrue(
            victim.health < healthBefore,
            "Damage applied through LivingEntityDamageMixin should reduce health (was $healthBefore, now ${victim.health})"
        )
        // 26.3 keeps the hurt cooldown in `damageCooldownTime`; `invulnerableTime` now means real
        // temporary invulnerability and must stay untouched or the damage above would be swallowed.
        helper.assertTrue(
            victim.damageCooldownTime > 0,
            "The hurt cooldown (damageCooldownTime) should be armed after taking damage"
        )
        helper.assertTrue(
            victim.getInvulnerableTime() == 0,
            "Taking damage must not flag the victim as temporarily invulnerable"
        )

        LOG.info("Victim health went from {} to {}, invulnerable for {} ticks", healthBefore, victim.health, victim.getInvulnerableTime())
        helper.succeed()
    }

    /**
     * Creates a real (but empty) runtime dungeon world for the duration of [build].
     *
     * A runtime world is the cheapest fixture that satisfies `Level.isDungeonWorld()`, which is what
     * `EntityConversionService` keys off. It is deleted again so it cannot be picked up by the dungeon
     * generation tests, which scan `server.allLevels` for dungeon worlds.
     */
    private fun <T> withDungeonWorld(helper: GameTestHelper, build: (ServerLevel) -> T): T {
        val server = helper.level.server
        val runtimeWorlds = RuntimeWorlds.create(server)

        val config = DungeonWorldSettings.getWorldConfig(Biomes.PLAINS, 6000)
        val handle: RuntimeLevelHandle = runtimeWorlds.openTemporaryDungeon(config)
        val dungeonWorld = handle.asLevel()

        helper.assertTrue(
            dungeonWorld.isDungeonWorld(),
            "The runtime world fixture should be recognised as a dungeon world"
        )

        try {
            return build(dungeonWorld)
        } finally {
            runtimeWorlds.tickDeleteLevel(dungeonWorld)
        }
    }

    /**
     * `Zombie.convertsInWater()` is protected, so a subclass is the cleanest way to reach the
     * mixin-patched implementation without reflection or a test-only accessor.
     */
    private class TestZombie(type: EntityType<out Zombie>, level: Level) : Zombie(type, level) {
        fun callConvertsInWater(): Boolean = convertsInWater()
    }
}
