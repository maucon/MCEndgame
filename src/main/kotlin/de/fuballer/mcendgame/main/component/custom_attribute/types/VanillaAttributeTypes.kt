package de.fuballer.mcendgame.main.component.custom_attribute.types

import de.fuballer.mcendgame.main.component.custom_attribute.AttributeFormats
import de.fuballer.mcendgame.main.component.custom_attribute.affinity.AttributeAffinities
import de.fuballer.mcendgame.main.component.custom_attribute.data.VanillaAttributeType
import de.fuballer.mcendgame.main.component.custom_attribute.sign_based_keyword.SignBasedKeywords
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import kotlin.reflect.KVisibility
import kotlin.reflect.full.memberProperties

object VanillaAttributeTypes {
    // DEFENSE
    val ARMOR = VanillaAttributeType(
        Attributes.ARMOR,
        AttributeModifier.Operation.ADD_VALUE,
        "armor",
        AttributeFormats.SIGNED_DOUBLE_ROLL,
        AttributeFormats.DOUBLE_BOUNDS,
        AttributeAffinities.BENEFICIAL
    )
    val ARMOR_TOUGHNESS = VanillaAttributeType(
        Attributes.ARMOR_TOUGHNESS,
        AttributeModifier.Operation.ADD_VALUE,
        "armor_toughness",
        AttributeFormats.SIGNED_DOUBLE_ROLL,
        AttributeFormats.DOUBLE_BOUNDS,
        AttributeAffinities.BENEFICIAL
    )
    val MAX_HEALTH =
        VanillaAttributeType(
            Attributes.MAX_HEALTH,
            AttributeModifier.Operation.ADD_VALUE,
            "max_health",
            AttributeFormats.SIGNED_DOUBLE_ROLL,
            AttributeFormats.DOUBLE_BOUNDS,
            AttributeAffinities.BENEFICIAL
        )
    val INCREASED_MAX_HEALTH =
        VanillaAttributeType(
            Attributes.MAX_HEALTH,
            AttributeModifier.Operation.ADD_MULTIPLIED_BASE,
            "increased_max_health",
            AttributeFormats.SIGNED_PERCENT_ROLL,
            AttributeFormats.PERCENT_BOUNDS,
            AttributeAffinities.BENEFICIAL,
            SignBasedKeywords.INCREASED
        )
    val MORE_MAX_HEALTH =
        VanillaAttributeType(
            Attributes.MAX_HEALTH,
            AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL,
            "more_max_health",
            AttributeFormats.PERCENT_ROLL,
            AttributeFormats.PERCENT_BOUNDS,
            AttributeAffinities.BENEFICIAL,
            SignBasedKeywords.MORE
        )

    // OFFENSE
    val ATTACK_DAMAGE = VanillaAttributeType(
        Attributes.ATTACK_DAMAGE,
        AttributeModifier.Operation.ADD_VALUE,
        "attack_damage",
        AttributeFormats.SIGNED_DOUBLE_ROLL,
        AttributeFormats.DOUBLE_BOUNDS,
        AttributeAffinities.BENEFICIAL
    )
    val INCREASED_ATTACK_DAMAGE = VanillaAttributeType(
        Attributes.ATTACK_DAMAGE,
        AttributeModifier.Operation.ADD_MULTIPLIED_BASE,
        "increased_attack_damage",
        AttributeFormats.SIGNED_PERCENT_ROLL,
        AttributeFormats.PERCENT_BOUNDS,
        AttributeAffinities.BENEFICIAL,
        SignBasedKeywords.INCREASED
    )
    val INCREASED_ATTACK_SPEED = VanillaAttributeType(
        Attributes.ATTACK_SPEED,
        AttributeModifier.Operation.ADD_MULTIPLIED_BASE,
        "increased_attack_speed",
        AttributeFormats.SIGNED_PERCENT_ROLL,
        AttributeFormats.PERCENT_BOUNDS,
        AttributeAffinities.BENEFICIAL,
        SignBasedKeywords.INCREASED
    )
    val MORE_ATTACK_SPEED = VanillaAttributeType(
        Attributes.ATTACK_SPEED,
        AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL,
        "more_attack_speed",
        AttributeFormats.PERCENT_ROLL,
        AttributeFormats.PERCENT_BOUNDS,
        AttributeAffinities.BENEFICIAL,
        SignBasedKeywords.MORE
    )

    // MISC
    val INCREASED_MOVEMENT_SPEED = VanillaAttributeType(
        Attributes.MOVEMENT_SPEED,
        AttributeModifier.Operation.ADD_MULTIPLIED_BASE,
        "increased_movement_speed",
        AttributeFormats.SIGNED_PERCENT_ROLL,
        AttributeFormats.PERCENT_BOUNDS,
        AttributeAffinities.BENEFICIAL,
        SignBasedKeywords.INCREASED
    )
    val MORE_MOVEMENT_SPEED = VanillaAttributeType(
        Attributes.MOVEMENT_SPEED,
        AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL,
        "more_movement_speed",
        AttributeFormats.PERCENT_ROLL,
        AttributeFormats.PERCENT_BOUNDS,
        AttributeAffinities.BENEFICIAL,
        SignBasedKeywords.MORE
    )
    val INCREASED_JUMP_STRENGTH = VanillaAttributeType(
        Attributes.JUMP_STRENGTH,
        AttributeModifier.Operation.ADD_MULTIPLIED_BASE,
        "increased_jump_strength",
        AttributeFormats.SIGNED_PERCENT_ROLL,
        AttributeFormats.PERCENT_BOUNDS,
        AttributeAffinities.BENEFICIAL,
        SignBasedKeywords.INCREASED
    )
    val LUCK = VanillaAttributeType(
        Attributes.LUCK,
        AttributeModifier.Operation.ADD_VALUE,
        "luck",
        AttributeFormats.SIGNED_DOUBLE_ROLL,
        AttributeFormats.DOUBLE_BOUNDS,
        AttributeAffinities.BENEFICIAL
    )
    val INCREASED_SCALE = VanillaAttributeType(
        Attributes.SCALE,
        AttributeModifier.Operation.ADD_MULTIPLIED_BASE,
        "increased_scale",
        AttributeFormats.SIGNED_PERCENT_ROLL,
        AttributeFormats.PERCENT_BOUNDS,
        AttributeAffinities.NEUTRAL,
        SignBasedKeywords.INCREASED
    )
    val MORE_SCALE = VanillaAttributeType(
        Attributes.SCALE,
        AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL,
        "more_scale",
        AttributeFormats.PERCENT_ROLL,
        AttributeFormats.PERCENT_BOUNDS,
        AttributeAffinities.NEUTRAL,
        SignBasedKeywords.MORE
    )
    val INCREASED_ENTITY_INTERACTION_RANGE = VanillaAttributeType(
        Attributes.ENTITY_INTERACTION_RANGE,
        AttributeModifier.Operation.ADD_MULTIPLIED_BASE,
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