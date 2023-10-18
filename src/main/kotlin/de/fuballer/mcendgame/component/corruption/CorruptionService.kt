package de.fuballer.mcendgame.component.corruption

import de.fuballer.mcendgame.component.corruption.data.AttributeWithModifier
import de.fuballer.mcendgame.component.corruption.data.CorruptionChanceType
import de.fuballer.mcendgame.component.statitem.StatItemSettings
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.AttributeUtil
import de.fuballer.mcendgame.util.PluginUtil
import de.fuballer.mcendgame.util.random.RandomUtil
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.Player
import org.bukkit.event.inventory.*
import org.bukkit.inventory.InventoryView
import org.bukkit.inventory.ItemStack
import java.util.*
import kotlin.math.abs

@Component
class CorruptionService {
    private val random = Random()

    fun onAnvilPrepare(event: PrepareAnvilEvent) {
        val inventory = event.inventory

        val base = inventory.getItem(0) ?: return
        val corruption = inventory.getItem(1) ?: return

        if (!StatItemSettings.MATERIAL_TO_EQUIPMENT.containsKey(base.type)) return
        if (!corruption.isSimilar(CorruptionSettings.getCorruptionItem())
            && !corruption.isSimilar(CorruptionSettings.getDoubleCorruptionItem())
        ) return

        event.result = addCorruptionTag(base.clone())

        PluginUtil.scheduleTask {
            event.inventory.repairCost = 1

            for (viewer in event.inventory.viewers) {
                (viewer as? Player)?.setWindowProperty(InventoryView.Property.REPAIR_COST, 1)
            }
        }
    }

    fun onInventoryClick(event: InventoryClickEvent) {
        inventoryClickCorruption(event)
        preventCorruptedItemModification(event)
    }

    private fun inventoryClickCorruption(event: InventoryClickEvent) {
        val inventory = event.inventory
        if (inventory.type != InventoryType.ANVIL) return
        if (event.slot != 2) return

        val result = inventory.getItem(2) ?: return
        val resultMeta = result.itemMeta ?: return
        val resultLore = resultMeta.lore ?: return
        if (!resultLore.contains(CorruptionSettings.CORRUPTION_TAG_LORE[0])) return

        val player = event.whoClicked as Player
        if (player.gameMode != GameMode.CREATIVE && player.level < 1) return

        val corruption = inventory.getItem(1) ?: return
        val corruptionMeta = corruption.itemMeta ?: return
        val corruptionLore = corruptionMeta.lore ?: return

        if (player.gameMode != GameMode.CREATIVE) {
            player.level -= 1
        }

        corruptItem(result, player)
        if (result.type != Material.AIR && corruptionLore.contains(CorruptionSettings.ITEM_LORE_DOUBLE[0])) {
            corruptItem(result, player)
        }

        if (result.type != Material.AIR) {
            player.world.playSound(player.location, Sound.BLOCK_ANVIL_USE, SoundCategory.PLAYERS, 1f, 1f)
        }

        AttributeUtil.setAttributeLore(result, true)

        when (event.action) {
            InventoryAction.PICKUP_ALL, InventoryAction.PICKUP_ONE, InventoryAction.PICKUP_HALF, InventoryAction.PICKUP_SOME ->
                player.setItemOnCursor(result)

            InventoryAction.MOVE_TO_OTHER_INVENTORY ->
                player.inventory.addItem(result)

            else -> {
                return
            }
        }

        inventory.setItem(0, null)
        inventory.setItem(2, null)

        val corruptionStack = inventory.getItem(1) ?: return
        corruptionStack.amount = corruptionStack.amount - 1
        inventory.setItem(1, corruptionStack)

        event.isCancelled = true
    }

    private fun addCorruptionTag(item: ItemStack): ItemStack {
        val meta = item.itemMeta ?: return item
        val lore = meta.lore

        if (lore != null) {
            if (lore.contains(CorruptionSettings.CORRUPTION_TAG_LORE[0])) return item
            lore.addAll(CorruptionSettings.CORRUPTION_TAG_LORE)
            meta.lore = lore
        } else {
            meta.lore = CorruptionSettings.CORRUPTION_TAG_LORE
        }

        return item.apply { itemMeta = meta }
    }

    private fun corruptItem(item: ItemStack, player: Player) {
        item.itemMeta ?: return

        val corruptions =
            if (hasExtraAttributes(item)) CorruptionSettings.CORRUPTIONS
            else CorruptionSettings.ALTERNATE_CORRUPTIONS

        when (RandomUtil.pick(corruptions).option) {
            CorruptionChanceType.CORRUPT_ENCHANTS -> corruptEnchant(item)
            CorruptionChanceType.CORRUPT_STATS -> corruptStats(item)
            CorruptionChanceType.CORRUPT_DESTROY -> {
                item.type = Material.AIR
                player.world.playSound(player.location, Sound.ENTITY_ITEM_BREAK, SoundCategory.PLAYERS, 1f, 1f)
            }

            CorruptionChanceType.DO_NOTHING -> {}
        }
    }

    private fun hasExtraAttributes(item: ItemStack): Boolean {
        val meta = item.itemMeta ?: return false

        val attributes = meta.attributeModifiers ?: return false
        if (attributes.isEmpty) return false

        val equipment = StatItemSettings.MATERIAL_TO_EQUIPMENT[item.type] ?: return false
        if (equipment.baseAttributes.isEmpty()) return false

        return attributes.size() > equipment.baseAttributes.size
    }

    private fun corruptEnchant(item: ItemStack) {
        val meta = item.itemMeta ?: return

        if (random.nextBoolean()) {
            val equipment = StatItemSettings.MATERIAL_TO_EQUIPMENT[item.type] ?: return
            val possibleEnchants = equipment.enchantOptions
                .map { it.option.enchantment }
                .distinct()
                .toMutableList()
            for (count in 1 until CorruptionSettings.PRESENT_ENCHANT_WEIGHT_MULTIPLIER)
                possibleEnchants.addAll(meta.enchants.keys)

            val enchantment = possibleEnchants[random.nextInt(possibleEnchants.size)]
            meta.addEnchant(enchantment, meta.getEnchantLevel(enchantment) + 1, true)

        } else {
            val enchants = meta.enchants.keys.toMutableList()
            if (enchants.isEmpty()) return

            val enchantment = enchants[random.nextInt(enchants.size)]

            val level = meta.getEnchantLevel(enchantment)
            if (level == 1) {
                meta.removeEnchant(enchantment)
            } else {
                meta.addEnchant(enchantment, level - 1, true)
            }
        }

        item.itemMeta = meta
    }

    private fun corruptStats(item: ItemStack) {
        val meta = item.itemMeta ?: return
        val possibleAttributes = meta.attributeModifiers ?: return

        val possibleAttributesList = possibleAttributes.entries()
            .map { (attribute, attributeModifier) -> AttributeWithModifier(attribute, attributeModifier) }
            .toMutableList()

        val equipment = StatItemSettings.MATERIAL_TO_EQUIPMENT[item.type]

        if (equipment != null && equipment.baseAttributes.isNotEmpty()) {
            val baseAttributes = equipment.baseAttributes

            for ((baseAttribute, modifier) in baseAttributes) {
                val baseValue = AttributeUtil.getActualAttributeValue(baseAttribute, modifier)

                for (possibleAttribute in possibleAttributesList) {
                    if (baseAttribute != possibleAttribute.attribute
                        || abs(baseValue - possibleAttribute.modifier.amount) > 0.00001
                    ) continue

                    possibleAttributesList.remove(possibleAttribute)
                    break
                }
            }
        }

        if (possibleAttributesList.size < 1) return

        val (attribute, modifier) = possibleAttributesList[random.nextInt(possibleAttributesList.size)]

        meta.removeAttributeModifier(attribute, modifier)
        meta.addAttributeModifier(
            attribute,
            AttributeModifier(
                UUID.randomUUID(),
                modifier.name,
                modifier.amount * (0.5 + random.nextDouble()),
                AttributeModifier.Operation.ADD_NUMBER,
                modifier.slot
            )
        )

        item.itemMeta = meta
    }

    fun onCrafting(event: CraftItemEvent) {
        val isAnyItemCorrupted = event.inventory.storageContents
            .any { hasCorruptionTag(it) }

        if (isAnyItemCorrupted) {
            event.isCancelled = true
        }
    }

    private fun preventCorruptedItemModification(event: InventoryClickEvent) {
        val inventory = event.inventory
        if (isAllowedInventoryType(inventory.type)) return

        val inventorySize = inventory.size
        val rawSlot = event.rawSlot

        val item = if (rawSlot >= inventorySize) {
            when (event.action) {
                InventoryAction.MOVE_TO_OTHER_INVENTORY -> event.currentItem
                else -> null
            }
        } else {
            when (event.action) {
                InventoryAction.PLACE_ONE, InventoryAction.PLACE_ALL, InventoryAction.PLACE_SOME -> event.cursor
                InventoryAction.HOTBAR_SWAP ->
                    if (event.hotbarButton >= 0) event.whoClicked.inventory.getItem(event.hotbarButton)
                    else event.whoClicked.inventory.itemInOffHand

                else -> null
            }
        } ?: return

        if (hasCorruptionTag(item)) {
            event.isCancelled = true
        }
    }

    fun onInventoryDrag(event: InventoryDragEvent) {
        val item = event.oldCursor
        if (!hasCorruptionTag(item)) return

        val inventory = event.inventory
        if (isAllowedInventoryType(inventory.type)) return

        val isAnySlotNotInInventory = event.rawSlots
            .any { it < inventory.size }

        if (isAnySlotNotInInventory) {
            event.isCancelled = true
        }
    }

    private fun hasCorruptionTag(item: ItemStack): Boolean {
        val meta = item.itemMeta ?: return false
        val lore = meta.lore ?: return false

        return lore.contains(CorruptionSettings.CORRUPTION_TAG_LORE[0])
    }

    private fun isAllowedInventoryType(inventoryType: InventoryType) = inventoryType != InventoryType.GRINDSTONE
            && inventoryType != InventoryType.ANVIL
            && inventoryType != InventoryType.ENCHANTING
            && inventoryType != InventoryType.SMITHING
}
