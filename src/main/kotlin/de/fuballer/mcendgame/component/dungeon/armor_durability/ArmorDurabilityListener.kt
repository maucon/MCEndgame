package de.fuballer.mcendgame.component.dungeon.armor_durability

import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.framework.stereotype.EventListener
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerItemDamageEvent

@Component
class ArmorDurabilityListener(
    private val armorDurabilityService: ArmorDurabilityService,
) : EventListener {
    @EventHandler
    fun onPlayerItemDamage(event: PlayerItemDamageEvent) = armorDurabilityService.onPlayerItemDamage(event)
}
