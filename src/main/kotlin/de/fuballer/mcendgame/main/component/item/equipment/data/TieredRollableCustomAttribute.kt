package de.fuballer.mcendgame.main.component.item.equipment.data

import de.fuballer.mcendgame.main.component.custom_attribute.data.AttributeType
import de.fuballer.mcendgame.main.component.custom_attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.main.util.random.LevelRestrictedRandomOption
import de.fuballer.mcendgame.main.util.random.RandomUtil
import kotlin.random.Random

data class TieredRollableCustomAttribute(
    val type: AttributeType,
    val tiers: List<LevelRestrictedRandomOption<AttributeTierData>>,
) {
    fun rollTier(
        rolls: Int,
        level: Int,
        random: Random,
    ): RollableCustomAttribute {
        val pickedTier = RandomUtil.pickLevelRestricted(tiers, rolls, level, random)
        return RollableCustomAttribute(type, pickedTier.tier, pickedTier.bounds)
    }
}