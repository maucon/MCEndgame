package de.fuballer.mcendgame.component.totem.totems.dodge

import de.fuballer.mcendgame.component.damage.DamageCalculationEvent
import de.fuballer.mcendgame.framework.annotation.Service
import de.fuballer.mcendgame.util.extension.EventExtension.cancel
import de.fuballer.mcendgame.util.extension.PlayerExtension.getHighestTotemTier
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import kotlin.random.Random

@Service
class DodgeEffectService : Listener {
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    fun on(event: DamageCalculationEvent) {
        if (!event.isDungeonWorld) return
        if (event.cause != EntityDamageEvent.DamageCause.PROJECTILE
            && event.cause != EntityDamageEvent.DamageCause.ENTITY_ATTACK
            && event.cause != EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK
        ) return

        val player = event.damaged as? Player ?: return
        val tier = player.getHighestTotemTier(DodgeTotemType) ?: return

        val (dodgeChance) = DodgeTotemType.getValues(tier)
        if (Random.nextDouble() > dodgeChance) return

        event.cancel()
    }
}