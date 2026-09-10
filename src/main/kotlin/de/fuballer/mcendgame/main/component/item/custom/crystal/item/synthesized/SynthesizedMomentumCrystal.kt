package de.fuballer.mcendgame.main.component.item.custom.crystal.item.synthesized

import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.IntBounds
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.custom_attribute.types.VanillaAttributeTypes
import de.fuballer.mcendgame.main.component.item.equipment.Equipment
import de.fuballer.mcendgame.main.component.item.equipment.armor.Boots
import de.fuballer.mcendgame.main.component.item.equipment.armor.Helmet
import de.fuballer.mcendgame.main.component.item.equipment.armor.Leggings
import de.fuballer.mcendgame.main.component.item.equipment.tool.Shield
import de.fuballer.mcendgame.main.component.item.equipment.tool.Sword
import de.fuballer.mcendgame.main.util.random.RandomOption
import net.minecraft.network.chat.Component
import java.awt.Color

class SynthesizedMomentumCrystal(
    settings: Properties,
) : SynthesizedCrystal(settings) {
    override val forgeColor = Color(40, 179, 232)

    override val description = Component.translatable(DESCRIPTION_BASE_KEY + "synthesized_momentum")

    override val forcedAttributes = mutableMapOf<Equipment, EquipmentAttributes>()
        .fromExisting(
            WEAPONS_WITH_SHIELD,
            CopyExistingData(
                VanillaAttributeTypes.INCREASED_MOVEMENT_SPEED,
                weight = (ATTRIBUTE_DEFAULT_WEIGHT * 0.3).toInt(),
                factor = 0.5,
                takeFrom = Boots.LEATHER,
            ),
        ).fromExisting(
            MELEE_WEAPONS,
            CopyExistingData(VanillaAttributeTypes.INCREASED_ATTACK_SPEED),
        ).fromExisting(
            Shield.entries,
            CopyExistingData(VanillaAttributeTypes.INCREASED_ATTACK_SPEED, takeFrom = Sword.WOODEN),
        ).putEquipmentAttributes(
            MELEE_WEAPONS_WITH_SHIELDS,
            RandomOption(
                (ATTRIBUTE_DEFAULT_WEIGHT * 0.2).toInt(),
                EquipmentAttribute.fromBoundsLists(
                    CustomAttributeTypes.INCREASED_MOVEMENT_SPEED_ON_KILL,
                    3 to listOf(DoubleBounds(0.04, 0.08), IntBounds(3, 3)),
                    2 to listOf(DoubleBounds(0.08, 0.12), IntBounds(3, 3)),
                    1 to listOf(DoubleBounds(0.12, 0.16), IntBounds(3, 3)),
                ),
            ),
        ).putEquipmentAttributes(
            RANGED_WEAPONS,
            RandomOption(
                (ATTRIBUTE_DEFAULT_WEIGHT * 0.3).toInt(),
                EquipmentAttribute(
                    CustomAttributeTypes.BOW_PULL_TICKS,
                    3 to IntBounds(-2),
                    2 to IntBounds(-3),
                    1 to IntBounds(-4),
                ),
            ),
        ).fromExisting(
            Helmet.entries,
            CopyExistingData(CustomAttributeTypes.DODGE),
            CopyExistingData(CustomAttributeTypes.PROJECTILE_DODGE),
            CopyExistingData(VanillaAttributeTypes.INCREASED_MOVEMENT_SPEED, factor = 0.4, takeFrom = Boots.LEATHER),
        ).putEquipmentAttributes(
            Helmet.entries,
            RandomOption(
                (ATTRIBUTE_DEFAULT_WEIGHT * 0.4).toInt(),
                EquipmentAttribute.fromBoundsLists(
                    CustomAttributeTypes.INCREASED_MOVEMENT_SPEED_AFTER_DODGING,
                    3 to listOf(DoubleBounds(0.02, 0.06), IntBounds(3, 3)),
                    2 to listOf(DoubleBounds(0.06, 0.1), IntBounds(3, 3)),
                    1 to listOf(DoubleBounds(0.1, 0.14), IntBounds(3, 3)),
                ),
            ),
        ).fromExisting(
            CHESTPLATES_WITH_ELYTRA,
            CopyExistingData(CustomAttributeTypes.DODGE),
            CopyExistingData(CustomAttributeTypes.PROJECTILE_DODGE),
            CopyExistingData(VanillaAttributeTypes.INCREASED_MOVEMENT_SPEED, factor = 0.5, takeFrom = Boots.LEATHER),
        ).putEquipmentAttributes(
            CHESTPLATES_WITH_ELYTRA,
            RandomOption(
                (ATTRIBUTE_DEFAULT_WEIGHT * 0.4).toInt(),
                EquipmentAttribute.fromBoundsLists(
                    CustomAttributeTypes.INCREASED_MOVEMENT_SPEED_AFTER_DODGING,
                    3 to listOf(DoubleBounds(0.03, 0.07), IntBounds(3, 3)),
                    2 to listOf(DoubleBounds(0.07, 0.11), IntBounds(3, 3)),
                    1 to listOf(DoubleBounds(0.11, 0.15), IntBounds(3, 3)),
                ),
            ),
        ).fromExisting(
            Leggings.entries,
            CopyExistingData(CustomAttributeTypes.DODGE),
            CopyExistingData(CustomAttributeTypes.PROJECTILE_DODGE),
            CopyExistingData(VanillaAttributeTypes.INCREASED_MOVEMENT_SPEED, factor = 0.7, takeFrom = Boots.LEATHER),
        ).putEquipmentAttributes(
            Leggings.entries,
            RandomOption(
                (ATTRIBUTE_DEFAULT_WEIGHT * 0.4).toInt(),
                EquipmentAttribute.fromBoundsLists(
                    CustomAttributeTypes.INCREASED_MOVEMENT_SPEED_AFTER_DODGING,
                    3 to listOf(DoubleBounds(0.045, 0.09), IntBounds(3, 3)),
                    2 to listOf(DoubleBounds(0.09, 0.135), IntBounds(3, 3)),
                    1 to listOf(DoubleBounds(0.135, 0.18), IntBounds(3, 3)),
                ),
            ),
        ).fromExisting(
            Boots.entries,
            CopyExistingData(CustomAttributeTypes.DODGE),
            CopyExistingData(CustomAttributeTypes.PROJECTILE_DODGE),
            CopyExistingData(VanillaAttributeTypes.INCREASED_MOVEMENT_SPEED),
        ).putEquipmentAttributes(
            Boots.entries,
            RandomOption(
                (ATTRIBUTE_DEFAULT_WEIGHT * 0.4).toInt(),
                EquipmentAttribute.fromBoundsLists(
                    CustomAttributeTypes.INCREASED_MOVEMENT_SPEED_AFTER_DODGING,
                    3 to listOf(DoubleBounds(0.05, 0.1), IntBounds(3, 3)),
                    2 to listOf(DoubleBounds(0.1, 0.15), IntBounds(3, 3)),
                    1 to listOf(DoubleBounds(0.15, 0.2), IntBounds(3, 3)),
                ),
            ),
        ).toMap()
}