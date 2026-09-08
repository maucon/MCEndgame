package de.fuballer.mcendgame.main.component.item.custom.crystal.item.synthesized

import de.fuballer.mcendgame.main.component.custom_attribute.types.VanillaAttributeTypes
import de.fuballer.mcendgame.main.component.item.equipment.Equipment
import de.fuballer.mcendgame.main.component.item.equipment.tool.*
import net.minecraft.network.chat.Component
import java.awt.Color

class SynthesizedForceCrystal(
    settings: Properties
) : SynthesizedCrystal(settings) {
    override val forgeColor = Color(40, 179, 232)

    override val description = Component.translatable(DESCRIPTION_BASE_KEY + "synthesized_force")

    override val forcedAttributes = mutableMapOf<Equipment, EquipmentAttributes>()
        .fromExisting(
            Sword.entries,
            CopyExistingData(VanillaAttributeTypes.ATTACK_DAMAGE),
            CopyExistingData(VanillaAttributeTypes.INCREASED_ATTACK_DAMAGE),
        ).fromExisting(
            Axe.entries,
            CopyExistingData(VanillaAttributeTypes.ATTACK_DAMAGE),
            CopyExistingData(VanillaAttributeTypes.INCREASED_ATTACK_DAMAGE),
        ).fromExisting(
            Pickaxe.entries,
            CopyExistingData(VanillaAttributeTypes.ATTACK_DAMAGE),
            CopyExistingData(VanillaAttributeTypes.INCREASED_ATTACK_DAMAGE),
        ).fromExisting(
            Shovel.entries,
            CopyExistingData(VanillaAttributeTypes.ATTACK_DAMAGE),
            CopyExistingData(VanillaAttributeTypes.INCREASED_ATTACK_DAMAGE),
        ).fromExisting(
            Hoe.entries,
            CopyExistingData(VanillaAttributeTypes.ATTACK_DAMAGE),
            CopyExistingData(VanillaAttributeTypes.INCREASED_ATTACK_DAMAGE),
        ).toMap()
}