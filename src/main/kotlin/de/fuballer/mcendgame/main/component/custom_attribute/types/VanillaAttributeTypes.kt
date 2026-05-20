package de.fuballer.mcendgame.main.component.custom_attribute.types

import de.fuballer.mcendgame.main.component.custom_attribute.AttributeFormats
import de.fuballer.mcendgame.main.component.custom_attribute.affinity.AttributeAffinities
import de.fuballer.mcendgame.main.component.custom_attribute.data.VanillaAttributeType
import de.fuballer.mcendgame.main.component.custom_attribute.sign_based_keyword.SignBasedKeywords
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import kotlin.reflect.KVisibility
import kotlin.reflect.full.memberProperties

object VanillaAttributeTypes {
    // DEFENSE
    val ARMOR = VanillaAttributeType(
        EntityAttributes.ARMOR,
        EntityAttributeModifier.Operation.ADD_VALUE,
        "armor",
        AttributeFormats.SIGNED_DOUBLE_ROLL,
        AttributeFormats.DOUBLE_BOUNDS,
        AttributeAffinities.BENEFICIAL
    )
    val ARMOR_TOUGHNESS = VanillaAttributeType(
        EntityAttributes.ARMOR_TOUGHNESS,
        EntityAttributeModifier.Operation.ADD_VALUE,
        "armor_toughness",
        AttributeFormats.SIGNED_DOUBLE_ROLL,
        AttributeFormats.DOUBLE_BOUNDS,
        AttributeAffinities.BENEFICIAL
    )
    val MAX_HEALTH =
        VanillaAttributeType(
            EntityAttributes.MAX_HEALTH,
            EntityAttributeModifier.Operation.ADD_VALUE,
            "max_health",
            AttributeFormats.SIGNED_DOUBLE_ROLL,
            AttributeFormats.DOUBLE_BOUNDS,
            AttributeAffinities.BENEFICIAL
        )
    val INCREASED_MAX_HEALTH =
        VanillaAttributeType(
            EntityAttributes.MAX_HEALTH,
            EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE,
            "increased_max_health",
            AttributeFormats.SIGNED_PERCENT_ROLL,
            AttributeFormats.PERCENT_BOUNDS,
            AttributeAffinities.BENEFICIAL,
            SignBasedKeywords.INCREASED
        )
    val MORE_MAX_HEALTH =
        VanillaAttributeType(
            EntityAttributes.MAX_HEALTH,
            EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL,
            "more_max_health",
            AttributeFormats.PERCENT_ROLL,
            AttributeFormats.PERCENT_BOUNDS,
            AttributeAffinities.BENEFICIAL,
            SignBasedKeywords.MORE
        )

    // OFFENSE
    val ATTACK_DAMAGE = VanillaAttributeType(
        EntityAttributes.ATTACK_DAMAGE,
        EntityAttributeModifier.Operation.ADD_VALUE,
        "attack_damage",
        AttributeFormats.SIGNED_DOUBLE_ROLL,
        AttributeFormats.DOUBLE_BOUNDS,
        AttributeAffinities.BENEFICIAL
    )
    val INCREASED_ATTACK_DAMAGE = VanillaAttributeType(
        EntityAttributes.ATTACK_DAMAGE,
        EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE,
        "increased_attack_damage",
        AttributeFormats.SIGNED_PERCENT_ROLL,
        AttributeFormats.PERCENT_BOUNDS,
        AttributeAffinities.BENEFICIAL,
        SignBasedKeywords.INCREASED
    )
    val INCREASED_ATTACK_SPEED = VanillaAttributeType(
        EntityAttributes.ATTACK_SPEED,
        EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE,
        "increased_attack_speed",
        AttributeFormats.SIGNED_PERCENT_ROLL,
        AttributeFormats.PERCENT_BOUNDS,
        AttributeAffinities.BENEFICIAL,
        SignBasedKeywords.INCREASED
    )
    val MORE_ATTACK_SPEED = VanillaAttributeType(
        EntityAttributes.ATTACK_SPEED,
        EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL,
        "more_attack_speed",
        AttributeFormats.PERCENT_ROLL,
        AttributeFormats.PERCENT_BOUNDS,
        AttributeAffinities.BENEFICIAL,
        SignBasedKeywords.MORE
    )

    // MISC
    val INCREASED_MOVEMENT_SPEED = VanillaAttributeType(
        EntityAttributes.MOVEMENT_SPEED,
        EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE,
        "increased_movement_speed",
        AttributeFormats.SIGNED_PERCENT_ROLL,
        AttributeFormats.PERCENT_BOUNDS,
        AttributeAffinities.BENEFICIAL,
        SignBasedKeywords.INCREASED
    )
    val MORE_MOVEMENT_SPEED = VanillaAttributeType(
        EntityAttributes.MOVEMENT_SPEED,
        EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL,
        "more_movement_speed",
        AttributeFormats.PERCENT_ROLL,
        AttributeFormats.PERCENT_BOUNDS,
        AttributeAffinities.BENEFICIAL,
        SignBasedKeywords.MORE
    )
    val INCREASED_JUMP_STRENGTH = VanillaAttributeType(
        EntityAttributes.JUMP_STRENGTH,
        EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE,
        "increased_jump_strength",
        AttributeFormats.SIGNED_PERCENT_ROLL,
        AttributeFormats.PERCENT_BOUNDS,
        AttributeAffinities.BENEFICIAL,
        SignBasedKeywords.INCREASED
    )
    val LUCK = VanillaAttributeType(
        EntityAttributes.LUCK,
        EntityAttributeModifier.Operation.ADD_VALUE,
        "luck",
        AttributeFormats.SIGNED_DOUBLE_ROLL,
        AttributeFormats.DOUBLE_BOUNDS,
        AttributeAffinities.BENEFICIAL
    )
    val INCREASED_SCALE = VanillaAttributeType(
        EntityAttributes.SCALE,
        EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE,
        "increased_scale",
        AttributeFormats.SIGNED_PERCENT_ROLL,
        AttributeFormats.PERCENT_BOUNDS,
        AttributeAffinities.NEUTRAL,
        SignBasedKeywords.INCREASED
    )
    val MORE_SCALE = VanillaAttributeType(
        EntityAttributes.SCALE,
        EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL,
        "more_scale",
        AttributeFormats.PERCENT_ROLL,
        AttributeFormats.PERCENT_BOUNDS,
        AttributeAffinities.NEUTRAL,
        SignBasedKeywords.MORE
    )
    val INCREASED_ENTITY_INTERACTION_RANGE = VanillaAttributeType(
        EntityAttributes.ENTITY_INTERACTION_RANGE,
        EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE,
        "increased_entity_interaction_range",
        AttributeFormats.SIGNED_PERCENT_ROLL,
        AttributeFormats.PERCENT_BOUNDS,
        AttributeAffinities.BENEFICIAL,
        SignBasedKeywords.INCREASED
    )

    // region get by key
    fun getByKey(key: String): VanillaAttributeType {
        return attributeTypes[key]!!
    }

    private val attributeTypes = VanillaAttributeTypes::class.memberProperties
        .filter { it.visibility == KVisibility.PUBLIC }
        .map { it(VanillaAttributeTypes) as VanillaAttributeType }
        .associateBy { it.key }
    // endregion
}