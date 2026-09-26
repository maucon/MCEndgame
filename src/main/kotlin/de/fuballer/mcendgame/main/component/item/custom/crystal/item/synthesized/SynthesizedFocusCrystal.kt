package de.fuballer.mcendgame.main.component.item.custom.crystal.item.synthesized

import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.item.equipment.Equipment
import de.fuballer.mcendgame.main.component.item.equipment.armor.Leggings
import de.fuballer.mcendgame.main.component.item.equipment.tool.Shield
import de.fuballer.mcendgame.main.component.item.equipment.tool.Sword
import net.minecraft.network.chat.Component
import java.awt.Color

class SynthesizedFocusCrystal(
    settings: Properties,
) : SynthesizedCrystal(settings) {
    override val forgeColor = Color(40, 179, 232)

    override val description = Component.translatable(DESCRIPTION_BASE_KEY + "synthesized_focus")

    override val forcedAttributes = mutableMapOf<Equipment, EquipmentAttributes>()
        .fromExisting(
            WEAPONS_WITH_SHIELD,
            CopyExistingData(CustomAttributeTypes.SPELL_DAMAGE),
            CopyExistingData(CustomAttributeTypes.INCREASED_SPELL_DAMAGE),
        ).fromExisting(
            ARMOR.toMutableList().apply { add(Shield.entries) },
            CopyExistingData(CustomAttributeTypes.SPELL_RESISTANCE),
        ).fromExisting(
            HELMETS_AND_BOOTS,
            CopyExistingData(CustomAttributeTypes.SPELL_DAMAGE, weight = (ATTRIBUTE_DEFAULT_WEIGHT * 0.75).toInt(), factor = 0.6, takeFrom = Sword.WOODEN),
            CopyExistingData(CustomAttributeTypes.INCREASED_SPELL_DAMAGE, weight = (ATTRIBUTE_DEFAULT_WEIGHT * 0.75).toInt(), factor = 0.6, takeFrom = Sword.WOODEN),
        ).fromExisting(
            CHESTPLATES_WITH_ELYTRA,
            CopyExistingData(CustomAttributeTypes.SPELL_DAMAGE, weight = (ATTRIBUTE_DEFAULT_WEIGHT * 0.75).toInt(), takeFrom = Sword.WOODEN),
            CopyExistingData(CustomAttributeTypes.INCREASED_SPELL_DAMAGE, weight = (ATTRIBUTE_DEFAULT_WEIGHT * 0.75).toInt(), takeFrom = Sword.WOODEN),
        ).fromExisting(
            Leggings.entries,
            CopyExistingData(CustomAttributeTypes.SPELL_DAMAGE, weight = (ATTRIBUTE_DEFAULT_WEIGHT * 0.75).toInt(), factor = 0.7, takeFrom = Sword.WOODEN),
            CopyExistingData(CustomAttributeTypes.INCREASED_SPELL_DAMAGE, weight = (ATTRIBUTE_DEFAULT_WEIGHT * 0.75).toInt(), factor = 0.7, takeFrom = Sword.WOODEN),
        ).toMap()
}