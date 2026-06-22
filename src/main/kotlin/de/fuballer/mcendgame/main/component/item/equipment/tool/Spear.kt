package de.fuballer.mcendgame.main.component.item.equipment.tool

import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.custom_attribute.types.VanillaAttributeTypes
import de.fuballer.mcendgame.main.component.item.equipment.Equipment
import de.fuballer.mcendgame.main.component.item.equipment.data.AttributeTierData
import de.fuballer.mcendgame.main.component.item.equipment.data.TieredRollableCustomAttribute
import de.fuballer.mcendgame.main.component.item.equipment.enchantment.EquipmentEnchantment
import de.fuballer.mcendgame.main.util.random.LevelRestrictedRandomOption
import de.fuballer.mcendgame.main.util.random.RandomOption
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items

enum class Spear(
    override val item: Item,
) : Equipment {
    WOODEN(
        Items.WOODEN_SPEAR,
    ),
    GOLDEN(
        Items.GOLDEN_SPEAR,
    ),
    STONE(
        Items.STONE_SPEAR,
    ),
    COPPER(
        Items.COPPER_SPEAR,
    ),
    IRON(
        Items.IRON_SPEAR,
    ),
    DIAMOND(
        Items.DIAMOND_SPEAR,
    ),
    NETHERITE(
        Items.NETHERITE_SPEAR,
    );

    override val slot = EquipmentSlotGroup.MAINHAND

    override val rollableCustomAttributes = listOf(
        RandomOption(
            weight = 1000,
            TieredRollableCustomAttribute(
                VanillaAttributeTypes.ATTACK_DAMAGE,
                listOf(
                    LevelRestrictedRandomOption(weight = 50, tier = 1, requiredLevel = 0, AttributeTierData(3, DoubleBounds(0.6, 1.4))),
                    LevelRestrictedRandomOption(weight = 10, tier = 2, requiredLevel = 5, AttributeTierData(2, DoubleBounds(1.4, 2.2))),
                    LevelRestrictedRandomOption(weight = 1, tier = 3, requiredLevel = 10, AttributeTierData(1, DoubleBounds(2.2, 3.0))),
                )
            )
        ),
        RandomOption(
            weight = 1000,
            TieredRollableCustomAttribute(
                VanillaAttributeTypes.INCREASED_ATTACK_DAMAGE,
                listOf(
                    LevelRestrictedRandomOption(weight = 50, tier = 1, requiredLevel = 0, AttributeTierData(3, DoubleBounds(0.03, 0.06))),
                    LevelRestrictedRandomOption(weight = 10, tier = 2, requiredLevel = 5, AttributeTierData(2, DoubleBounds(0.06, 0.09))),
                    LevelRestrictedRandomOption(weight = 1, tier = 3, requiredLevel = 10, AttributeTierData(1, DoubleBounds(0.09, 0.12))),
                )
            )
        ),
        RandomOption(
            weight = 1000,
            TieredRollableCustomAttribute(
                VanillaAttributeTypes.INCREASED_ATTACK_SPEED,
                listOf(
                    LevelRestrictedRandomOption(weight = 50, tier = 1, requiredLevel = 0, AttributeTierData(3, DoubleBounds(0.05, 0.1))),
                    LevelRestrictedRandomOption(weight = 10, tier = 2, requiredLevel = 5, AttributeTierData(2, DoubleBounds(0.1, 0.15))),
                    LevelRestrictedRandomOption(weight = 1, tier = 3, requiredLevel = 10, AttributeTierData(1, DoubleBounds(0.15, 0.2))),
                )
            )
        ),
        RandomOption(
            weight = 1000,
            TieredRollableCustomAttribute(
                CustomAttributeTypes.SPELL_DAMAGE,
                listOf(
                    LevelRestrictedRandomOption(weight = 50, tier = 1, requiredLevel = 0, AttributeTierData(3, DoubleBounds(0.6, 1.4))),
                    LevelRestrictedRandomOption(weight = 10, tier = 2, requiredLevel = 5, AttributeTierData(2, DoubleBounds(1.4, 2.2))),
                    LevelRestrictedRandomOption(weight = 1, tier = 3, requiredLevel = 10, AttributeTierData(1, DoubleBounds(2.2, 3.0))),
                )
            )
        ),
        RandomOption(
            weight = 1000,
            TieredRollableCustomAttribute(
                CustomAttributeTypes.INCREASED_SPELL_DAMAGE,
                listOf(
                    LevelRestrictedRandomOption(weight = 50, tier = 1, requiredLevel = 0, AttributeTierData(3, DoubleBounds(0.05, 0.1))),
                    LevelRestrictedRandomOption(weight = 10, tier = 2, requiredLevel = 5, AttributeTierData(2, DoubleBounds(0.1, 0.15))),
                    LevelRestrictedRandomOption(weight = 1, tier = 3, requiredLevel = 10, AttributeTierData(1, DoubleBounds(0.15, 0.2))),
                )
            )
        ),
        RandomOption(
            weight = 1000,
            TieredRollableCustomAttribute(
                CustomAttributeTypes.INCREASED_DAMAGE,
                listOf(
                    LevelRestrictedRandomOption(weight = 50, tier = 1, requiredLevel = 0, AttributeTierData(3, DoubleBounds(0.015, 0.03))),
                    LevelRestrictedRandomOption(weight = 10, tier = 2, requiredLevel = 5, AttributeTierData(2, DoubleBounds(0.03, 0.045))),
                    LevelRestrictedRandomOption(weight = 1, tier = 3, requiredLevel = 10, AttributeTierData(1, DoubleBounds(0.045, 0.06))),
                )
            )
        ),
    )

    override val rollableEnchants = listOf(
        RandomOption(10, EquipmentEnchantment.MENDING),
        RandomOption(20, EquipmentEnchantment.UNBREAKING_1),
        RandomOption(15, EquipmentEnchantment.UNBREAKING_2),
        RandomOption(10, EquipmentEnchantment.UNBREAKING_3),
        RandomOption(0, EquipmentEnchantment.CURSE_OF_VANISHING),
        RandomOption(15, EquipmentEnchantment.BANE_OF_ARTHROPODS_1),
        RandomOption(12, EquipmentEnchantment.BANE_OF_ARTHROPODS_2),
        RandomOption(9, EquipmentEnchantment.BANE_OF_ARTHROPODS_3),
        RandomOption(6, EquipmentEnchantment.BANE_OF_ARTHROPODS_4),
        RandomOption(3, EquipmentEnchantment.BANE_OF_ARTHROPODS_5),
        RandomOption(10, EquipmentEnchantment.FIRE_ASPECT_1),
        RandomOption(5, EquipmentEnchantment.FIRE_ASPECT_2),
        RandomOption(15, EquipmentEnchantment.LOOTING_1),
        RandomOption(10, EquipmentEnchantment.LOOTING_2),
        RandomOption(5, EquipmentEnchantment.LOOTING_3),
        RandomOption(10, EquipmentEnchantment.KNOCKBACK_1),
        RandomOption(5, EquipmentEnchantment.KNOCKBACK_2),
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
        RandomOption(15, EquipmentEnchantment.SWEEPING_EDGE_1),
        RandomOption(10, EquipmentEnchantment.SWEEPING_EDGE_2),
        RandomOption(5, EquipmentEnchantment.SWEEPING_EDGE_3),
        RandomOption(15, EquipmentEnchantment.LUNGE_1),
        RandomOption(10, EquipmentEnchantment.LUNGE_2),
        RandomOption(5, EquipmentEnchantment.LUNGE_3),
    )
}