package de.fuballer.mcendgame.component.crafting.corruption

import de.fuballer.mcendgame.component.crafting.AnvilCraftingBaseService
import de.fuballer.mcendgame.component.item.equipment.Equipment
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.ItemUtil
import de.fuballer.mcendgame.util.extension.ItemStackExtension.getCorruptionRounds
import de.fuballer.mcendgame.util.extension.ItemStackExtension.getRolledAttributes
import de.fuballer.mcendgame.util.extension.ItemStackExtension.setRolledAttributes
import de.fuballer.mcendgame.util.extension.ItemStackExtension.setUnmodifiable
import de.fuballer.mcendgame.util.random.RandomUtil
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import kotlin.random.Random

@Component
class CorruptionService : AnvilCraftingBaseService() {
    override fun isBaseValid(base: ItemStack) =
        Equipment.existsByMaterial(base.type)

    override fun isCraftingItemValid(craftingItem: ItemStack) =
        craftingItem.getCorruptionRounds() != null

    override fun getResultPreview(base: ItemStack): ItemStack =
        base.also { it.setUnmodifiable() }

    override fun getResult(base: ItemStack, craftingItem: ItemStack): ItemStack {
        base.setUnmodifiable()

        val corruptionRounds = craftingItem.getCorruptionRounds()!!
        repeat(corruptionRounds) {
            if (base.type == Material.AIR) return@repeat

            corruptItem(base)
        }
        return base
    }

    private fun corruptItem(item: ItemStack) {
        val corruptions =
            if (ItemUtil.hasCustomAttributes(item)) CorruptionSettings.CORRUPTIONS
            else CorruptionSettings.ALTERNATE_CORRUPTIONS

        when (RandomUtil.pick(corruptions).option) {
            CorruptionModification.CORRUPT_ENCHANTS -> corruptEnchant(item)
            CorruptionModification.CORRUPT_ATTRIBUTES -> corruptAttributes(item)
            CorruptionModification.DESTROY -> item.type = Material.AIR
            CorruptionModification.DO_NOTHING -> {}
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