package de.fuballer.mcendgame.main.component.dungeon.enemy.equipment.attributes

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.setCustomAttributes
import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.main.component.dungeon.enemy.equipment.EquipmentGenerationData
import de.fuballer.mcendgame.main.component.entity.EnemyEquipmentClass
import de.fuballer.mcendgame.main.component.item.equipment.data.TieredRollableCustomAttribute
import de.fuballer.mcendgame.main.util.random.RandomOption
import de.fuballer.mcendgame.main.util.random.RandomUtil
import de.maucon.mauconframework.di.annotation.Injectable
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.item.ItemStack
import kotlin.random.Random

@Injectable
class AttributeService {
    fun applyAttributes(
        itemStack: ItemStack,
        attributeOptions: List<RandomOption<TieredRollableCustomAttribute>>,
        entityEquipmentClass: EnemyEquipmentClass,
        level: Int,
        random: Random,
        slot: EquipmentSlotGroup,
        data: EquipmentGenerationData,
    ) {
        val customAttributes = selectAttributes(level, attributeOptions, entityEquipmentClass, slot, random, data)
        itemStack.setCustomAttributes(customAttributes, slot)
    }

    private fun selectAttributes(
        level: Int,
        attributeOptions: List<RandomOption<TieredRollableCustomAttribute>>,
        entityEquipmentClass: EnemyEquipmentClass,
        slot: EquipmentSlotGroup,
        random: Random,
        data: EquipmentGenerationData,
    ): List<CustomAttribute> {
        val modifiedAttributeOptions = getEquipmentClassAffectedAttributeOptions(attributeOptions, entityEquipmentClass)
        if (modifiedAttributeOptions.isEmpty()) return listOf()

        val rolledAttributes = getDistinctRolledAttributes(level, modifiedAttributeOptions, slot, random, data)
        if (!data.luckyAttributes) return rolledAttributes

        val rolledAttributesB = getDistinctRolledAttributes(level, modifiedAttributeOptions, slot, random, data)
        return getBetterRolls(rolledAttributes, rolledAttributesB)
    }

    private fun getEquipmentClassAffectedAttributeOptions(
        attributeOptions: List<RandomOption<TieredRollableCustomAttribute>>,
        entityEquipmentClass: EnemyEquipmentClass,
    ): List<RandomOption<TieredRollableCustomAttribute>> {
        val weightFactors = entityEquipmentClass.getAttributeWeightFactors()
        if (weightFactors.isEmpty()) return attributeOptions

        return attributeOptions.mapNotNull { weightedOption ->
            val option = weightedOption.option
            val type = option.type

            val factor = weightFactors.getOrDefault(type, 1.0)
            if (factor == 1.0) return@mapNotNull weightedOption

            val newWeight = (weightedOption.weight * factor).toInt()
            if (newWeight <= 0) return@mapNotNull null

            RandomOption(newWeight, option)
        }
    }

    private fun getDistinctRolledAttributes(
        level: Int,
        attributeOptions: List<RandomOption<TieredRollableCustomAttribute>>,
        slot: EquipmentSlotGroup,
        random: Random,
        data: EquipmentGenerationData,
    ): List<CustomAttribute> {
        var statAmount = EnemyEquipmentAttributesSettings.getAttributeCount(level, random)
        data.additionalAttributeProbabilities.forEach {
            if (random.nextDouble() < it) statAmount++
        }

        val pickedTieredAttributes = RandomUtil.pickUnique(attributeOptions, random, statAmount)
        val tierRolls = EnemyEquipmentAttributesSettings.getAttributeTierRolls(level, random)
        val pickedAttributes = pickedTieredAttributes.map { it.rollTier(tierRolls, level, random) }

        return rollAttributes(pickedAttributes, slot, random)
    }

    private fun rollAttributes(
        attributes: List<RollableCustomAttribute>,
        slot: EquipmentSlotGroup,
        random: Random,
    ) = attributes
        .map {
            val percentageRolls = mutableListOf<Double>()
            repeat(it.bounds.size) {
                percentageRolls.add(random.nextDouble())
            }
            it.roll(percentageRolls, slot)
        }

    private fun getBetterRolls(
        rollsA: List<CustomAttribute>,
        rollsB: List<CustomAttribute>
    ): List<CustomAttribute> {
        val tierScoreA = getTierScore(rollsA)
        val tierScoreB = getTierScore(rollsB)
        if (tierScoreA != tierScoreB) return if (tierScoreA > tierScoreB) rollsA else rollsB

        return if (getAvgPercentRoll(rollsA) >= getAvgPercentRoll(rollsB)) rollsA else rollsB
    }

    private fun getTierScore(rolls: List<CustomAttribute>) = rolls.sumOf { 5 - it.tier }

    private fun getAvgPercentRoll(attributes: List<CustomAttribute>) = attributes.map { it.getAffinityBasedRollPercentage() }.average()
}