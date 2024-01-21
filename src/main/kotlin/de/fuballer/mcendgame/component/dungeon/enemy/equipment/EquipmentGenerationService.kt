package de.fuballer.mcendgame.component.dungeon.enemy.equipment

import de.fuballer.mcendgame.domain.attribute.RollableAttribute
import de.fuballer.mcendgame.domain.equipment.Equipment
import de.fuballer.mcendgame.domain.equipment.ItemEnchantment
import de.fuballer.mcendgame.domain.technical.persistent_data.TypeKeys
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.ItemUtil
import de.fuballer.mcendgame.util.PersistentDataUtil
import de.fuballer.mcendgame.util.random.RandomOption
import de.fuballer.mcendgame.util.random.RandomUtil
import de.fuballer.mcendgame.util.random.SortableRandomOption
import org.bukkit.entity.LivingEntity
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import java.util.*

@Component
class EquipmentGenerationService : Listener {
    private val random = Random()

    fun setCreatureEquipment(
        livingEntity: LivingEntity,
        mapTier: Int,
        weapons: Boolean,
        ranged: Boolean,
        armor: Boolean,
    ) {
        val equipment = livingEntity.equipment!!

        if (weapons) {
            createMainHandItem(mapTier, ranged)?.also { equipment.setItemInMainHand(it) }
            createOffHandItem(mapTier)?.also { equipment.setItemInOffHand(it) }
        }

        if (armor) {
            createRandomSortableEquipment(mapTier, EquipmentGenerationSettings.HELMETS)?.also { equipment.helmet = it }
            createRandomSortableEquipment(mapTier, EquipmentGenerationSettings.CHESTPLATES)?.also { equipment.chestplate = it }
            createRandomSortableEquipment(mapTier, EquipmentGenerationSettings.LEGGINGS)?.also { equipment.leggings = it }
            createRandomSortableEquipment(mapTier, EquipmentGenerationSettings.BOOTS)?.also { equipment.boots = it }
        }
    }

    private fun createMainHandItem(mapTier: Int, ranged: Boolean): ItemStack? {
        if (ranged) return createRangedMainHandItem(mapTier)

        val itemProbability = RandomUtil.pick(EquipmentGenerationSettings.MAINHAND_PROBABILITIES).option ?: return null
        return createRandomSortableEquipment(mapTier, itemProbability)
    }

    private fun createRangedMainHandItem(mapTier: Int): ItemStack? {
        val itemProbability = RandomUtil.pick(EquipmentGenerationSettings.RANGED_MAINHAND_PROBABILITIES).option
        return createRandomSortableEquipment(mapTier, itemProbability)
    }

    private fun createOffHandItem(mapTier: Int): ItemStack? {
        if (random.nextDouble() < EquipmentGenerationSettings.OFFHAND_OTHER_OVER_MAINHAND_PROBABILITY) {
            return createRandomEquipment(mapTier, EquipmentGenerationSettings.OTHER_ITEMS)
        }

        val itemProbability = RandomUtil.pick(EquipmentGenerationSettings.MAINHAND_PROBABILITIES).option ?: return null
        return createRandomSortableEquipment(mapTier, itemProbability)
    }

    private fun createRandomSortableEquipment(
        mapTier: Int,
        equipmentProbabilities: List<SortableRandomOption<out Equipment?>>
    ): ItemStack? {
        val rolls = EquipmentGenerationSettings.calculateEquipmentRollTries(mapTier)
        val equipment = RandomUtil.pick(equipmentProbabilities, rolls).option ?: return null

        return createEquipment(equipment, mapTier)
    }

    private fun createRandomEquipment(
        mapTier: Int,
        equipmentProbabilities: List<RandomOption<out Equipment>>
    ): ItemStack {
        val equipment = RandomUtil.pick(equipmentProbabilities).option
        return createEquipment(equipment, mapTier)
    }

    private fun createEquipment(
        equipment: Equipment,
        mapTier: Int
    ): ItemStack {
        val item = ItemStack(equipment.material)
        val itemMeta = item.itemMeta ?: return item

        addEnchants(mapTier, itemMeta, equipment.rollableEnchants)
        addCustomAttributes(mapTier, equipment, itemMeta)
        item.itemMeta = itemMeta

        ItemUtil.updateAttributesAndLore(item)

        return item
    }

    private fun addEnchants(
        mapTier: Int,
        itemMeta: ItemMeta,
        enchants: List<RandomOption<ItemEnchantment>>
    ) {
        repeat(EquipmentGenerationSettings.calculateEnchantTries(mapTier)) {
            val itemEnchantment = RandomUtil.pick(enchants).option

            if (itemMeta.getEnchantLevel(itemEnchantment.enchantment) < itemEnchantment.level) {
                itemMeta.addEnchant(itemEnchantment.enchantment, itemEnchantment.level, true)
            }
        }
    }

    private fun addCustomAttributes(
        mapTier: Int,
        equipment: Equipment,
        itemMeta: ItemMeta
    ) {
        val statAmount = RandomUtil.pick(EquipmentGenerationSettings.STAT_AMOUNTS, mapTier).option
        val rollableAttributesCopy = equipment.rollableAttributes.toMutableList()

        val pickedAttributes = mutableListOf<RollableAttribute>()
        repeat(statAmount) {
            if (rollableAttributesCopy.isEmpty()) return@repeat
            val pickedAttribute = RandomUtil.pick(rollableAttributesCopy)

            pickedAttributes.add(pickedAttribute.option)
            rollableAttributesCopy.remove(pickedAttribute)
        }

        val rolledAttributes = pickedAttributes.sortedBy { it.type.ordinal }
            .map { it.roll(mapTier) }

        PersistentDataUtil.setValue(itemMeta, TypeKeys.ATTRIBUTES, rolledAttributes)
    }
}