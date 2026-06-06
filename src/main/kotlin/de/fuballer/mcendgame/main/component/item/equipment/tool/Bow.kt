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

enum class Bow(
    override val item: Item,
) : Equipment {
    BOW(
        Items.BOW,
    ),
    WINDSTRING(
        CustomToolItems.WINDSTRING,
    ),
    HAILSTORM(
        CustomToolItems.HAILSTORM,
    ),
    DUSK_PIERCER(
        CustomToolItems.DUSK_PIERCER,
    );

    override val slot = EquipmentSlotGroup.MAINHAND

    override val rollableCustomAttributes = listOf(
        RandomOption(
            weight = 1,
            listOf(
                LevelRestrictedRandomOption(weight = 50, tier = 1, requiredLevel = 0, RollableCustomAttribute(CustomAttributeTypes.INCREASED_DAMAGE, 3, DoubleBounds(0.015, 0.03))),
                LevelRestrictedRandomOption(weight = 10, tier = 2, requiredLevel = 5, RollableCustomAttribute(CustomAttributeTypes.INCREASED_DAMAGE, 2, DoubleBounds(0.03, 0.045))),
                LevelRestrictedRandomOption(weight = 1, tier = 3, requiredLevel = 10, RollableCustomAttribute(CustomAttributeTypes.INCREASED_DAMAGE, 1, DoubleBounds(0.045, 0.06))),
            )
        ),
        RandomOption(
            weight = 1,
            listOf(
                LevelRestrictedRandomOption(weight = 50, tier = 1, requiredLevel = 0, RollableCustomAttribute(CustomAttributeTypes.INCREASED_PROJECTILE_DAMAGE, 3, DoubleBounds(0.03, 0.06))),
                LevelRestrictedRandomOption(weight = 10, tier = 2, requiredLevel = 5, RollableCustomAttribute(CustomAttributeTypes.INCREASED_PROJECTILE_DAMAGE, 2, DoubleBounds(0.06, 0.09))),
                LevelRestrictedRandomOption(weight = 1, tier = 3, requiredLevel = 10, RollableCustomAttribute(CustomAttributeTypes.INCREASED_PROJECTILE_DAMAGE, 1, DoubleBounds(0.09, 0.12))),
            )
        ),
        RandomOption(
            weight = 1,
            listOf(
                LevelRestrictedRandomOption(weight = 50, tier = 1, requiredLevel = 0, RollableCustomAttribute(CustomAttributeTypes.INCREASED_ELEMENTAL_DAMAGE, 3, DoubleBounds(0.03, 0.06))),
                LevelRestrictedRandomOption(weight = 10, tier = 2, requiredLevel = 5, RollableCustomAttribute(CustomAttributeTypes.INCREASED_ELEMENTAL_DAMAGE, 2, DoubleBounds(0.06, 0.09))),
                LevelRestrictedRandomOption(weight = 1, tier = 3, requiredLevel = 10, RollableCustomAttribute(CustomAttributeTypes.INCREASED_ELEMENTAL_DAMAGE, 1, DoubleBounds(0.09, 0.12))),
            )
        ),
        RandomOption(
            weight = 1,
            listOf(
                LevelRestrictedRandomOption(weight = 50, tier = 1, requiredLevel = 0, RollableCustomAttribute(CustomAttributeTypes.ELEMENTAL_DAMAGE, 3, DoubleBounds(0.6, 1.4))),
                LevelRestrictedRandomOption(weight = 10, tier = 2, requiredLevel = 5, RollableCustomAttribute(CustomAttributeTypes.ELEMENTAL_DAMAGE, 2, DoubleBounds(1.4, 2.2))),
                LevelRestrictedRandomOption(weight = 1, tier = 3, requiredLevel = 10, RollableCustomAttribute(CustomAttributeTypes.ELEMENTAL_DAMAGE, 1, DoubleBounds(2.2, 3.0))),
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
                LevelRestrictedRandomOption(weight = 50, tier = 1, requiredLevel = 0, RollableCustomAttribute(VanillaAttributeTypes.ATTACK_DAMAGE, 3, DoubleBounds(0.6, 1.4))),
                LevelRestrictedRandomOption(weight = 10, tier = 2, requiredLevel = 5, RollableCustomAttribute(VanillaAttributeTypes.ATTACK_DAMAGE, 2, DoubleBounds(1.4, 2.2))),
                LevelRestrictedRandomOption(weight = 1, tier = 3, requiredLevel = 10, RollableCustomAttribute(VanillaAttributeTypes.ATTACK_DAMAGE, 1, DoubleBounds(2.2, 3.0))),
            )
        ),
    )

    override val rollableEnchants = listOf(
        RandomOption(10, EquipmentEnchantment.MENDING),
        RandomOption(20, EquipmentEnchantment.UNBREAKING_1),
        RandomOption(15, EquipmentEnchantment.UNBREAKING_2),
        RandomOption(10, EquipmentEnchantment.UNBREAKING_3),
        RandomOption(0, EquipmentEnchantment.CURSE_OF_VANISHING),
        RandomOption(25, EquipmentEnchantment.POWER_1),
        RandomOption(20, EquipmentEnchantment.POWER_2),
        RandomOption(15, EquipmentEnchantment.POWER_3),
        RandomOption(10, EquipmentEnchantment.POWER_4),
        RandomOption(5, EquipmentEnchantment.POWER_5),
        RandomOption(20, EquipmentEnchantment.PUNCH_1),
        RandomOption(10, EquipmentEnchantment.PUNCH_2),
        RandomOption(15, EquipmentEnchantment.FLAME),
        RandomOption(10, EquipmentEnchantment.INFINITY),
    )
}