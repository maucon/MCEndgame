package de.fuballer.mcendgame.main.component.dungeon.generation.encounter.encounters.scarred_one

import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.IntBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.data.StringBounds
import de.fuballer.mcendgame.main.component.custom_attribute.effects.companion.wolf_companion.WolfCompanionType
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.custom_attribute.types.VanillaAttributeTypes
import de.fuballer.mcendgame.main.component.dungeon.generation.encounter.encounters.scarred_one.data.ScarredOneEffect
import de.fuballer.mcendgame.main.util.random.RandomOption
import de.fuballer.mcendgame.main.util.random.RandomUtil
import kotlin.random.Random

object ScarredOneEncounterSettings {
    fun getSpawnProbability(dungeonLevel: Int) = if (dungeonLevel >= 4) 0.2 else 0.0 // per dungeon

    fun getBaseEffectCount(dungeonLevel: Int) = (dungeonLevel / 4).coerceIn(1, 3)

    fun getPositiveEffects(count: Int = 1) = RandomUtil.pickRepeatIfNeeded(POSITIVE, Random.Default, count).map { it.roll() }
    fun getNegativeEffects(count: Int = 1) = RandomUtil.pickRepeatIfNeeded(NEGATIVE, Random.Default, count).map { it.roll() }

    val POSITIVE = listOf(
        RandomOption(5, ScarredOneEffect(RollableCustomAttribute(VanillaAttributeTypes.MAX_HEALTH, 0, DoubleBounds(4.0, 6.0)), ScarredOneEffectTargetGroup.ALLIES)),
        RandomOption(5, ScarredOneEffect(RollableCustomAttribute(VanillaAttributeTypes.INCREASED_ATTACK_SPEED, 0, DoubleBounds(0.15, 0.25)), ScarredOneEffectTargetGroup.ALLIES)),
        RandomOption(8, ScarredOneEffect(RollableCustomAttribute(VanillaAttributeTypes.INCREASED_MOVEMENT_SPEED, 0, DoubleBounds(0.15, 0.25)), ScarredOneEffectTargetGroup.ALLIES)),
        RandomOption(
            5,
            ScarredOneEffect(RollableCustomAttribute(VanillaAttributeTypes.INCREASED_ENTITY_INTERACTION_RANGE, 0, DoubleBounds(0.1, 0.15)), ScarredOneEffectTargetGroup.ALLIES)
        ),
        RandomOption(5, ScarredOneEffect(RollableCustomAttribute(CustomAttributeTypes.DODGE, 0, DoubleBounds(0.15, 0.25)), ScarredOneEffectTargetGroup.ALLIES)),
        RandomOption(5, ScarredOneEffect(RollableCustomAttribute(CustomAttributeTypes.PROJECTILE_DODGE, 0, DoubleBounds(0.2, 0.35)), ScarredOneEffectTargetGroup.ALLIES)),
        RandomOption(7, ScarredOneEffect(RollableCustomAttribute(CustomAttributeTypes.MORE_DAMAGE_TAKEN, 0, DoubleBounds(-0.15, -0.1)), ScarredOneEffectTargetGroup.ALLIES)),
        RandomOption(7, ScarredOneEffect(RollableCustomAttribute(CustomAttributeTypes.MORE_DAMAGE, 0, DoubleBounds(0.2, 0.3)), ScarredOneEffectTargetGroup.ALLIES)),
        RandomOption(4, ScarredOneEffect(RollableCustomAttribute(CustomAttributeTypes.ADDITIONAL_ARROWS, 0, IntBounds(1, 2)), ScarredOneEffectTargetGroup.ALLIES)),
        RandomOption(4, ScarredOneEffect(RollableCustomAttribute(CustomAttributeTypes.BOW_PULL_TICKS, 0, IntBounds(-5, -3)), ScarredOneEffectTargetGroup.ALLIES)),
        RandomOption(
            5,
            ScarredOneEffect(RollableCustomAttribute(CustomAttributeTypes.INCREASED_DAMAGE_AGAINST_FULL_HEALTH, 0, DoubleBounds(0.4, 0.6)), ScarredOneEffectTargetGroup.ALLIES)
        ),
        RandomOption(5, ScarredOneEffect(RollableCustomAttribute(CustomAttributeTypes.MAGIC_FIND, 0, IntBounds(15, 25)), ScarredOneEffectTargetGroup.ALLIES)),
        RandomOption(5, ScarredOneEffect(RollableCustomAttribute(CustomAttributeTypes.MORE_ATTACK_KNOCKBACK, 0, DoubleBounds(0.2, 0.3)), ScarredOneEffectTargetGroup.ALLIES)),
        RandomOption(5, ScarredOneEffect(RollableCustomAttribute(CustomAttributeTypes.INCREASED_HEALTH_RECOVERY, 0, DoubleBounds(0.2, 0.3)), ScarredOneEffectTargetGroup.ALLIES)),
        RandomOption(5, ScarredOneEffect(RollableCustomAttribute(CustomAttributeTypes.FURY_ON_KILL, 0), ScarredOneEffectTargetGroup.ALLIES)),
        RandomOption(5, ScarredOneEffect(RollableCustomAttribute(CustomAttributeTypes.RESILIENCE_ON_DAMAGE_TAKEN, 0), ScarredOneEffectTargetGroup.ALLIES)),
        RandomOption(5, ScarredOneEffect(RollableCustomAttribute(CustomAttributeTypes.WOLF_COMPANION, 0, StringBounds(WolfCompanionType.getNames())), ScarredOneEffectTargetGroup.ALLIES)),
        RandomOption(4, ScarredOneEffect(RollableCustomAttribute(CustomAttributeTypes.MORE_HORN_EFFECT_DURATION, 0, DoubleBounds(0.15, 0.25)), ScarredOneEffectTargetGroup.ALLIES)),
        RandomOption(4, ScarredOneEffect(RollableCustomAttribute(CustomAttributeTypes.MORE_HORN_COOLDOWN, 0, DoubleBounds(-0.25, -0.15)), ScarredOneEffectTargetGroup.ALLIES)),
        RandomOption(
            5,
            ScarredOneEffect(
                RollableCustomAttribute(CustomAttributeTypes.INCREASED_MOVEMENT_SPEED_ON_KILL, 0, DoubleBounds(0.2, 0.4), IntBounds(3, 3)),
                ScarredOneEffectTargetGroup.ALLIES
            )
        ),
        RandomOption(4, ScarredOneEffect(RollableCustomAttribute(CustomAttributeTypes.STEALTH, 0), ScarredOneEffectTargetGroup.ALLIES)),
        RandomOption(5, ScarredOneEffect(RollableCustomAttribute(CustomAttributeTypes.SLOWNESS_ON_HIT, 0, IntBounds(1), IntBounds(2, 4)), ScarredOneEffectTargetGroup.ALLIES)),

        RandomOption(4, ScarredOneEffect(RollableCustomAttribute(CustomAttributeTypes.MORE_DAMAGE_TAKEN, 0, DoubleBounds(0.1, 0.15)), ScarredOneEffectTargetGroup.ENEMIES)),
        RandomOption(4, ScarredOneEffect(RollableCustomAttribute(VanillaAttributeTypes.INCREASED_MOVEMENT_SPEED, 0, DoubleBounds(-0.15, -0.1)), ScarredOneEffectTargetGroup.ENEMIES)),
        RandomOption(4, ScarredOneEffect(RollableCustomAttribute(CustomAttributeTypes.MORE_DAMAGE, 0, DoubleBounds(-0.15, -0.1)), ScarredOneEffectTargetGroup.ENEMIES)),

        RandomOption(4, ScarredOneEffect(RollableCustomAttribute(CustomAttributeTypes.MORE_DAMAGE_TAKEN, 0, DoubleBounds(0.1, 0.15)), ScarredOneEffectTargetGroup.BOSSES)),
        RandomOption(4, ScarredOneEffect(RollableCustomAttribute(CustomAttributeTypes.MORE_DAMAGE, 0, DoubleBounds(-0.2, -0.1)), ScarredOneEffectTargetGroup.BOSSES)),

        RandomOption(50, ScarredOneEffect(RollableCustomAttribute(CustomAttributeTypes.DROP_INCREASED_LOOT, 0, DoubleBounds(0.2, 0.4)), ScarredOneEffectTargetGroup.NON_BOSS_ENEMIES)),
        RandomOption(25, ScarredOneEffect(RollableCustomAttribute(CustomAttributeTypes.DROP_INCREASED_LOOT, 0, DoubleBounds(0.3, 0.5)), ScarredOneEffectTargetGroup.BOSSES)),
    )

    val NEGATIVE = listOf(
        RandomOption(7, ScarredOneEffect(RollableCustomAttribute(CustomAttributeTypes.MORE_DAMAGE_TAKEN, 0, DoubleBounds(-0.1, -0.05)), ScarredOneEffectTargetGroup.ENEMIES)),
        RandomOption(5, ScarredOneEffect(RollableCustomAttribute(VanillaAttributeTypes.MORE_SCALE, 0, DoubleBounds(-0.3, -0.2)), ScarredOneEffectTargetGroup.NON_BOSS_ENEMIES)),
        RandomOption(6, ScarredOneEffect(RollableCustomAttribute(VanillaAttributeTypes.INCREASED_MOVEMENT_SPEED, 0, DoubleBounds(0.05, 0.15)), ScarredOneEffectTargetGroup.ENEMIES)),
        RandomOption(4, ScarredOneEffect(RollableCustomAttribute(CustomAttributeTypes.DODGE, 0, DoubleBounds(0.05, 0.15)), ScarredOneEffectTargetGroup.ENEMIES)),
        RandomOption(7, ScarredOneEffect(RollableCustomAttribute(CustomAttributeTypes.MORE_DAMAGE, 0, DoubleBounds(0.1, 0.15)), ScarredOneEffectTargetGroup.ENEMIES)),
        RandomOption(5, ScarredOneEffect(RollableCustomAttribute(CustomAttributeTypes.ADDITIONAL_ARROWS, 0, IntBounds(1, 2)), ScarredOneEffectTargetGroup.ENEMIES)),
        RandomOption(5, ScarredOneEffect(RollableCustomAttribute(CustomAttributeTypes.MORE_ATTACK_KNOCKBACK, 0, DoubleBounds(0.15, 0.25)), ScarredOneEffectTargetGroup.ENEMIES)),
        RandomOption(5, ScarredOneEffect(RollableCustomAttribute(CustomAttributeTypes.SLOWNESS_ON_HIT, 0, IntBounds(1), IntBounds(1, 2)), ScarredOneEffectTargetGroup.ENEMIES)),

        RandomOption(6, ScarredOneEffect(RollableCustomAttribute(CustomAttributeTypes.MORE_DAMAGE_TAKEN, 0, DoubleBounds(-0.15, -0.05)), ScarredOneEffectTargetGroup.BOSSES)),
        RandomOption(5, ScarredOneEffect(RollableCustomAttribute(CustomAttributeTypes.DODGE, 0, DoubleBounds(0.1, 0.2)), ScarredOneEffectTargetGroup.BOSSES)),
        RandomOption(6, ScarredOneEffect(RollableCustomAttribute(CustomAttributeTypes.MORE_DAMAGE, 0, DoubleBounds(0.1, 0.15)), ScarredOneEffectTargetGroup.BOSSES)),
        RandomOption(4, ScarredOneEffect(RollableCustomAttribute(VanillaAttributeTypes.MORE_SCALE, 0, DoubleBounds(0.1, 0.2)), ScarredOneEffectTargetGroup.BOSSES)),

        RandomOption(3, ScarredOneEffect(RollableCustomAttribute(VanillaAttributeTypes.INCREASED_ATTACK_SPEED, 0, DoubleBounds(-0.1, -0.05)), ScarredOneEffectTargetGroup.ALLIES)),
        RandomOption(3, ScarredOneEffect(RollableCustomAttribute(VanillaAttributeTypes.INCREASED_MOVEMENT_SPEED, 0, DoubleBounds(-0.15, -0.05)), ScarredOneEffectTargetGroup.ALLIES)),
        RandomOption(
            3,
            ScarredOneEffect(RollableCustomAttribute(VanillaAttributeTypes.INCREASED_ENTITY_INTERACTION_RANGE, 0, DoubleBounds(-0.1, -0.05)), ScarredOneEffectTargetGroup.ALLIES)
        ),
        RandomOption(3, ScarredOneEffect(RollableCustomAttribute(CustomAttributeTypes.MORE_DAMAGE_TAKEN, 0, DoubleBounds(0.05, 0.1)), ScarredOneEffectTargetGroup.ALLIES)),
        RandomOption(3, ScarredOneEffect(RollableCustomAttribute(CustomAttributeTypes.MORE_DAMAGE, 0, DoubleBounds(-0.15, -0.1)), ScarredOneEffectTargetGroup.ALLIES)),
        RandomOption(2, ScarredOneEffect(RollableCustomAttribute(CustomAttributeTypes.BOW_PULL_TICKS, 0, IntBounds(2, 3)), ScarredOneEffectTargetGroup.ALLIES)),
        RandomOption(2, ScarredOneEffect(RollableCustomAttribute(CustomAttributeTypes.MORE_ATTACK_KNOCKBACK, 0, DoubleBounds(-0.15, -0.1)), ScarredOneEffectTargetGroup.ALLIES)),
        RandomOption(2, ScarredOneEffect(RollableCustomAttribute(CustomAttributeTypes.MORE_HORN_EFFECT_DURATION, 0, DoubleBounds(-0.15, -0.1)), ScarredOneEffectTargetGroup.ALLIES)),
        RandomOption(2, ScarredOneEffect(RollableCustomAttribute(CustomAttributeTypes.MORE_HORN_COOLDOWN, 0, DoubleBounds(0.1, 0.15)), ScarredOneEffectTargetGroup.ALLIES)),
        RandomOption(3, ScarredOneEffect(RollableCustomAttribute(CustomAttributeTypes.INCREASED_HEALTH_RECOVERY, 0, DoubleBounds(-0.15, -0.1)), ScarredOneEffectTargetGroup.ALLIES)),
    )
}