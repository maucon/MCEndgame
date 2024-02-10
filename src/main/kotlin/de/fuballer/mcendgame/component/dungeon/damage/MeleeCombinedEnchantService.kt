package de.fuballer.mcendgame.component.dungeon.damage

import de.fuballer.mcendgame.event.DamageCalculationEvent
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.DamageUtil
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener

@Component
class MeleeCombinedEnchantService : Listener {
    @EventHandler(priority = EventPriority.LOW)
    fun on(event: DamageCalculationEvent) {
        if (!event.isDungeonWorld) return

        event.enchantDamage = DamageUtil.getCombinedMeleeDamage(event.damager)
    }
}