package de.fuballer.mcendgame.main.component.entity

import de.fuballer.mcendgame.main.component.custom_attribute.data.AttributeType
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.custom_attribute.types.VanillaAttributeTypes

enum class EnemyEquipmentClass(
    vararg includes: EnemyEquipmentClass,
    private val attributeWeightFactors: Map<AttributeType, Double> = mapOf(),
) {
    NO_ARMOR,
    NO_WEAPONS,
    NO_EQUIPMENT(
        NO_ARMOR,
        NO_WEAPONS,
    ),
    NO_DODGE(
        attributeWeightFactors = mapOf(
            CustomAttributeTypes.DODGE to 0.0,
            CustomAttributeTypes.PROJECTILE_DODGE to 0.0,
        )
    ),
    RANGED,
    MELEE(
        NO_DODGE,
    ),
    NO_ATTACK_DAMAGE(
        attributeWeightFactors = mapOf(
            VanillaAttributeTypes.ATTACK_DAMAGE to 0.0,
            VanillaAttributeTypes.INCREASED_ATTACK_DAMAGE to 0.0,
        )
    ),
    NO_SPELL_DAMAGE(
        attributeWeightFactors = mapOf(
            CustomAttributeTypes.SPELL_DAMAGE to 0.0,
            CustomAttributeTypes.INCREASED_SPELL_DAMAGE to 0.0,
        )
    ),
    RANGED_SPELL_DAMAGE(
        RANGED,
        NO_ATTACK_DAMAGE,
    ),
    MELEE_ATTACK_DAMAGE(
        MELEE,
        NO_SPELL_DAMAGE,
    ),
    RANGED_ATTACK_DAMAGE(
        RANGED,
        NO_SPELL_DAMAGE,
    );

    private val includes = includes.toSet()

    fun isOrIncludes(equipmentClass: EnemyEquipmentClass): Boolean = this == equipmentClass || includes.any { it.isOrIncludes(equipmentClass) }

    fun isNot(equipmentClass: EnemyEquipmentClass): Boolean = !isOrIncludes(equipmentClass)

    private val allAttributeWeightFactors: Map<AttributeType, Double> by lazy {
        buildMap {
            includes.forEach { putAll(it.allAttributeWeightFactors) }
            putAll(attributeWeightFactors)
        }
    }

    fun getAttributeWeightFactors() = allAttributeWeightFactors
}