package de.fuballer.mcendgame.main.component.item.equipment.armor

import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.custom_attribute.types.VanillaAttributeTypes
import de.fuballer.mcendgame.main.component.item.custom.armor.CustomArmorItems
import de.fuballer.mcendgame.main.component.item.equipment.Equipment
import de.fuballer.mcendgame.main.component.item.equipment.data.AttributeTierData
import de.fuballer.mcendgame.main.component.item.equipment.data.TieredRollableCustomAttribute
import de.fuballer.mcendgame.main.component.item.equipment.enchantment.EquipmentEnchantment
import de.fuballer.mcendgame.main.util.random.LevelRestrictedRandomOption
import de.fuballer.mcendgame.main.util.random.RandomOption
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items

enum class Boots(
    override val item: Item,
) : Equipment {
    LEATHER(
        Items.LEATHER_BOOTS,
    ),
    COPPER(
        Items.COPPER_BOOTS,
    ),
    GOLDEN(
        Items.GOLDEN_BOOTS,
    ),
    CHAINMAIL(
        Items.CHAINMAIL_BOOTS,
    ),
    IRON(
        Items.IRON_BOOTS,
    ),
    DIAMOND(
        Items.DIAMOND_BOOTS,
    ),
    NETHERITE(
        Items.NETHERITE_BOOTS,
    ),
    DRUIDS_BOOTS(
        CustomArmorItems.DRUIDS_BOOTS,
    ),
    WITHER_ROSE_BOOTS(
        CustomArmorItems.WITHER_ROSE_BOOTS,
    ),
    SUEDE_BOOTS(
        CustomArmorItems.SUEDE_BOOTS,
    ),
    MOONSHADOW(
        CustomArmorItems.MOONSHADOW,
    ),
    GEISTERGALOSCHEN(
        CustomArmorItems.GEISTERGALOSCHEN,
    ),
    EMBERREIGN(
        CustomArmorItems.EMBERREIGN,
    );

    override val slot = EquipmentSlotGroup.FEET

    override val rollableCustomAttributes = listOf(
        RandomOption(
            weight = 1000,
            TieredRollableCustomAttribute(
                VanillaAttributeTypes.ARMOR,
                listOf(
                    LevelRestrictedRandomOption(weight = 50, tier = 1, requiredLevel = 0, AttributeTierData(3, DoubleBounds(0.3, 0.7))),
                    LevelRestrictedRandomOption(weight = 10, tier = 2, requiredLevel = 5, AttributeTierData(2, DoubleBounds(0.7, 1.1))),
                    LevelRestrictedRandomOption(weight = 1, tier = 3, requiredLevel = 10, AttributeTierData(1, DoubleBounds(1.1, 1.5))),
                )
            )
        ),
        RandomOption(
            weight = 1000,
            TieredRollableCustomAttribute(
                VanillaAttributeTypes.ARMOR_TOUGHNESS,
                listOf(
                    LevelRestrictedRandomOption(weight = 50, tier = 1, requiredLevel = 0, AttributeTierData(3, DoubleBounds(0.5, 1.0))),
                    LevelRestrictedRandomOption(weight = 10, tier = 2, requiredLevel = 5, AttributeTierData(2, DoubleBounds(1.0, 1.5))),
                    LevelRestrictedRandomOption(weight = 1, tier = 3, requiredLevel = 10, AttributeTierData(1, DoubleBounds(1.5, 2.0))),
                )
            )
        ),
        RandomOption(
            weight = 1000,
            TieredRollableCustomAttribute(
                VanillaAttributeTypes.MAX_HEALTH,
                listOf(
                    LevelRestrictedRandomOption(weight = 50, tier = 1, requiredLevel = 0, AttributeTierData(3, DoubleBounds(0.5, 1.0))),
                    LevelRestrictedRandomOption(weight = 10, tier = 2, requiredLevel = 5, AttributeTierData(2, DoubleBounds(1.0, 1.5))),
                    LevelRestrictedRandomOption(weight = 1, tier = 3, requiredLevel = 10, AttributeTierData(1, DoubleBounds(1.5, 2.0))),
                )
            )
        ),
        RandomOption(
            weight = 1000,
            TieredRollableCustomAttribute(
                CustomAttributeTypes.SPELL_RESISTANCE,
                listOf(
                    LevelRestrictedRandomOption(weight = 50, tier = 1, requiredLevel = 0, AttributeTierData(3, DoubleBounds(0.04, 0.08))),
                    LevelRestrictedRandomOption(weight = 10, tier = 2, requiredLevel = 5, AttributeTierData(2, DoubleBounds(0.08, 0.12))),
                    LevelRestrictedRandomOption(weight = 1, tier = 3, requiredLevel = 10, AttributeTierData(1, DoubleBounds(0.12, 0.14))),
                )
            )
        ),
        RandomOption(
            weight = 1000,
            TieredRollableCustomAttribute(
                CustomAttributeTypes.DODGE,
                listOf(
                    LevelRestrictedRandomOption(weight = 50, tier = 1, requiredLevel = 0, AttributeTierData(3, DoubleBounds(0.01, 0.03))),
                    LevelRestrictedRandomOption(weight = 10, tier = 2, requiredLevel = 5, AttributeTierData(2, DoubleBounds(0.03, 0.05))),
                    LevelRestrictedRandomOption(weight = 1, tier = 3, requiredLevel = 10, AttributeTierData(1, DoubleBounds(0.05, 0.06))),
                )
            )
        ),
        RandomOption(
            weight = 1000,
            TieredRollableCustomAttribute(
                CustomAttributeTypes.PROJECTILE_DODGE,
                listOf(
                    LevelRestrictedRandomOption(weight = 50, tier = 1, requiredLevel = 0, AttributeTierData(3, DoubleBounds(0.02, 0.04))),
                    LevelRestrictedRandomOption(weight = 10, tier = 2, requiredLevel = 5, AttributeTierData(2, DoubleBounds(0.04, 0.06))),
                    LevelRestrictedRandomOption(weight = 1, tier = 3, requiredLevel = 10, AttributeTierData(1, DoubleBounds(0.06, 0.08))),
                )
            )
        ),
        RandomOption(
            weight = 1000,
            TieredRollableCustomAttribute(
                CustomAttributeTypes.MORE_DAMAGE_TAKEN,
                listOf(
                    LevelRestrictedRandomOption(weight = 50, tier = 1, requiredLevel = 0, AttributeTierData(3, DoubleBounds(-0.025, -0.01))),
                    LevelRestrictedRandomOption(weight = 10, tier = 2, requiredLevel = 5, AttributeTierData(2, DoubleBounds(-0.04, -0.025))),
                    LevelRestrictedRandomOption(weight = 1, tier = 3, requiredLevel = 10, AttributeTierData(1, DoubleBounds(-0.05, -0.04))),
                )
            )
        ),
        RandomOption(
            weight = 1000,
            TieredRollableCustomAttribute(
                VanillaAttributeTypes.INCREASED_MOVEMENT_SPEED,
                listOf(
                    LevelRestrictedRandomOption(weight = 50, tier = 1, requiredLevel = 0, AttributeTierData(3, DoubleBounds(0.05, 0.1))),
                    LevelRestrictedRandomOption(weight = 10, tier = 2, requiredLevel = 5, AttributeTierData(2, DoubleBounds(0.1, 0.15))),
                    LevelRestrictedRandomOption(weight = 1, tier = 3, requiredLevel = 10, AttributeTierData(1, DoubleBounds(0.15, 0.2))),
                )
            )
        ),
    )

    override val rollableEnchants = listOf(
        RandomOption(20, EquipmentEnchantment.MENDING),
        RandomOption(35, EquipmentEnchantment.UNBREAKING_1),
        RandomOption(22, EquipmentEnchantment.UNBREAKING_2),
        RandomOption(11, EquipmentEnchantment.UNBREAKING_3),
        RandomOption(0, EquipmentEnchantment.CURSE_OF_VANISHING),
        RandomOption(20, EquipmentEnchantment.BLAST_PROTECTION_1),
        RandomOption(15, EquipmentEnchantment.BLAST_PROTECTION_2),
        RandomOption(10, EquipmentEnchantment.BLAST_PROTECTION_3),
        RandomOption(5, EquipmentEnchantment.BLAST_PROTECTION_4),
        RandomOption(0, EquipmentEnchantment.CURSE_OF_BINDING),
        RandomOption(20, EquipmentEnchantment.FIRE_PROTECTION_1),
        RandomOption(15, EquipmentEnchantment.FIRE_PROTECTION_2),
        RandomOption(10, EquipmentEnchantment.FIRE_PROTECTION_3),
        RandomOption(5, EquipmentEnchantment.FIRE_PROTECTION_4),
        RandomOption(20, EquipmentEnchantment.PROJECTILE_PROTECTION_1),
        RandomOption(15, EquipmentEnchantment.PROJECTILE_PROTECTION_2),
        RandomOption(10, EquipmentEnchantment.PROJECTILE_PROTECTION_3),
        RandomOption(5, EquipmentEnchantment.PROJECTILE_PROTECTION_4),
        RandomOption(40, EquipmentEnchantment.PROTECTION_1),
        RandomOption(30, EquipmentEnchantment.PROTECTION_2),
        RandomOption(20, EquipmentEnchantment.PROTECTION_3),
        RandomOption(10, EquipmentEnchantment.PROTECTION_4),
        RandomOption(3, EquipmentEnchantment.THORNS_1),
        RandomOption(2, EquipmentEnchantment.THORNS_2),
        RandomOption(1, EquipmentEnchantment.THORNS_3),
        RandomOption(25, EquipmentEnchantment.DEPTH_STRIDER_1),
        RandomOption(15, EquipmentEnchantment.DEPTH_STRIDER_2),
        RandomOption(5, EquipmentEnchantment.DEPTH_STRIDER_3),
        RandomOption(20, EquipmentEnchantment.FEATHER_FALLING_1),
        RandomOption(15, EquipmentEnchantment.FEATHER_FALLING_2),
        RandomOption(10, EquipmentEnchantment.FEATHER_FALLING_3),
        RandomOption(5, EquipmentEnchantment.FEATHER_FALLING_4),
        RandomOption(20, EquipmentEnchantment.FROST_WALKER_1),
        RandomOption(10, EquipmentEnchantment.FROST_WALKER_2),
        RandomOption(25, EquipmentEnchantment.SOUL_SPEED_1),
        RandomOption(15, EquipmentEnchantment.SOUL_SPEED_2),
        RandomOption(5, EquipmentEnchantment.SOUL_SPEED_3),
    )
}