package de.fuballer.mcendgame.component.corruption

import de.fuballer.mcendgame.component.corruption.data.CorruptionChanceType
import de.fuballer.mcendgame.component.item.equipment.Equipment
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.ItemUtil
import de.fuballer.mcendgame.util.SchedulingUtil
import de.fuballer.mcendgame.util.extension.ItemStackExtension.getCorruptionRounds
import de.fuballer.mcendgame.util.extension.ItemStackExtension.getRolledAttributes
import de.fuballer.mcendgame.util.extension.ItemStackExtension.isUnmodifiable
import de.fuballer.mcendgame.util.extension.ItemStackExtension.setRolledAttributes
import de.fuballer.mcendgame.util.extension.ItemStackExtension.setUnmodifiable
import de.fuballer.mcendgame.util.random.RandomUtil
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.inventory.PrepareAnvilEvent
import org.bukkit.inventory.InventoryView
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import kotlin.random.Random

@Component
class CorruptionService : Listener {
    @EventHandler
    fun on(event: PrepareAnvilEvent) {
        val inventory = event.inventory

        val base = inventory.getItem(0) ?: return
        val corruption = inventory.getItem(1) ?: return

        if (!Equipment.existsByMaterial(base.type)) return
        if (corruption.getCorruptionRounds() == null) return

        val result = base.clone()
        result.setUnmodifiable()
        ItemUtil.updateAttributesAndLore(result)

        event.result = result

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
        if (event.slot != 2) return

        val result = inventory.getItem(2) ?: return
        if (!result.isUnmodifiable()) return

        val player = event.whoClicked as Player
        if (player.gameMode != GameMode.CREATIVE && player.level < 1) return
        if (player.gameMode != GameMode.CREATIVE) {
            player.level -= 1
        }

        val corruption = inventory.getItem(1) ?: return
        val corruptionRounds = corruption.getCorruptionRounds() ?: return

        repeat(corruptionRounds) {
            if (result.type == Material.AIR) return@repeat

            corruptItem(result)
        }

        val sound = if (result.type == Material.AIR) Sound.ENTITY_ITEM_BREAK else Sound.BLOCK_ANVIL_USE
        player.world.playSound(player.location, sound, SoundCategory.PLAYERS, 1f, 1f)

        result.setUnmodifiable()
        ItemUtil.updateAttributesAndLore(result)

        when (event.action) {
            InventoryAction.PICKUP_ALL, InventoryAction.PICKUP_ONE, InventoryAction.PICKUP_HALF, InventoryAction.PICKUP_SOME ->
                player.setItemOnCursor(result)

            InventoryAction.MOVE_TO_OTHER_INVENTORY ->
                player.inventory.addItem(result)

            else -> return
        }

        inventory.setItem(0, null)
        inventory.setItem(2, null)

        val corruptionStack = inventory.getItem(1) ?: return
        corruptionStack.amount -= 1
        inventory.setItem(1, corruptionStack)

        event.isCancelled = true
    }

    private fun corruptItem(item: ItemStack) {
        val corruptions =
            if (ItemUtil.hasCustomAttributes(item)) CorruptionSettings.CORRUPTIONS
            else CorruptionSettings.ALTERNATE_CORRUPTIONS

        when (RandomUtil.pick(corruptions).option) {
            CorruptionChanceType.CORRUPT_ENCHANTS -> corruptEnchant(item)
            CorruptionChanceType.CORRUPT_ATTRIBUTES -> corruptAttributes(item)
            CorruptionChanceType.DESTROY -> item.type = Material.AIR
            CorruptionChanceType.DO_NOTHING -> {}
        }
    }

    private fun corruptEnchant(item: ItemStack) {
        val meta = item.itemMeta ?: return

        if (Random.nextBoolean()) {
            increaseEnchantLevel(item, meta)
        } else {
            decreaseEnchantLevel(item, meta)
        }
    }

    private fun increaseEnchantLevel(item: ItemStack, meta: ItemMeta) {
        val equipment = Equipment.fromMaterial(item.type) ?: return

        val possibleEnchants = equipment.rollableEnchants
            .map { it.option.enchantment }
            .distinct()
            .toMutableList()

        for (count in 1 until CorruptionSettings.PRESENT_ENCHANT_WEIGHT_MULTIPLIER) {
            possibleEnchants.addAll(meta.enchants.keys)
        }

        val enchantment = possibleEnchants[Random.nextInt(possibleEnchants.size)]
        meta.addEnchant(enchantment, meta.getEnchantLevel(enchantment) + 1, true)

        item.itemMeta = meta
    }

    private fun decreaseEnchantLevel(item: ItemStack, meta: ItemMeta) {
        val enchants = meta.enchants.keys.toMutableList()
        if (enchants.isEmpty()) return

        val enchantment = enchants[Random.nextInt(enchants.size)]

        val level = meta.getEnchantLevel(enchantment)
        if (level == 1) {
            meta.removeEnchant(enchantment)
        } else {
            meta.addEnchant(enchantment, level - 1, true)
        }

        item.itemMeta = meta
    }

    private fun corruptAttributes(item: ItemStack) {
        val attributesBounds = ItemUtil.getEquipmentAttributes(item)
        val attributes = item.getRolledAttributes() ?: return
        val chosenAttribute = attributes.randomOrNull() ?: return

        val attributeBounds = attributesBounds.firstOrNull { it.type == chosenAttribute.type } ?: return
        chosenAttribute.roll = CorruptionSettings.corruptAttributeValue(attributeBounds, chosenAttribute.roll, Random.nextDouble())

        item.setRolledAttributes(attributes)
    }
}