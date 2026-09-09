package de.fuballer.mcendgame.main.component.item.custom.crystal.item.synthesized

import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.IntBounds
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.item.equipment.Equipment
import de.fuballer.mcendgame.main.component.item.equipment.armor.Chestplate
import de.fuballer.mcendgame.main.component.item.equipment.armor.Leggings
import de.fuballer.mcendgame.main.component.item.equipment.tool.Bow
import de.fuballer.mcendgame.main.component.item.equipment.tool.Shield
import de.fuballer.mcendgame.main.util.random.RandomOption
import net.minecraft.network.chat.Component
import java.awt.Color

class SynthesizedPrecisionCrystal(
    settings: Properties,
) : SynthesizedCrystal(settings) {
    override val forgeColor = Color(40, 179, 232)

    override val description = Component.translatable(DESCRIPTION_BASE_KEY + "synthesized_precision")

    override val forcedAttributes = mutableMapOf<Equipment, EquipmentAttributes>()
        .fromExisting(
            RANGED_WEAPONS,
            CopyExistingData(CustomAttributeTypes.INCREASED_PROJECTILE_DAMAGE),
        ).putEquipmentAttributes(
            RANGED_WEAPONS,
            RandomOption(
                (ATTRIBUTE_DEFAULT_WEIGHT * 0.15).toInt(),
                EquipmentAttribute(
                    CustomAttributeTypes.ADDITIONAL_PROJECTILES,
                    2 to IntBounds(1),
                    1 to IntBounds(2),
                ),
            ),
        ).putEquipmentAttributes(
            Bow.entries,
            RandomOption(
                (ATTRIBUTE_DEFAULT_WEIGHT * 0.6).toInt(),
                EquipmentAttribute(
                    CustomAttributeTypes.BOW_PULL_TICKS,
                    3 to IntBounds(-2),
                    2 to IntBounds(-3),
                    1 to IntBounds(-4),
                ),
            ),
        ).fromExisting(
            MELEE_WEAPONS,
            CopyExistingData(CustomAttributeTypes.PROJECTILE_DODGE, factor = 1.0, takeFrom = Chestplate.LEATHER),
        ).fromExisting(
            Shield.entries,
            CopyExistingData(CustomAttributeTypes.PROJECTILE_DODGE, takeFrom = Chestplate.LEATHER),
            CopyExistingData(CustomAttributeTypes.INCREASED_PROJECTILE_DAMAGE, takeFrom = Bow.BOW),
        ).putEquipmentAttributes(
            MELEE_WEAPONS_WITH_SHIELDS,
            RandomOption(
                ATTRIBUTE_DEFAULT_WEIGHT,
                EquipmentAttribute(
                    CustomAttributeTypes.MORE_PROJECTILE_DAMAGE_TAKEN,
                    3 to DoubleBounds(-0.03, -0.01),
                    2 to DoubleBounds(-0.05, -0.03),
                    1 to DoubleBounds(-0.07, -0.05),
                ),
            ),
        ).fromExisting(
            ARMOR,
            CopyExistingData(CustomAttributeTypes.PROJECTILE_DODGE),
        ).fromExisting(
            HELMETS_AND_BOOTS,
            CopyExistingData(CustomAttributeTypes.INCREASED_PROJECTILE_DAMAGE, factor = 0.6, takeFrom = Bow.BOW),
        ).putEquipmentAttributes(
            HELMETS_AND_BOOTS,
            RandomOption(
                ATTRIBUTE_DEFAULT_WEIGHT,
                EquipmentAttribute(
                    CustomAttributeTypes.MORE_PROJECTILE_DAMAGE_TAKEN,
                    3 to DoubleBounds(-0.03, -0.015),
                    2 to DoubleBounds(-0.045, -0.03),
                    1 to DoubleBounds(-0.06, -0.045),
                ),
            ),
        ).fromExisting(
            CHESTPLATES_WITH_ELYTRA,
            CopyExistingData(CustomAttributeTypes.INCREASED_PROJECTILE_DAMAGE, takeFrom = Bow.BOW),
        ).putEquipmentAttributes(
            CHESTPLATES_WITH_ELYTRA,
            RandomOption(
                ATTRIBUTE_DEFAULT_WEIGHT,
                EquipmentAttribute(
                    CustomAttributeTypes.MORE_PROJECTILE_DAMAGE_TAKEN,
                    3 to DoubleBounds(-0.05, -0.03),
                    2 to DoubleBounds(-0.07, -0.05),
                    1 to DoubleBounds(-0.09, -0.07),
                ),
            ),
        ).fromExisting(
            Leggings.entries,
            CopyExistingData(CustomAttributeTypes.INCREASED_PROJECTILE_DAMAGE, factor = 0.7, takeFrom = Bow.BOW),
        ).putEquipmentAttributes(
            Leggings.entries,
            RandomOption(
                ATTRIBUTE_DEFAULT_WEIGHT,
                EquipmentAttribute(
                    CustomAttributeTypes.MORE_PROJECTILE_DAMAGE_TAKEN,
                    3 to DoubleBounds(-0.04, -0.025),
                    2 to DoubleBounds(-0.055, -0.04),
                    1 to DoubleBounds(-0.07, -0.055),
                ),
            ),
        ).toMap()
}