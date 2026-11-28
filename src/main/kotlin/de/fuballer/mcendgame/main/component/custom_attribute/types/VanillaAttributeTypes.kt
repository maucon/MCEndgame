package de.fuballer.mcendgame.main.component.custom_attribute.types

import de.fuballer.mcendgame.main.component.custom_attribute.affinity.AttributeAffinities
import de.fuballer.mcendgame.main.component.custom_attribute.affinity.AttributeAffinity
import de.fuballer.mcendgame.main.component.custom_attribute.data.AttributeFormat
import de.fuballer.mcendgame.main.component.custom_attribute.data.ValueFormats.PERCENT
import de.fuballer.mcendgame.main.component.custom_attribute.data.ValueFormats.SIGNED_DOUBLE
import de.fuballer.mcendgame.main.component.custom_attribute.data.ValueFormats.SIGNED_PERCENT
import de.fuballer.mcendgame.main.component.custom_attribute.data.VanillaAttributeType
import de.fuballer.mcendgame.main.component.custom_attribute.sign_based_keyword.SignBasedKeywords
import net.minecraft.core.Holder
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation
import net.minecraft.world.entity.ai.attributes.Attributes
import kotlin.reflect.KVisibility
import kotlin.reflect.full.memberProperties

object VanillaAttributeTypes {
    // DEFENSE
    val ARMOR = flat(Attributes.ARMOR, "armor")
    val ARMOR_TOUGHNESS = flat(Attributes.ARMOR_TOUGHNESS, "armor_toughness")
    val MAX_HEALTH = flat(Attributes.MAX_HEALTH, "max_health")
    val INCREASED_MAX_HEALTH = increased(Attributes.MAX_HEALTH, "increased_max_health")
    val MORE_MAX_HEALTH = more(Attributes.MAX_HEALTH, "more_max_health")

    // OFFENSE
    val ATTACK_DAMAGE = flat(Attributes.ATTACK_DAMAGE, "attack_damage")
    val INCREASED_ATTACK_DAMAGE = increased(Attributes.ATTACK_DAMAGE, "increased_attack_damage")
    val MORE_ATTACK_DAMAGE = more(Attributes.ATTACK_DAMAGE, "more_attack_damage")
    val INCREASED_ATTACK_SPEED = increased(Attributes.ATTACK_SPEED, "increased_attack_speed")
    val MORE_ATTACK_SPEED = more(Attributes.ATTACK_SPEED, "more_attack_speed")

    // MISC
    val INCREASED_MOVEMENT_SPEED = increased(Attributes.MOVEMENT_SPEED, "increased_movement_speed")
    val MORE_MOVEMENT_SPEED = more(Attributes.MOVEMENT_SPEED, "more_movement_speed")
    val INCREASED_JUMP_STRENGTH = increased(Attributes.JUMP_STRENGTH, "increased_jump_strength")
    val LUCK = flat(Attributes.LUCK, "luck")
    val INCREASED_SCALE = increased(Attributes.SCALE, "increased_scale", AttributeAffinities.NEUTRAL)
    val MORE_SCALE = more(Attributes.SCALE, "more_scale", AttributeAffinities.NEUTRAL)
    val INCREASED_ENTITY_INTERACTION_RANGE = increased(Attributes.ENTITY_INTERACTION_RANGE, "increased_entity_interaction_range")

    /** Adds the rolled value directly to the attribute (e.g. +2 armor). */
    private fun flat(
        attribute: Holder<Attribute>,
        key: String,
        affinities: List<AttributeAffinity> = AttributeAffinities.BENEFICIAL,
    ) = VanillaAttributeType(attribute, Operation.ADD_VALUE, key, AttributeFormat.of(SIGNED_DOUBLE), affinities)

    /** Adds a percentage of the base value; stacks additively with other "increased" modifiers. */
    private fun increased(
        attribute: Holder<Attribute>,
        key: String,
        affinities: List<AttributeAffinity> = AttributeAffinities.BENEFICIAL,
    ) = VanillaAttributeType(attribute, Operation.ADD_MULTIPLIED_BASE, key, AttributeFormat.of(SIGNED_PERCENT), affinities, SignBasedKeywords.INCREASED)

    /** Multiplies the total value by a percentage; stacks multiplicatively with other "more" modifiers. */
    private fun more(
        attribute: Holder<Attribute>,
        key: String,
        affinities: List<AttributeAffinity> = AttributeAffinities.BENEFICIAL,
    ) = VanillaAttributeType(attribute, Operation.ADD_MULTIPLIED_TOTAL, key, AttributeFormat.of(PERCENT), affinities, SignBasedKeywords.MORE)

    // region get by key
    fun getByKey(key: String): VanillaAttributeType =
        attributeTypes[key] ?: error("Unknown vanilla attribute type: $key")

    private val attributeTypes = VanillaAttributeTypes::class.memberProperties
        .filter { it.visibility == KVisibility.PUBLIC }
        .map { it(VanillaAttributeTypes) as VanillaAttributeType }
        .associateBy { it.key }
    // endregion
}