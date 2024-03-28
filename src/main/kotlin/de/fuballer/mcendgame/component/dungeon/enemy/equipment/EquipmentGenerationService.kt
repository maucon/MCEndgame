package de.fuballer.mcendgame.component.dungeon.enemy.equipment

import de.fuballer.mcendgame.component.dungeon.enemy.equipment.enchantment.EquipmentEnchantmentService
import de.fuballer.mcendgame.component.item.attribute.RollableAttribute
import de.fuballer.mcendgame.component.item.equipment.Equipment
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.ItemUtil
import de.fuballer.mcendgame.util.extension.ItemStackExtension.setRolledAttributes
import de.fuballer.mcendgame.util.random.RandomOption
import de.fuballer.mcendgame.util.random.RandomUtil
import de.fuballer.mcendgame.util.random.SortableRandomOption
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemStack
import kotlin.random.Random

@Component
class EquipmentGenerationService(
    private val equipmentEnchantmentService: EquipmentEnchantmentService
) {
    fun setCreatureEquipment(
        random: Random,
        livingEntity: LivingEntity,
        mapTier: Int,
        weapons: Boolean,
        ranged: Boolean,
        armor: Boolean,
    ) {
        val equipment = livingEntity.equipment!!

        if (weapons) {
            createMainHandItem(random, mapTier, ranged)?.also {
                equipment.setItemInMainHand(it)
                equipment.itemInMainHandDropChance = 0f
            }
            createOffHandItem(random, mapTier)?.also {
                equipment.setItemInOffHand(it)
                equipment.itemInOffHandDropChance = 0f
            }
        }

        if (armor) {
            createRandomSortableEquipment(random, mapTier, EquipmentGenerationSettings.HELMETS)?.also {
                equipment.helmet = it
                equipment.helmetDropChance = 0f
            }
            createRandomSortableEquipment(random, mapTier, EquipmentGenerationSettings.CHESTPLATES)?.also {
                equipment.chestplate = it
                equipment.chestplateDropChance = 0f
            }
            createRandomSortableEquipment(random, mapTier, EquipmentGenerationSettings.LEGGINGS)?.also {
                equipment.leggings = it
                equipment.leggingsDropChance = 0f
            }
            createRandomSortableEquipment(random, mapTier, EquipmentGenerationSettings.BOOTS)?.also {
                equipment.boots = it
                equipment.bootsDropChance = 0f
            }
        }
    }

    private fun createMainHandItem(
        random: Random,
        mapTier: Int,
        ranged: Boolean
    ): ItemStack? {
        if (ranged) return createRangedMainHandItem(random, mapTier)

        val itemProbability = RandomUtil.pick(EquipmentGenerationSettings.MAINHAND_PROBABILITIES, random).option ?: return null
        return createRandomSortableEquipment(random, mapTier, itemProbability)
    }

    private fun createRangedMainHandItem(
        random: Random,
        mapTier: Int
    ): ItemStack? {
        val itemProbability = RandomUtil.pick(EquipmentGenerationSettings.RANGED_MAINHAND_PROBABILITIES, random).option
        return createRandomSortableEquipment(random, mapTier, itemProbability)
    }

    private fun createOffHandItem(
        random: Random,
        mapTier: Int
    ): ItemStack? {
        if (random.nextDouble() < EquipmentGenerationSettings.OFFHAND_OTHER_OVER_MAINHAND_PROBABILITY) {
            return createRandomEquipment(random, mapTier, EquipmentGenerationSettings.OTHER_ITEMS)
        }

        val itemProbability = RandomUtil.pick(EquipmentGenerationSettings.MAINHAND_PROBABILITIES, random).option ?: return null
        return createRandomSortableEquipment(random, mapTier, itemProbability)
    }

    private fun createRandomSortableEquipment(
        random: Random,
        mapTier: Int,
        equipmentProbabilities: List<SortableRandomOption<out Equipment?>>
    ): ItemStack? {
        val rolls = EquipmentGenerationSettings.calculateEquipmentRollTries(mapTier)
        val equipment = RandomUtil.pick(equipmentProbabilities, rolls, random).option ?: return null

        return createEquipment(random, equipment, mapTier)
    }

    private fun createRandomEquipment(
        random: Random,
        mapTier: Int,
        equipmentProbabilities: List<RandomOption<out Equipment>>
    ): ItemStack {
        val equipment = RandomUtil.pick(equipmentProbabilities, random).option
        return createEquipment(random, equipment, mapTier)
    }

    private fun createEquipment(
        random: Random,
        equipment: Equipment,
        mapTier: Int
    ): ItemStack {
        val item = ItemStack(equipment.material)
        val itemMeta = item.itemMeta ?: return item

        equipmentEnchantmentService.enchantItem(random, mapTier, itemMeta, equipment.rollableEnchants)
        item.itemMeta = itemMeta

        addCustomAttributes(item, random, mapTier, equipment)
        ItemUtil.updateAttributesAndLore(item)

        return item
    }

    private fun addCustomAttributes(
        item: ItemStack,
        random: Random,
        mapTier: Int,
        equipment: Equipment
    ) {
        val statAmount = RandomUtil.pick(EquipmentGenerationSettings.STAT_AMOUNTS, mapTier, random).option
        val rollableAttributesCopy = equipment.rollableAttributes.toMutableList()

        val pickedAttributes = mutableListOf<RollableAttribute>()
        repeat(statAmount) {
            if (rollableAttributesCopy.isEmpty()) return@repeat
            val pickedAttribute = RandomUtil.pick(rollableAttributesCopy, random)

            pickedAttributes.add(pickedAttribute.option)
            rollableAttributesCopy.remove(pickedAttribute)
        }

        val rolledAttributes = pickedAttributes.sortedBy { it.type.ordinal }
            .map { it.roll(mapTier) }

        item.setRolledAttributes(rolledAttributes)
    }
}