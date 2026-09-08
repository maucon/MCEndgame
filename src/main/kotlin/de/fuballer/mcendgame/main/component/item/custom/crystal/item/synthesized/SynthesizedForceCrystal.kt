package de.fuballer.mcendgame.main.component.item.custom.crystal.item.synthesized

import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.types.VanillaAttributeTypes
import de.fuballer.mcendgame.main.component.item.equipment.Equipment
import de.fuballer.mcendgame.main.component.item.equipment.tool.Sword
import de.fuballer.mcendgame.main.util.random.RandomOption
import net.minecraft.network.chat.Component
import java.awt.Color

class SynthesizedForceCrystal(
    settings: Properties
) : SynthesizedCrystal(settings) {
    override val forgeColor = Color(40, 179, 232)

    override val description = Component.translatable(DESCRIPTION_BASE_KEY + "synthesized_force")

    override val forcedAttributes = mutableMapOf<Equipment, EquipmentAttributes>()
        .apply {
            putAll(Sword.entries.associateWith {
                EquipmentAttributes(
                    RandomOption(
                        weight = 1,
                        EquipmentAttribute(
                            VanillaAttributeTypes.ATTACK_DAMAGE,
                            3 to DoubleBounds(0.6, 1.4),
                            2 to DoubleBounds(1.4, 2.2),
                            1 to DoubleBounds(2.2, 3.0),
                        ),
                    ),
                    RandomOption(
                        weight = 1,
                        EquipmentAttribute(
                            VanillaAttributeTypes.INCREASED_ATTACK_DAMAGE,
                            3 to DoubleBounds(0.03, 0.06),
                            2 to DoubleBounds(0.06, 0.09),
                            1 to DoubleBounds(0.09, 0.12),
                        ),
                    ),
                )
            })
        }.toMap()
}