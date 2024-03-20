package de.fuballer.mcendgame.component.refinement

import de.fuballer.mcendgame.component.item.equipment.Equipment
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.ItemUtil
import de.fuballer.mcendgame.util.SchedulingUtil
import de.fuballer.mcendgame.util.extension.ItemStackExtension.getCustomItemType
import de.fuballer.mcendgame.util.extension.ItemStackExtension.getRolledAttributes
import de.fuballer.mcendgame.util.extension.ItemStackExtension.isRefinement
import de.fuballer.mcendgame.util.extension.ItemStackExtension.isUnmodifiable
import de.fuballer.mcendgame.util.extension.ItemStackExtension.setRolledAttributes
import org.bukkit.GameMode
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.inventory.PrepareAnvilEvent
import org.bukkit.inventory.InventoryView
import org.bukkit.inventory.ItemStack

@Component
class RefinementService : Listener {
    @EventHandler
    fun on(event: EntityDeathEvent) {
        event.entity.world.dropItemNaturally(event.entity.location, RefinementSettings.getRefinementItem())
    }

    @EventHandler
    fun on(event: PrepareAnvilEvent) {
        val inventory = event.inventory

        val base = inventory.getItem(0) ?: return
        if (base.isUnmodifiable()) return
        if (base.getCustomItemType() != null) return
        if (!hasMultipleAttributes(base)) return

        val refinement = inventory.getItem(1) ?: return

        if (!Equipment.existsByMaterial(base.type)) return
        if (!refinement.isRefinement()) return

        event.result = base.clone()

        SchedulingUtil.runTask {
            event.inventory.repairCost = 1

            event.inventory.viewers.forEach {
                it.setWindowProperty(InventoryView.Property.REPAIR_COST, 1)
            }
        }
    }

    @EventHandler
    fun on(event: InventoryClickEvent) {
        val inventory = event.inventory
        if (inventory.type != InventoryType.ANVIL) return
        if (event.rawSlot != 2) return

        val refinementOrb = inventory.getItem(1) ?: return
        if (!refinementOrb.isRefinement()) return

        val result = inventory.getItem(2) ?: return

        val player = event.whoClicked as Player
        if (player.gameMode != GameMode.CREATIVE && player.level < 1) return
        if (player.gameMode != GameMode.CREATIVE) {
            player.level -= 1
        }

        refineItem(result)
        ItemUtil.updateAttributesAndLore(result)

        player.world.playSound(player.location, Sound.BLOCK_ANVIL_USE, SoundCategory.PLAYERS, 1f, 1f)

        when (event.action) {
            InventoryAction.PICKUP_ALL, InventoryAction.PICKUP_ONE, InventoryAction.PICKUP_HALF, InventoryAction.PICKUP_SOME ->
                player.setItemOnCursor(result)

            InventoryAction.MOVE_TO_OTHER_INVENTORY ->
                player.inventory.addItem(result)

            else -> return
        }

        inventory.setItem(0, null)
        inventory.setItem(2, null)

        val refinementStack = inventory.getItem(1) ?: return
        refinementStack.amount -= 1
        inventory.setItem(1, refinementStack)

        event.isCancelled = true
    }

    private fun hasMultipleAttributes(item: ItemStack): Boolean {
        val attributes = item.getRolledAttributes() ?: return false
        return attributes.size >= 2
    }

    private fun refineItem(item: ItemStack) {
        val attributes = item.getRolledAttributes()?.toMutableList() ?: return
        val attributesBounds = ItemUtil.getEquipmentAttributes(item)

        val sacrificedAttribute = attributes.random()
        attributes.remove(sacrificedAttribute)

        val sacrificedAttributeBounds = attributesBounds.firstOrNull { it.type == sacrificedAttribute.type } ?: return
        val sacrificedAttributePercentage = (sacrificedAttribute.roll - sacrificedAttributeBounds.min) / (sacrificedAttributeBounds.max - sacrificedAttributeBounds.min)

        val enhancedAttribute = attributes.random()
        val enhancedAttributeBounds = attributesBounds.firstOrNull { it.type == enhancedAttribute.type } ?: return
        val enhancedAttributeRange = enhancedAttributeBounds.max - enhancedAttributeBounds.min

        enhancedAttribute.roll += RefinementSettings.refineAttributeValue(enhancedAttributeRange, sacrificedAttributePercentage)

        item.setRolledAttributes(attributes)
    }
}