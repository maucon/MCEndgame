package de.fuballer.mcendgame.component.crafting

import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.ItemUtil
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.enchantment.EnchantItemEvent

@Component
class EnchantmentTableCraftingService : Listener {

    @EventHandler
    fun on(event: EnchantItemEvent) {
        println(event.item.enchantments.size)
        ItemUtil.updateAttributesAndLore(event.item)//TODO doesnt work
    }
}