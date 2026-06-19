package de.fuballer.mcendgame.main.component.item.equipment.tool

import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.custom_attribute.types.VanillaAttributeTypes
import de.fuballer.mcendgame.main.component.item.custom.tool.CustomToolItems
import de.fuballer.mcendgame.main.component.item.equipment.Equipment
import de.fuballer.mcendgame.main.component.item.equipment.enchantment.EquipmentEnchantment
import de.fuballer.mcendgame.main.util.random.LevelRestrictedRandomOption
import de.fuballer.mcendgame.main.util.random.RandomOption
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items

enum class Mace(
    override val item: Item,
) : Equipment {
    MACE(
        Items.MACE,
    ),
    GRAVEBREAKER(
        CustomToolItems.GRAVEBREAKER,
    );

    override val slot = EquipmentSlotGroup.MAINHAND
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
                LevelRestrictedRandomOption(
                    weight = 50,
                    tier = 1,
                    requiredLevel = 0,
                    RollableCustomAttribute(VanillaAttributeTypes.INCREASED_ATTACK_DAMAGE, 3, DoubleBounds(0.03, 0.06))
                ),
                LevelRestrictedRandomOption(
                    weight = 10,
                    tier = 2,
                    requiredLevel = 5,
                    RollableCustomAttribute(VanillaAttributeTypes.INCREASED_ATTACK_DAMAGE, 2, DoubleBounds(0.06, 0.09))
                ),
                LevelRestrictedRandomOption(
                    weight = 1,
                    tier = 3,
                    requiredLevel = 10,
                    RollableCustomAttribute(VanillaAttributeTypes.INCREASED_ATTACK_DAMAGE, 1, DoubleBounds(0.09, 0.12))
                ),
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
                LevelRestrictedRandomOption(weight = 50, tier = 1, requiredLevel = 0, RollableCustomAttribute(CustomAttributeTypes.SPELL_DAMAGE, 3, DoubleBounds(0.6, 1.4))),
                LevelRestrictedRandomOption(weight = 10, tier = 2, requiredLevel = 5, RollableCustomAttribute(CustomAttributeTypes.SPELL_DAMAGE, 2, DoubleBounds(1.4, 2.2))),
                LevelRestrictedRandomOption(weight = 1, tier = 3, requiredLevel = 10, RollableCustomAttribute(CustomAttributeTypes.SPELL_DAMAGE, 1, DoubleBounds(2.2, 3.0))),
            )
        ),
        RandomOption(
            weight = 1,
            listOf(
                LevelRestrictedRandomOption(
                    weight = 50,
                    tier = 1,
                    requiredLevel = 0,
                    RollableCustomAttribute(CustomAttributeTypes.INCREASED_SPELL_DAMAGE, 3, DoubleBounds(0.05, 0.1))
                ),
                LevelRestrictedRandomOption(
                    weight = 10,
                    tier = 2,
                    requiredLevel = 5,
                    RollableCustomAttribute(CustomAttributeTypes.INCREASED_SPELL_DAMAGE, 2, DoubleBounds(0.1, 0.15))
                ),
                LevelRestrictedRandomOption(
                    weight = 1,
                    tier = 3,
                    requiredLevel = 10,
                    RollableCustomAttribute(CustomAttributeTypes.INCREASED_SPELL_DAMAGE, 1, DoubleBounds(0.15, 0.2))
                ),
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
        RandomOption(10, EquipmentEnchantment.FIRE_ASPECT_1),
        RandomOption(5, EquipmentEnchantment.FIRE_ASPECT_2),
        RandomOption(25, EquipmentEnchantment.DENSITY_1),
        RandomOption(20, EquipmentEnchantment.DENSITY_2),
        RandomOption(15, EquipmentEnchantment.DENSITY_3),
        RandomOption(10, EquipmentEnchantment.DENSITY_4),
        RandomOption(5, EquipmentEnchantment.DENSITY_5),
        RandomOption(12, EquipmentEnchantment.BREACH_1),
        RandomOption(9, EquipmentEnchantment.BREACH_2),
        RandomOption(6, EquipmentEnchantment.BREACH_3),
        RandomOption(3, EquipmentEnchantment.BREACH_4),
        RandomOption(12, EquipmentEnchantment.WINDBURST_1),
        RandomOption(8, EquipmentEnchantment.WINDBURST_2),
        RandomOption(4, EquipmentEnchantment.WINDBURST_3),
    )
}