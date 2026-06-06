package de.fuballer.mcendgame.main.component.dungeon.enemy.equipment.attributes

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.setCustomAttributes
import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.main.component.dungeon.enemy.equipment.EquipmentGenerationData
import de.fuballer.mcendgame.main.util.random.LevelRestrictedRandomOption
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
        possibleAttributes: List<RandomOption<List<LevelRestrictedRandomOption<RollableCustomAttribute>>>>,
        level: Int,
        random: Random,
        slot: EquipmentSlotGroup,
        data: EquipmentGenerationData,
    ) {
        val customAttributes = selectAttributes(level, possibleAttributes, slot, random, data)
        itemStack.setCustomAttributes(customAttributes, slot)
    }

    private fun selectAttributes(
        level: Int,
        possibleAttributes: List<RandomOption<List<LevelRestrictedRandomOption<RollableCustomAttribute>>>>,
        slot: EquipmentSlotGroup,
        random: Random,
        data: EquipmentGenerationData,
    ): List<CustomAttribute> {
        val rolledAttributes = getDistinctRolledAttributes(level, possibleAttributes, slot, random, data)
        if (!data.luckyAttributes) return rolledAttributes

        val rolledAttributesB = getDistinctRolledAttributes(level, possibleAttributes, slot, random, data)
        return getBetterRolls(rolledAttributes, rolledAttributesB)
    }

    private fun getDistinctRolledAttributes(
        level: Int,
        possibleAttributes: List<RandomOption<List<LevelRestrictedRandomOption<RollableCustomAttribute>>>>,
        slot: EquipmentSlotGroup,
        random: Random,
        data: EquipmentGenerationData,
    ): List<CustomAttribute> {
        var statAmount = AttributeSettings.getAttributeCount(level, random)
        data.additionalAttributeProbabilities.forEach {
            if (random.nextDouble() < it) statAmount++
        }

        val pickedAttributeTypes = RandomUtil.pickUnique(possibleAttributes, random, statAmount)
        val tierRolls = AttributeSettings.getAttributeTierRolls(level, random)
        val pickedAttributes = pickedAttributeTypes.map { RandomUtil.pickLevelRestricted(it, tierRolls, level, random) }

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