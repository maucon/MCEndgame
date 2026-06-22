package de.fuballer.mcendgame.main.component.dungeon.enemy.equipment.attributes

import de.fuballer.mcendgame.main.util.random.LevelRestrictedRandomOption
import de.fuballer.mcendgame.main.util.random.RandomUtil
import kotlin.random.Random

object EnemyEquipmentAttributesSettings {
    fun getAttributeCount(level: Int, random: Random): Int {
        val rolls = 1 + level / 2 + if (random.nextDouble() < (level / 2.0) % 1) 1 else 0
        return RandomUtil.pickLevelRestricted(ATTRIBUTE_COUNT, rolls, level, random)
    }

    private val ATTRIBUTE_COUNT = listOf(
        LevelRestrictedRandomOption(weight = 10000, tier = 0, requiredLevel = 0, option = 0),
        LevelRestrictedRandomOption(weight = 10000, tier = 1, requiredLevel = 0, option = 1),
        LevelRestrictedRandomOption(weight = 2400, tier = 2, requiredLevel = 3, option = 2),
        LevelRestrictedRandomOption(weight = 350, tier = 3, requiredLevel = 5, option = 3),
        LevelRestrictedRandomOption(weight = 50, tier = 4, requiredLevel = 10, option = 4),
        LevelRestrictedRandomOption(weight = 1, tier = 5, requiredLevel = 15, option = 5),
    )

    fun getAttributeTierRolls(level: Int, random: Random) = 1 + level / 5 + if (random.nextDouble() < (level / 5.0) % 1) 1 else 0
}