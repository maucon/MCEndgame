package de.fuballer.mcendgame.main.component.item.equipment.tool

import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.custom_attribute.types.VanillaAttributeTypes
import de.fuballer.mcendgame.main.component.item.custom.tool.CustomToolItems
import de.fuballer.mcendgame.main.component.item.equipment.Equipment
import de.fuballer.mcendgame.main.component.item.equipment.data.AttributeTierData
import de.fuballer.mcendgame.main.component.item.equipment.data.TieredRollableCustomAttribute
import de.fuballer.mcendgame.main.component.item.equipment.enchantment.EquipmentEnchantment
import de.fuballer.mcendgame.main.util.random.LevelRestrictedRandomOption
import de.fuballer.mcendgame.main.util.random.RandomOption
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items

enum class Shield(
    override val item: Item,
) : Equipment {
    SHIELD(
        Items.SHIELD,
    ),
    GRUDGEBEARER(
        CustomToolItems.GRUDGEBEARER,
    );

    override val slot = EquipmentSlotGroup.HAND

    override val rollableCustomAttributes = listOf(
        RandomOption(
            weight = 1000,
            TieredRollableCustomAttribute(
                CustomAttributeTypes.INCREASED_DAMAGE,
                listOf(
                    LevelRestrictedRandomOption(weight = 50, tier = 1, requiredLevel = 0, AttributeTierData(3, DoubleBounds(0.01, 0.025))),
                    LevelRestrictedRandomOption(weight = 10, tier = 2, requiredLevel = 5, AttributeTierData(2, DoubleBounds(0.025, 0.04))),
                    LevelRestrictedRandomOption(weight = 1, tier = 3, requiredLevel = 10, AttributeTierData(1, DoubleBounds(0.04, 0.05))),
                )
            )
        ),
        RandomOption(
            weight = 1000,
            TieredRollableCustomAttribute(
                CustomAttributeTypes.INCREASED_SPELL_DAMAGE,
                listOf(
                    LevelRestrictedRandomOption(weight = 50, tier = 1, requiredLevel = 0, AttributeTierData(3, DoubleBounds(0.02, 0.07))),
                    LevelRestrictedRandomOption(weight = 10, tier = 2, requiredLevel = 5, AttributeTierData(2, DoubleBounds(0.07, 0.12))),
                    LevelRestrictedRandomOption(weight = 1, tier = 3, requiredLevel = 10, AttributeTierData(1, DoubleBounds(0.12, 0.17))),
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
                VanillaAttributeTypes.INCREASED_ATTACK_DAMAGE,
                listOf(
                    LevelRestrictedRandomOption(weight = 50, tier = 1, requiredLevel = 0, AttributeTierData(3, DoubleBounds(0.02, 0.07))),
                    LevelRestrictedRandomOption(weight = 10, tier = 2, requiredLevel = 5, AttributeTierData(2, DoubleBounds(0.07, 0.12))),
                    LevelRestrictedRandomOption(weight = 1, tier = 3, requiredLevel = 10, AttributeTierData(1, DoubleBounds(0.12, 0.17))),
                )
            )
        ),
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
    )

    override val rollableEnchants = listOf(
        RandomOption(10, EquipmentEnchantment.MENDING),
        RandomOption(25, EquipmentEnchantment.UNBREAKING_1),
        RandomOption(20, EquipmentEnchantment.UNBREAKING_2),
        RandomOption(15, EquipmentEnchantment.UNBREAKING_3),
        RandomOption(0, EquipmentEnchantment.CURSE_OF_VANISHING),
    )
}