package de.fuballer.mcendgame.main.component.item.equipment.tool

import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.custom_attribute.types.VanillaAttributeTypes
import de.fuballer.mcendgame.main.component.item.equipment.Equipment
import de.fuballer.mcendgame.main.component.item.equipment.enchantment.EquipmentEnchantment
import de.fuballer.mcendgame.main.util.random.LevelRestrictedRandomOption
import de.fuballer.mcendgame.main.util.random.RandomOption
import net.minecraft.component.type.AttributeModifierSlot
import net.minecraft.item.Item
import net.minecraft.item.Items

enum class Shovel(
    override val item: Item,
) : Equipment {
    WOODEN(
        Items.WOODEN_SHOVEL,
    ),
    GOLDEN(
        Items.GOLDEN_SHOVEL,
    ),
    STONE(
        Items.STONE_SHOVEL,
    ),
    IRON(
        Items.IRON_SHOVEL,
    ),
    DIAMOND(
        Items.DIAMOND_SHOVEL,
    ),
    NETHERITE(
        Items.NETHERITE_SHOVEL,
    );

    override val slot = AttributeModifierSlot.MAINHAND

    override val rollableCustomAttributes = listOf(
        RandomOption(
            weight = 1,
            listOf(
                LevelRestrictedRandomOption(weight = 50, tier = 1, requiredLevel = 0, RollableCustomAttribute(VanillaAttributeTypes.ATTACK_DAMAGE, 3, DoubleBounds(0.6, 1.4))),
                LevelRestrictedRandomOption(weight = 10, tier = 2, requiredLevel = 5, RollableCustomAttribute(VanillaAttributeTypes.ATTACK_DAMAGE, 2, DoubleBounds(1.4, 2.2))),
                LevelRestrictedRandomOption(weight = 1, tier = 3, requiredLevel = 10, RollableCustomAttribute(VanillaAttributeTypes.ATTACK_DAMAGE, 1, DoubleBounds(2.2, 3.0))),
            )
        ),
        RandomOption(
            weight = 1,
            listOf(
                LevelRestrictedRandomOption(weight = 50, tier = 1, requiredLevel = 0, RollableCustomAttribute(VanillaAttributeTypes.INCREASED_ATTACK_DAMAGE, 3, DoubleBounds(0.03, 0.06))),
                LevelRestrictedRandomOption(weight = 10, tier = 2, requiredLevel = 5, RollableCustomAttribute(VanillaAttributeTypes.INCREASED_ATTACK_DAMAGE, 2, DoubleBounds(0.06, 0.09))),
                LevelRestrictedRandomOption(weight = 1, tier = 3, requiredLevel = 10, RollableCustomAttribute(VanillaAttributeTypes.INCREASED_ATTACK_DAMAGE, 1, DoubleBounds(0.09, 0.12))),
            )
        ),
        RandomOption(
            weight = 1,
            listOf(
                LevelRestrictedRandomOption(weight = 50, tier = 1, requiredLevel = 0, RollableCustomAttribute(VanillaAttributeTypes.INCREASED_ATTACK_SPEED, 3, DoubleBounds(0.05, 0.1))),
                LevelRestrictedRandomOption(weight = 10, tier = 2, requiredLevel = 5, RollableCustomAttribute(VanillaAttributeTypes.INCREASED_ATTACK_SPEED, 2, DoubleBounds(0.1, 0.15))),
                LevelRestrictedRandomOption(weight = 1, tier = 3, requiredLevel = 10, RollableCustomAttribute(VanillaAttributeTypes.INCREASED_ATTACK_SPEED, 1, DoubleBounds(0.15, 0.2))),
            )
        ),
        RandomOption(
            weight = 1,
            listOf(
                LevelRestrictedRandomOption(weight = 50, tier = 1, requiredLevel = 0, RollableCustomAttribute(CustomAttributeTypes.ELEMENTAL_DAMAGE, 3, DoubleBounds(1.0, 2.5))),
                LevelRestrictedRandomOption(weight = 10, tier = 2, requiredLevel = 5, RollableCustomAttribute(CustomAttributeTypes.ELEMENTAL_DAMAGE, 2, DoubleBounds(2.5, 4.0))),
                LevelRestrictedRandomOption(weight = 1, tier = 3, requiredLevel = 10, RollableCustomAttribute(CustomAttributeTypes.ELEMENTAL_DAMAGE, 1, DoubleBounds(4.0, 5.0))),
            )
        ),
        RandomOption(
            weight = 1,
            listOf(
                LevelRestrictedRandomOption(weight = 50, tier = 1, requiredLevel = 0, RollableCustomAttribute(CustomAttributeTypes.INCREASED_ELEMENTAL_DAMAGE, 3, DoubleBounds(0.05, 0.1))),
                LevelRestrictedRandomOption(weight = 10, tier = 2, requiredLevel = 5, RollableCustomAttribute(CustomAttributeTypes.INCREASED_ELEMENTAL_DAMAGE, 2, DoubleBounds(0.1, 0.15))),
                LevelRestrictedRandomOption(weight = 1, tier = 3, requiredLevel = 10, RollableCustomAttribute(CustomAttributeTypes.INCREASED_ELEMENTAL_DAMAGE, 1, DoubleBounds(0.15, 0.2))),
            )
        ),
        RandomOption(
            weight = 1,
            listOf(
                LevelRestrictedRandomOption(weight = 50, tier = 1, requiredLevel = 0, RollableCustomAttribute(CustomAttributeTypes.INCREASED_DAMAGE, 3, DoubleBounds(0.015, 0.03))),
                LevelRestrictedRandomOption(weight = 10, tier = 2, requiredLevel = 5, RollableCustomAttribute(CustomAttributeTypes.INCREASED_DAMAGE, 2, DoubleBounds(0.03, 0.045))),
                LevelRestrictedRandomOption(weight = 1, tier = 3, requiredLevel = 10, RollableCustomAttribute(CustomAttributeTypes.INCREASED_DAMAGE, 1, DoubleBounds(0.045, 0.06))),
            )
        ),
    )

    override val rollableEnchants = listOf(
        RandomOption(10, EquipmentEnchantment.MENDING),
        RandomOption(20, EquipmentEnchantment.UNBREAKING_1),
        RandomOption(15, EquipmentEnchantment.UNBREAKING_2),
        RandomOption(10, EquipmentEnchantment.UNBREAKING_3),
        RandomOption(0, EquipmentEnchantment.CURSE_OF_VANISHING),
        RandomOption(25, EquipmentEnchantment.EFFICIENCY_1),
        RandomOption(20, EquipmentEnchantment.EFFICIENCY_2),
        RandomOption(15, EquipmentEnchantment.EFFICIENCY_3),
        RandomOption(10, EquipmentEnchantment.EFFICIENCY_4),
        RandomOption(5, EquipmentEnchantment.EFFICIENCY_5),
        RandomOption(10, EquipmentEnchantment.FORTUNE_1),
        RandomOption(6, EquipmentEnchantment.FORTUNE_2),
        RandomOption(3, EquipmentEnchantment.FORTUNE_3),
        RandomOption(15, EquipmentEnchantment.SILK_TOUCH),
        RandomOption(25, EquipmentEnchantment.SHARPNESS_1),
        RandomOption(20, EquipmentEnchantment.SHARPNESS_2),
        RandomOption(15, EquipmentEnchantment.SHARPNESS_3),
        RandomOption(10, EquipmentEnchantment.SHARPNESS_4),
        RandomOption(5, EquipmentEnchantment.SHARPNESS_5),
        RandomOption(15, EquipmentEnchantment.SMITE_1),
        RandomOption(12, EquipmentEnchantment.SMITE_2),
        RandomOption(9, EquipmentEnchantment.SMITE_3),
        RandomOption(6, EquipmentEnchantment.SMITE_4),
        RandomOption(3, EquipmentEnchantment.SMITE_5),
        RandomOption(15, EquipmentEnchantment.BANE_OF_ARTHROPODS_1),
        RandomOption(12, EquipmentEnchantment.BANE_OF_ARTHROPODS_2),
        RandomOption(9, EquipmentEnchantment.BANE_OF_ARTHROPODS_3),
        RandomOption(6, EquipmentEnchantment.BANE_OF_ARTHROPODS_4),
        RandomOption(3, EquipmentEnchantment.BANE_OF_ARTHROPODS_5),
    )
}