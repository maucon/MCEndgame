package de.fuballer.mcendgame.component.crafting

import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.ItemUtil
import de.fuballer.mcendgame.util.extension.ItemStackExtension.getCustomAttributes
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.enchantment.EnchantItemEvent

@Component
class EnchantmentTableCraftingService : Listener {
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    fun on(event: EnchantItemEvent) {
        if (event.item.getCustomAttributes() == null) return

        val clone = event.item.clone()
        clone.addEnchantments(event.enchantsToAdd)
        ItemUtil.updateAttributesAndLore(clone)
        val cloneMeta = clone.itemMeta ?: return

        val meta = event.item.itemMeta ?: return
        meta.lore = cloneMeta.lore
        event.item.itemMeta = meta
    }
}