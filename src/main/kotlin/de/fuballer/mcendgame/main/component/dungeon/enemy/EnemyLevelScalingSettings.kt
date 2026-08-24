package de.fuballer.mcendgame.main.component.dungeon.enemy

import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.effects.SpellResistanceSettings
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.custom_attribute.types.VanillaAttributeTypes
import kotlin.math.min

object EnemyLevelScalingSettings {
    fun getEnemyLevelAttributes(level: Int) = mutableListOf(
        CustomAttribute(VanillaAttributeTypes.ATTACK_DAMAGE, roll = DoubleRoll(DoubleBounds(2.0 * level))),
        CustomAttribute(CustomAttributeTypes.SPELL_DAMAGE, roll = DoubleRoll(DoubleBounds(1.5 * level))),
    ).also {
        if (level >= 5) it.add(
            CustomAttribute(
                CustomAttributeTypes.SPELL_RESISTANCE,
                roll = DoubleRoll(DoubleBounds(min(SpellResistanceSettings.LIMIT, 0.04 * (level - 4))))
            )
        )
    }

    fun getBossLevelAttributes(level: Int) = mutableListOf(
        CustomAttribute(VanillaAttributeTypes.MORE_MAX_HEALTH, roll = DoubleRoll(DoubleBounds(0.1 * level))),
        CustomAttribute(VanillaAttributeTypes.ARMOR, roll = DoubleRoll(DoubleBounds(15 + 0.5 * level))),
        CustomAttribute(VanillaAttributeTypes.ARMOR_TOUGHNESS, roll = DoubleRoll(DoubleBounds(0.5 * level))),
    )
}