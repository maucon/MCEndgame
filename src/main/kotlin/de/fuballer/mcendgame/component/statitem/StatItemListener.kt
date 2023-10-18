package de.fuballer.mcendgame.component.statitem

import de.fuballer.mcendgame.framework.annotation.Service
import de.fuballer.mcendgame.framework.stereotype.EventListener
import org.bukkit.event.EventHandler
import org.bukkit.event.enchantment.EnchantItemEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.PrepareAnvilEvent
import org.bukkit.event.inventory.PrepareSmithingEvent

@Service
class StatItemListener(
    private val statItemService: StatItemService
) : EventListener {
    @EventHandler
    fun onEnchant(event: EnchantItemEvent) = statItemService.onEnchant(event)

    @EventHandler
    fun onAnvilPrepare(event: PrepareAnvilEvent) = statItemService.onAnvilPrepare(event)

    @EventHandler
    fun onSmithingPrepare(event: PrepareSmithingEvent) = statItemService.onSmithingPrepare(event)

    @EventHandler
    fun onGrindstoneUse(event: InventoryClickEvent) = statItemService.onGrindstoneUse(event)
}
