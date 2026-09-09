package de.fuballer.mcendgame.main.component.item.custom.crystal.item.synthesized

import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.IntBounds
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.item.equipment.Equipment
import de.fuballer.mcendgame.main.component.item.equipment.armor.Leggings
import de.fuballer.mcendgame.main.component.item.equipment.tool.*
import de.fuballer.mcendgame.main.util.random.RandomOption
import net.minecraft.network.chat.Component
import java.awt.Color

class SynthesizedVitalityCrystal(
    settings: Properties,
) : SynthesizedCrystal(settings) {
    override val forgeColor = Color(40, 179, 232)

    override val description = Component.translatable(DESCRIPTION_BASE_KEY + "synthesized_vitality")

    override val forcedAttributes = mutableMapOf<Equipment, EquipmentAttributes>()
        .putEquipmentAttributes(
            WEAPONS,
            RandomOption(
                ATTRIBUTE_DEFAULT_WEIGHT,
                EquipmentAttribute(
                    CustomAttributeTypes.HEAL_ON_KILL,
                    3 to DoubleBounds(1.0, 1.6),
                    2 to DoubleBounds(1.6, 2.2),
                    1 to DoubleBounds(2.2, 2.8),
                ),
            ),
        )
        .putEquipmentAttributes(
            Sword.entries, // 1.6 AttackSpeed
            RandomOption(
                (ATTRIBUTE_DEFAULT_WEIGHT * 0.6).toInt(),
                EquipmentAttribute.fromBoundsLists(
                    CustomAttributeTypes.HEAL_NEARBY_ALLIES_ON_MELEE_HIT,
                    3 to listOf(IntBounds(8, 8), DoubleBounds(0.2, 0.3)),
                    2 to listOf(IntBounds(8, 8), DoubleBounds(0.3, 0.4)),
                    1 to listOf(IntBounds(8, 8), DoubleBounds(0.4, 0.5)),
                ),
            ),
        ).putEquipmentAttributes(
            listOf(
                Axe.entries, // 1.0 AttackSpeed
                Shovel.entries, // 1.0 AttackSpeed
                Spear.entries, // ~ 1.0 AttackSpeed (wooden 1.54 -> netherite 0.87) (+ charge behaviour)
                listOf(Miscellaneous.TRIDENT), // 1.1 AttackSpeed
            ),
            RandomOption(
                (ATTRIBUTE_DEFAULT_WEIGHT * 0.6).toInt(),
                EquipmentAttribute.fromBoundsLists(
                    CustomAttributeTypes.HEAL_NEARBY_ALLIES_ON_MELEE_HIT,
                    3 to listOf(IntBounds(8, 8), DoubleBounds(0.35, 0.5)),
                    2 to listOf(IntBounds(8, 8), DoubleBounds(0.5, 0.65)),
                    1 to listOf(IntBounds(8, 8), DoubleBounds(0.65, 0.8)),
                ),
            ),
        ).putEquipmentAttributes(
            Pickaxe.entries, // 1.2 AttackSpeed
            RandomOption(
                (ATTRIBUTE_DEFAULT_WEIGHT * 0.6).toInt(),
                EquipmentAttribute.fromBoundsLists(
                    CustomAttributeTypes.HEAL_NEARBY_ALLIES_ON_MELEE_HIT,
                    3 to listOf(IntBounds(8, 8), DoubleBounds(0.26, 0.39)),
                    2 to listOf(IntBounds(8, 8), DoubleBounds(0.39, 0.52)),
                    1 to listOf(IntBounds(8, 8), DoubleBounds(0.52, 0.65)),
                ),
            ),
        ).putEquipmentAttributes(
            Hoe.entries, // 4.0 AttackSpeed
            RandomOption(
                (ATTRIBUTE_DEFAULT_WEIGHT * 0.6).toInt(),
                EquipmentAttribute.fromBoundsLists(
                    CustomAttributeTypes.HEAL_NEARBY_ALLIES_ON_MELEE_HIT,
                    3 to listOf(IntBounds(8, 8), DoubleBounds(0.05, 0.1)),
                    2 to listOf(IntBounds(8, 8), DoubleBounds(0.1, 0.15)),
                    1 to listOf(IntBounds(8, 8), DoubleBounds(0.15, 0.2)),
                ),
            ),
        ).putEquipmentAttributes(
            Mace.entries, // 0.6 AttackSpeed
            RandomOption(
                (ATTRIBUTE_DEFAULT_WEIGHT * 0.6).toInt(),
                EquipmentAttribute.fromBoundsLists(
                    CustomAttributeTypes.HEAL_NEARBY_ALLIES_ON_MELEE_HIT,
                    3 to listOf(IntBounds(8, 8), DoubleBounds(0.55, 0.8)),
                    2 to listOf(IntBounds(8, 8), DoubleBounds(0.8, 1.05)),
                    1 to listOf(IntBounds(8, 8), DoubleBounds(1.05, 1.3)),
                ),
            ),
        ).putEquipmentAttributes(
            HELMETS_AND_BOOTS.toMutableList().apply { add(Shield.entries) },
            RandomOption(
                ATTRIBUTE_DEFAULT_WEIGHT,
                EquipmentAttribute(
                    CustomAttributeTypes.INCREASED_HEALTH_RECOVERY,
                    3 to DoubleBounds(0.025, 0.05),
                    2 to DoubleBounds(0.05, 0.075),
                    1 to DoubleBounds(0.075, 0.1),
                ),
            ),
            RandomOption(
                (ATTRIBUTE_DEFAULT_WEIGHT * 0.5).toInt(),
                EquipmentAttribute(
                    CustomAttributeTypes.RESILIENCE_ON_DAMAGE_TAKEN_CHANCE,
                    3 to DoubleBounds(0.07, 0.13),
                    2 to DoubleBounds(0.13, 0.19),
                    1 to DoubleBounds(0.19, 0.25),
                ),
            ),
            RandomOption(
                ATTRIBUTE_DEFAULT_WEIGHT,
                EquipmentAttribute(
                    CustomAttributeTypes.INCREASED_HEALING,
                    3 to DoubleBounds(0.02, 0.04),
                    2 to DoubleBounds(0.04, 0.06),
                    1 to DoubleBounds(0.06, 0.08),
                ),
            ),
        ).putEquipmentAttributes(
            CHESTPLATES_WITH_ELYTRA,
            RandomOption(
                ATTRIBUTE_DEFAULT_WEIGHT,
                EquipmentAttribute(
                    CustomAttributeTypes.INCREASED_HEALTH_RECOVERY,
                    3 to DoubleBounds(0.06, 0.09),
                    2 to DoubleBounds(0.09, 0.12),
                    1 to DoubleBounds(0.12, 0.15),
                ),
            ),
            RandomOption(
                (ATTRIBUTE_DEFAULT_WEIGHT * 0.5).toInt(),
                EquipmentAttribute(
                    CustomAttributeTypes.RESILIENCE_ON_DAMAGE_TAKEN_CHANCE,
                    3 to DoubleBounds(0.1, 0.2),
                    2 to DoubleBounds(0.2, 0.3),
                    1 to DoubleBounds(0.3, 0.4),
                ),
            ),
            RandomOption(
                ATTRIBUTE_DEFAULT_WEIGHT,
                EquipmentAttribute(
                    CustomAttributeTypes.INCREASED_HEALING,
                    3 to DoubleBounds(0.03, 0.06),
                    2 to DoubleBounds(0.06, 0.09),
                    1 to DoubleBounds(0.09, 0.12),
                ),
            ),
        ).putEquipmentAttributes(
            Leggings.entries,
            RandomOption(
                ATTRIBUTE_DEFAULT_WEIGHT,
                EquipmentAttribute(
                    CustomAttributeTypes.INCREASED_HEALTH_RECOVERY,
                    3 to DoubleBounds(0.04, 0.07),
                    2 to DoubleBounds(0.07, 0.1),
                    1 to DoubleBounds(0.1, 0.13),
                ),
            ),
            RandomOption(
                (ATTRIBUTE_DEFAULT_WEIGHT * 0.5).toInt(),
                EquipmentAttribute(
                    CustomAttributeTypes.RESILIENCE_ON_DAMAGE_TAKEN_CHANCE,
                    3 to DoubleBounds(0.08, 0.16),
                    2 to DoubleBounds(0.16, 0.24),
                    1 to DoubleBounds(0.24, 0.32),
                ),
            ),
            RandomOption(
                ATTRIBUTE_DEFAULT_WEIGHT,
                EquipmentAttribute(
                    CustomAttributeTypes.INCREASED_HEALING,
                    3 to DoubleBounds(0.025, 0.05),
                    2 to DoubleBounds(0.05, 0.075),
                    1 to DoubleBounds(0.075, 0.1),
                ),
            ),
        ).toMap()
}