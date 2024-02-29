package de.fuballer.mcendgame.component.dungeon.enemy.equipment.enchantment

import de.fuballer.mcendgame.component.item.equipment.ItemEnchantment
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.random.RandomOption
import de.fuballer.mcendgame.util.random.RandomUtil
import org.bukkit.inventory.meta.ItemMeta
import kotlin.random.Random

@Component
class EquipmentEnchantmentService {
    fun enchantItem(
        random: Random,
        mapTier: Int,
        itemMeta: ItemMeta,
        enchants: List<RandomOption<ItemEnchantment>>
    ) {
        selectEnchantments(random, mapTier, enchants).forEach { itemEnchantment ->
            val existingLevel = itemMeta.getEnchantLevel(itemEnchantment.enchantment)

            if (existingLevel < itemEnchantment.level) {
                itemMeta.addEnchant(itemEnchantment.enchantment, itemEnchantment.level, true)
            }
        }
    }

    private fun selectEnchantments(
        random: Random,
        mapTier: Int,
        enchants: List<RandomOption<ItemEnchantment>>
    ): Set<ItemEnchantment> {
        val pickedEnchantments = mutableSetOf<ItemEnchantment>()

        repeat(EquipmentEnchantmentSettings.calculateEnchantTries(mapTier)) {
            val potentialEnchant = RandomUtil.pick(enchants, random).option
            val enchantmentsNotConflicting = pickedEnchantments.none { it.enchantment.conflictsWith(potentialEnchant.enchantment) }
            if (enchantmentsNotConflicting) {
                pickedEnchantments.add(potentialEnchant)
            }
        }

        return pickedEnchantments
    }
}