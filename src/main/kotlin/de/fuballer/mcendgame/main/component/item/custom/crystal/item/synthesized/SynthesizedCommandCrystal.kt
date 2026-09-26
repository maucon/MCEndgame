package de.fuballer.mcendgame.main.component.item.custom.crystal.item.synthesized

import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.item.equipment.Equipment
import de.fuballer.mcendgame.main.component.item.equipment.armor.Leggings
import de.fuballer.mcendgame.main.util.random.RandomOption
import net.minecraft.network.chat.Component
import java.awt.Color

class SynthesizedCommandCrystal(
    settings: Properties,
) : SynthesizedCrystal(settings) {
    override val forgeColor = Color(40, 179, 232)

    override val description = Component.translatable(DESCRIPTION_BASE_KEY + "synthesized_command")

    override val forcedAttributes = mutableMapOf<Equipment, EquipmentAttributes>()
        .putEquipmentAttributes(
            WEAPONS_WITH_SHIELD,
            RandomOption(
                ATTRIBUTE_DEFAULT_WEIGHT,
                EquipmentAttribute(
                    CustomAttributeTypes.COMPANION_ATTACK_DAMAGE,
                    3 to DoubleBounds(0.4, 1.1),
                    2 to DoubleBounds(1.1, 1.8),
                    1 to DoubleBounds(1.8, 2.5),
                ),
            ),
            RandomOption(
                ATTRIBUTE_DEFAULT_WEIGHT,
                EquipmentAttribute(
                    CustomAttributeTypes.INCREASED_COMPANION_DAMAGE,
                    3 to DoubleBounds(0.05, 0.1),
                    2 to DoubleBounds(0.1, 0.15),
                    1 to DoubleBounds(0.15, 0.2),
                ),
            ),
        ).putEquipmentAttributes(
            HELMETS_AND_BOOTS,
            RandomOption(
                ATTRIBUTE_DEFAULT_WEIGHT,
                EquipmentAttribute(
                    CustomAttributeTypes.COMPANION_ATTACK_DAMAGE,
                    3 to DoubleBounds(0.2, 0.6),
                    2 to DoubleBounds(0.6, 1.0),
                    1 to DoubleBounds(1.0, 1.4),
                ),
            ),
            RandomOption(
                ATTRIBUTE_DEFAULT_WEIGHT,
                EquipmentAttribute(
                    CustomAttributeTypes.INCREASED_COMPANION_DAMAGE,
                    3 to DoubleBounds(0.01, 0.04),
                    2 to DoubleBounds(0.04, 0.07),
                    1 to DoubleBounds(0.07, 0.1),
                ),
            ),
        ).putEquipmentAttributes(
            CHESTPLATES_WITH_ELYTRA,
            RandomOption(
                ATTRIBUTE_DEFAULT_WEIGHT,
                EquipmentAttribute(
                    CustomAttributeTypes.COMPANION_ATTACK_DAMAGE,
                    3 to DoubleBounds(0.5, 1.0),
                    2 to DoubleBounds(1.0, 1.5),
                    1 to DoubleBounds(1.5, 2.0),
                ),
            ),
            RandomOption(
                ATTRIBUTE_DEFAULT_WEIGHT,
                EquipmentAttribute(
                    CustomAttributeTypes.INCREASED_COMPANION_DAMAGE,
                    3 to DoubleBounds(0.03, 0.07),
                    2 to DoubleBounds(0.07, 0.11),
                    1 to DoubleBounds(0.11, 0.15),
                ),
            ),
        ).putEquipmentAttributes(
            Leggings.entries,
            RandomOption(
                ATTRIBUTE_DEFAULT_WEIGHT,
                EquipmentAttribute(
                    CustomAttributeTypes.COMPANION_ATTACK_DAMAGE,
                    3 to DoubleBounds(0.5, 0.9),
                    2 to DoubleBounds(0.9, 1.3),
                    1 to DoubleBounds(1.3, 1.7),
                ),
            ),
            RandomOption(
                ATTRIBUTE_DEFAULT_WEIGHT,
                EquipmentAttribute(
                    CustomAttributeTypes.INCREASED_COMPANION_DAMAGE,
                    3 to DoubleBounds(0.03, 0.06),
                    2 to DoubleBounds(0.06, 0.09),
                    1 to DoubleBounds(0.09, 0.12),
                ),
            ),
        ).toMap()
}