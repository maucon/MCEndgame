package de.fuballer.mcendgame.main.component.item.custom.crystal.item.synthesized

import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.custom_attribute.types.VanillaAttributeTypes
import de.fuballer.mcendgame.main.component.item.equipment.Equipment
import de.fuballer.mcendgame.main.component.item.equipment.armor.Leggings
import de.fuballer.mcendgame.main.component.item.equipment.tool.Sword
import net.minecraft.network.chat.Component
import java.awt.Color

class SynthesizedForceCrystal(
    settings: Properties
) : SynthesizedCrystal(settings) {
    override val forgeColor = Color(40, 179, 232)

    override val description = Component.translatable(DESCRIPTION_BASE_KEY + "synthesized_force")

    override val forcedAttributes = mutableMapOf<Equipment, EquipmentAttributes>()
        .fromExisting(
            WEAPONS_WITH_SHIELD,
            CopyExistingData(VanillaAttributeTypes.ATTACK_DAMAGE),
            CopyExistingData(VanillaAttributeTypes.INCREASED_ATTACK_DAMAGE),
            CopyExistingData(CustomAttributeTypes.INCREASED_DAMAGE),
        ).fromExisting(
            HELMETS_AND_BOOTS,
            CopyExistingData(VanillaAttributeTypes.ATTACK_DAMAGE, factor = 0.6, takeFrom = Sword.WOODEN),
            CopyExistingData(VanillaAttributeTypes.INCREASED_ATTACK_DAMAGE, factor = 0.6, takeFrom = Sword.WOODEN),
            CopyExistingData(CustomAttributeTypes.INCREASED_DAMAGE, factor = 0.6, takeFrom = Sword.WOODEN),
        ).fromExisting(
            CHESTPLATES_WITH_ELYTRA,
            CopyExistingData(VanillaAttributeTypes.ATTACK_DAMAGE, takeFrom = Sword.WOODEN),
            CopyExistingData(VanillaAttributeTypes.INCREASED_ATTACK_DAMAGE, takeFrom = Sword.WOODEN),
            CopyExistingData(CustomAttributeTypes.INCREASED_DAMAGE, takeFrom = Sword.WOODEN),
        ).fromExisting(
            Leggings.entries,
            CopyExistingData(VanillaAttributeTypes.ATTACK_DAMAGE, factor = 0.7, takeFrom = Sword.WOODEN),
            CopyExistingData(VanillaAttributeTypes.INCREASED_ATTACK_DAMAGE, factor = 0.7, takeFrom = Sword.WOODEN),
            CopyExistingData(CustomAttributeTypes.INCREASED_DAMAGE, factor = 0.7, takeFrom = Sword.WOODEN),
        ).toMap()
}