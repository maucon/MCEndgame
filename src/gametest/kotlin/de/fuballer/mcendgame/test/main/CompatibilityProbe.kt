package de.fuballer.mcendgame.test.main

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents
import net.minecraft.world.entity.LivingEntity
import java.util.concurrent.atomic.AtomicInteger

/**
 * Records whether the hooks that third-party mods rely on still run while MCEndgame is installed.
 *
 * - [dropAllDeathLootTail] is incremented by a test mixin mirroring Pufferfish's Skills' `@Inject(at = TAIL)`
 *   on `LivingEntity#dropAllDeathLoot` (the hook that awards kill skill XP).
 * - [allowDamage]/[afterDamage]/[allowDeath] are Fabric API events fired from inside
 *   `LivingEntity#hurtServer`; Better Revive relies on [allowDeath] and [afterDamage].
 *
 * Counters only observe [target] so tests running in parallel cannot influence each other.
 */
object CompatibilityProbe {
    @JvmField
    @Volatile
    var target: LivingEntity? = null

    @JvmField
    val allowDamage = AtomicInteger()

    @JvmField
    val afterDamage = AtomicInteger()

    @JvmField
    val allowDeath = AtomicInteger()

    @JvmField
    val dropAllDeathLootTail = AtomicInteger()

    @Volatile
    private var registered = false

    fun register() {
        if (registered) return
        registered = true

        ServerLivingEntityEvents.ALLOW_DAMAGE.register { entity, _, _ ->
            if (entity === target) allowDamage.incrementAndGet()
            true
        }
        ServerLivingEntityEvents.AFTER_DAMAGE.register { entity, _, _, _, _ ->
            if (entity === target) afterDamage.incrementAndGet()
        }
        ServerLivingEntityEvents.ALLOW_DEATH.register { entity, _, _ ->
            if (entity === target) allowDeath.incrementAndGet()
            true
        }
    }

    fun reset(entity: LivingEntity?) {
        target = entity
        allowDamage.set(0)
        afterDamage.set(0)
        allowDeath.set(0)
        dropAllDeathLootTail.set(0)
    }
}
