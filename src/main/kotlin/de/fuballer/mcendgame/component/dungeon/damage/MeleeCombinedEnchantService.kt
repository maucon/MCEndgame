package de.fuballer.mcendgame.component.dungeon.damage

import de.fuballer.mcendgame.component.damage.DamageCalculationEvent
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.DamageUtil
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent

@Component
class MeleeCombinedEnchantService : Listener {
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    fun on(event: DamageCalculationEvent) {
        if (!event.isDungeonWorld) return
        if (event.cause != EntityDamageEvent.DamageCause.ENTITY_ATTACK) return

        event.enchantDamage = DamageUtil.getCombinedMeleeEnchantDamage(event.damager)
    }
}