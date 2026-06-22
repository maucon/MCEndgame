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

enum class Leggings(
    override val item: Item,
) : Equipment {
    LEATHER(
        Items.LEATHER_LEGGINGS,
    ),
    COPPER(
        Items.COPPER_LEGGINGS,
    ),
    GOLDEN(
        Items.GOLDEN_LEGGINGS,
    ),
    CHAINMAIL(
        Items.CHAINMAIL_LEGGINGS,
    ),
    IRON(
        Items.IRON_LEGGINGS,
    ),
    DIAMOND(
        Items.DIAMOND_LEGGINGS,
    ),
    NETHERITE(
        Items.NETHERITE_LEGGINGS,
    ),
    DRUIDS_LEGGINGS(
        CustomArmorItems.DRUIDS_LEGGINGS,
    ),
    WITHER_ROSE_LEGGINGS(
        CustomArmorItems.WITHER_ROSE_LEGGINGS,
    ),
    LAMIAS_GIFT(
        CustomArmorItems.LAMIAS_GIFT,
    ),
    SUEDE_LEGGINGS(
        CustomArmorItems.SUEDE_LEGGINGS,
    ),
    STONEWARD(
        CustomArmorItems.STONEWARD,
    ),
    GILDED_TEMPEST(
        CustomArmorItems.GILDED_TEMPEST,
    ),
    WINDSTRIDER(
        CustomArmorItems.WINDSTRIDER,
    );

    override val slot = EquipmentSlotGroup.LEGS

    override val rollableCustomAttributes = listOf(
        RandomOption(
            weight = 1000,
            TieredRollableCustomAttribute(
                VanillaAttributeTypes.ARMOR,
                listOf(
                    LevelRestrictedRandomOption(weight = 50, tier = 1, requiredLevel = 0, AttributeTierData(3, DoubleBounds(0.4, 0.85))),
                    LevelRestrictedRandomOption(weight = 10, tier = 2, requiredLevel = 5, AttributeTierData(2, DoubleBounds(0.85, 1.3))),
                    LevelRestrictedRandomOption(weight = 1, tier = 3, requiredLevel = 10, AttributeTierData(1, DoubleBounds(1.3, 1.75))),
                )
            )
        ),
        RandomOption(
            weight = 1000,
            TieredRollableCustomAttribute(
                VanillaAttributeTypes.ARMOR_TOUGHNESS,
                listOf(
                    LevelRestrictedRandomOption(weight = 50, tier = 1, requiredLevel = 0, AttributeTierData(3, DoubleBounds(0.5, 1.25))),
                    LevelRestrictedRandomOption(weight = 10, tier = 2, requiredLevel = 5, AttributeTierData(2, DoubleBounds(1.25, 2.0))),
                    LevelRestrictedRandomOption(weight = 1, tier = 3, requiredLevel = 10, AttributeTierData(1, DoubleBounds(2.0, 2.5))),
                )
            )
        ),
        RandomOption(
            weight = 1000,
            TieredRollableCustomAttribute(
                VanillaAttributeTypes.MAX_HEALTH,
                listOf(
                    LevelRestrictedRandomOption(weight = 50, tier = 1, requiredLevel = 0, AttributeTierData(3, DoubleBounds(0.5, 1.25))),
                    LevelRestrictedRandomOption(weight = 10, tier = 2, requiredLevel = 5, AttributeTierData(2, DoubleBounds(1.25, 2.0))),
                    LevelRestrictedRandomOption(weight = 1, tier = 3, requiredLevel = 10, AttributeTierData(1, DoubleBounds(2.0, 2.5))),
                )
            )
        ),
        RandomOption(
            weight = 1000,
            TieredRollableCustomAttribute(
                CustomAttributeTypes.SPELL_RESISTANCE,
                listOf(
                    LevelRestrictedRandomOption(weight = 50, tier = 1, requiredLevel = 0, AttributeTierData(3, DoubleBounds(0.05, 0.125))),
                    LevelRestrictedRandomOption(weight = 10, tier = 2, requiredLevel = 5, AttributeTierData(2, DoubleBounds(0.125, 0.2))),
                    LevelRestrictedRandomOption(weight = 1, tier = 3, requiredLevel = 10, AttributeTierData(1, DoubleBounds(0.2, 0.25))),
                )
            )
        ),
        RandomOption(
            weight = 1000,
            TieredRollableCustomAttribute(
                CustomAttributeTypes.DODGE,
                listOf(
                    LevelRestrictedRandomOption(weight = 50, tier = 1, requiredLevel = 0, AttributeTierData(3, DoubleBounds(0.015, 0.035))),
                    LevelRestrictedRandomOption(weight = 10, tier = 2, requiredLevel = 5, AttributeTierData(2, DoubleBounds(0.035, 0.055))),
                    LevelRestrictedRandomOption(weight = 1, tier = 3, requiredLevel = 10, AttributeTierData(1, DoubleBounds(0.055, 0.07))),
                )
            )
        ),
        RandomOption(
            weight = 1000,
            TieredRollableCustomAttribute(
                CustomAttributeTypes.PROJECTILE_DODGE,
                listOf(
                    LevelRestrictedRandomOption(weight = 50, tier = 1, requiredLevel = 0, AttributeTierData(3, DoubleBounds(0.03, 0.05))),
                    LevelRestrictedRandomOption(weight = 10, tier = 2, requiredLevel = 5, AttributeTierData(2, DoubleBounds(0.05, 0.07))),
                    LevelRestrictedRandomOption(weight = 1, tier = 3, requiredLevel = 10, AttributeTierData(1, DoubleBounds(0.07, 0.09))),
                )
            )
        ),
        RandomOption(
            weight = 1000,
            TieredRollableCustomAttribute(
                CustomAttributeTypes.MORE_DAMAGE_TAKEN,
                listOf(
                    LevelRestrictedRandomOption(weight = 50, tier = 1, requiredLevel = 0, AttributeTierData(3, DoubleBounds(-0.0275, -0.01))),
                    LevelRestrictedRandomOption(weight = 10, tier = 2, requiredLevel = 5, AttributeTierData(2, DoubleBounds(-0.045, -0.0275))),
                    LevelRestrictedRandomOption(weight = 1, tier = 3, requiredLevel = 10, AttributeTierData(1, DoubleBounds(-0.06, -0.045))),
                )
            )
        ),
    )

    override val rollableEnchants = listOf(
        RandomOption(10, EquipmentEnchantment.MENDING),
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
        RandomOption(28, EquipmentEnchantment.SWIFT_SNEAK_1),
        RandomOption(18, EquipmentEnchantment.SWIFT_SNEAK_2),
        RandomOption(8, EquipmentEnchantment.SWIFT_SNEAK_3),
    )
}