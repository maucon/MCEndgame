package de.fuballer.mcendgame.main.component.item.custom.crystal.item.synthesized

import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.IntBounds
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.custom_attribute.types.VanillaAttributeTypes
import de.fuballer.mcendgame.main.component.item.equipment.Equipment
import de.fuballer.mcendgame.main.component.item.equipment.tool.Miscellaneous
import de.fuballer.mcendgame.main.component.item.equipment.tool.Shield
import de.fuballer.mcendgame.main.util.random.RandomOption
import net.minecraft.network.chat.Component
import java.awt.Color

class SynthesizedEnduranceCrystal(
    settings: Properties,
) : SynthesizedCrystal(settings) {
    override val forgeColor = Color(40, 179, 232)

    override val description = Component.translatable(DESCRIPTION_BASE_KEY + "synthesized_endurance")

    override val forcedAttributes = mutableMapOf<Equipment, EquipmentAttributes>()
        .fromExisting(
            WEAPONS,
            CopyExistingData(CustomAttributeTypes.MORE_DAMAGE_TAKEN, takeFrom = Shield.SHIELD)
        ).putEquipmentAttributes(
            WEAPONS,
            RandomOption(
                (ATTRIBUTE_DEFAULT_WEIGHT * 0.5).toInt(),
                EquipmentAttribute.fromBoundsLists(
                    CustomAttributeTypes.GAIN_ENEMY_ARMOR_ON_KILL,
                    3 to listOf(DoubleBounds(0.04, 0.06), IntBounds(10, 10)),
                    2 to listOf(DoubleBounds(0.06, 0.08), IntBounds(10, 10)),
                    1 to listOf(DoubleBounds(0.08, 0.1), IntBounds(10, 10)),
                ),
            ),
        ).fromExisting(
            ARMOR.toMutableList().apply {
                add(Shield.entries)
                add(listOf(Miscellaneous.ELYTRA))
            },
            CopyExistingData(VanillaAttributeTypes.ARMOR),
            CopyExistingData(VanillaAttributeTypes.ARMOR_TOUGHNESS),
            CopyExistingData(VanillaAttributeTypes.MAX_HEALTH),
            CopyExistingData(CustomAttributeTypes.MORE_DAMAGE_TAKEN),
            CopyExistingData(CustomAttributeTypes.SPELL_RESISTANCE),
        ).toMap()
}