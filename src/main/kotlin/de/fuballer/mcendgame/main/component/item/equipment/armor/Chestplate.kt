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

enum class Chestplate(
    override val item: Item,
) : Equipment {
    LEATHER(
        Items.LEATHER_CHESTPLATE,
    ),
    COPPER(
        Items.COPPER_CHESTPLATE,
    ),
    GOLDEN(
        Items.GOLDEN_CHESTPLATE,
    ),
    CHAINMAIL(
        Items.CHAINMAIL_CHESTPLATE,
    ),
    IRON(
        Items.IRON_CHESTPLATE,
    ),
    DIAMOND(
        Items.DIAMOND_CHESTPLATE,
    ),
    NETHERITE(
        Items.NETHERITE_CHESTPLATE,
    ),
    DRUIDS_CHESTPLATE(
        CustomArmorItems.DRUIDS_CHESTPLATE,
    ),
    WITHER_ROSE_CHESTPLATE(
        CustomArmorItems.WITHER_ROSE_CHESTPLATE,
    ),
    BOUND_ABYSS(
        CustomArmorItems.BOUND_ABYSS,
    ),
    SUEDE_CHESTPLATE(
        CustomArmorItems.SUEDE_CHESTPLATE,
    ),
    VOIDWEAVER(
        CustomArmorItems.VOIDWEAVER,
    ),
    BROODMOTHER(
        CustomArmorItems.BROODMOTHER,
    );

    override val slot = EquipmentSlotGroup.CHEST

    override val rollableCustomAttributes = listOf(
        RandomOption(
            weight = 1000,
            TieredRollableCustomAttribute(
                VanillaAttributeTypes.ARMOR,
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
                VanillaAttributeTypes.ARMOR_TOUGHNESS,
                listOf(
                    LevelRestrictedRandomOption(weight = 50, tier = 1, requiredLevel = 0, AttributeTierData(3, DoubleBounds(0.5, 1.5))),
                    LevelRestrictedRandomOption(weight = 10, tier = 2, requiredLevel = 5, AttributeTierData(2, DoubleBounds(1.5, 2.5))),
                    LevelRestrictedRandomOption(weight = 1, tier = 3, requiredLevel = 10, AttributeTierData(1, DoubleBounds(2.5, 3.0))),
                )
            )
        ),
        RandomOption(
            weight = 1000,
            TieredRollableCustomAttribute(
                VanillaAttributeTypes.MAX_HEALTH,
                listOf(
                    LevelRestrictedRandomOption(weight = 50, tier = 1, requiredLevel = 0, AttributeTierData(3, DoubleBounds(0.5, 1.5))),
                    LevelRestrictedRandomOption(weight = 10, tier = 2, requiredLevel = 5, AttributeTierData(2, DoubleBounds(1.5, 2.5))),
                    LevelRestrictedRandomOption(weight = 1, tier = 3, requiredLevel = 10, AttributeTierData(1, DoubleBounds(2.5, 3.0))),
                )
            )
        ),
        RandomOption(
            weight = 1000,
            TieredRollableCustomAttribute(
                CustomAttributeTypes.SPELL_RESISTANCE,
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
                CustomAttributeTypes.DODGE,
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
                CustomAttributeTypes.PROJECTILE_DODGE,
                listOf(
                    LevelRestrictedRandomOption(weight = 50, tier = 1, requiredLevel = 0, AttributeTierData(3, DoubleBounds(0.04, 0.06))),
                    LevelRestrictedRandomOption(weight = 10, tier = 2, requiredLevel = 5, AttributeTierData(2, DoubleBounds(0.06, 0.08))),
                    LevelRestrictedRandomOption(weight = 1, tier = 3, requiredLevel = 10, AttributeTierData(1, DoubleBounds(0.08, 0.1))),
                )
            )
        ),
        RandomOption(
            weight = 1000,
            TieredRollableCustomAttribute(
                CustomAttributeTypes.MORE_DAMAGE_TAKEN,
                listOf(
                    LevelRestrictedRandomOption(weight = 50, tier = 1, requiredLevel = 0, AttributeTierData(3, DoubleBounds(-0.03, -0.01))),
                    LevelRestrictedRandomOption(weight = 10, tier = 2, requiredLevel = 5, AttributeTierData(2, DoubleBounds(-0.05, -0.03))),
                    LevelRestrictedRandomOption(weight = 1, tier = 3, requiredLevel = 10, AttributeTierData(1, DoubleBounds(-0.07, -0.05))),
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
    )
}