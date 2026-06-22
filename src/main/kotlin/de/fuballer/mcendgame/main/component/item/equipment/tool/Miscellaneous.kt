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

@Suppress("unused")
enum class Miscellaneous(
    override val item: Item,
    override val slot: EquipmentSlotGroup,
    override val rollableCustomAttributes: List<RandomOption<TieredRollableCustomAttribute>>,
    override val rollableEnchants: List<RandomOption<EquipmentEnchantment>>,
) : Equipment {
    TRIDENT(
        Items.TRIDENT,
        EquipmentSlotGroup.MAINHAND,
        listOf(
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
        ),
        listOf(
            RandomOption(10, EquipmentEnchantment.MENDING),
            RandomOption(20, EquipmentEnchantment.UNBREAKING_1),
            RandomOption(15, EquipmentEnchantment.UNBREAKING_2),
            RandomOption(10, EquipmentEnchantment.UNBREAKING_3),
            RandomOption(0, EquipmentEnchantment.CURSE_OF_VANISHING),
            RandomOption(10, EquipmentEnchantment.CHANNELING),
            RandomOption(15, EquipmentEnchantment.LOYALTY_1),
            RandomOption(10, EquipmentEnchantment.LOYALTY_2),
            RandomOption(5, EquipmentEnchantment.LOYALTY_3),
            RandomOption(25, EquipmentEnchantment.IMPALING_1),
            RandomOption(20, EquipmentEnchantment.IMPALING_2),
            RandomOption(15, EquipmentEnchantment.IMPALING_3),
            RandomOption(10, EquipmentEnchantment.IMPALING_4),
            RandomOption(5, EquipmentEnchantment.IMPALING_5),
            RandomOption(15, EquipmentEnchantment.RIPTIDE_1),
            RandomOption(10, EquipmentEnchantment.RIPTIDE_2),
            RandomOption(5, EquipmentEnchantment.RIPTIDE_3),
        )
    ),
    FISHING_ROD(
        Items.FISHING_ROD,
        EquipmentSlotGroup.OFFHAND,
        listOf(
            RandomOption(
                weight = 1000,
                TieredRollableCustomAttribute(
                    VanillaAttributeTypes.LUCK,
                    listOf(
                        LevelRestrictedRandomOption(weight = 50, tier = 1, requiredLevel = 0, AttributeTierData(3, DoubleBounds(0.5, 2.0))),
                        LevelRestrictedRandomOption(weight = 10, tier = 2, requiredLevel = 5, AttributeTierData(2, DoubleBounds(2.0, 3.5))),
                        LevelRestrictedRandomOption(weight = 1, tier = 3, requiredLevel = 10, AttributeTierData(1, DoubleBounds(3.5, 5.0))),
                    )
                )
            ),
        ),
        listOf(
            RandomOption(10, EquipmentEnchantment.MENDING),
            RandomOption(25, EquipmentEnchantment.UNBREAKING_1),
            RandomOption(20, EquipmentEnchantment.UNBREAKING_2),
            RandomOption(15, EquipmentEnchantment.UNBREAKING_3),
            RandomOption(0, EquipmentEnchantment.CURSE_OF_VANISHING),
            RandomOption(20, EquipmentEnchantment.LUCK_OF_THE_SEA_1),
            RandomOption(15, EquipmentEnchantment.LUCK_OF_THE_SEA_2),
            RandomOption(10, EquipmentEnchantment.LUCK_OF_THE_SEA_3),
            RandomOption(20, EquipmentEnchantment.LURE_1),
            RandomOption(15, EquipmentEnchantment.LURE_2),
            RandomOption(10, EquipmentEnchantment.LURE_3),
        )
    ),
    CROSSBOW(
        Items.CROSSBOW,
        EquipmentSlotGroup.MAINHAND,
        listOf(
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
            RandomOption(
                weight = 1000,
                TieredRollableCustomAttribute(
                    CustomAttributeTypes.INCREASED_PROJECTILE_DAMAGE,
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
                    CustomAttributeTypes.INCREASED_SPELL_DAMAGE,
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
                        LevelRestrictedRandomOption(weight = 50, tier = 1, requiredLevel = 0, AttributeTierData(3, DoubleBounds(0.03, 0.06))),
                        LevelRestrictedRandomOption(weight = 10, tier = 2, requiredLevel = 5, AttributeTierData(2, DoubleBounds(0.06, 0.09))),
                        LevelRestrictedRandomOption(weight = 1, tier = 3, requiredLevel = 10, AttributeTierData(1, DoubleBounds(0.09, 0.12))),
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
        ),
        listOf(
            RandomOption(10, EquipmentEnchantment.MENDING),
            RandomOption(25, EquipmentEnchantment.UNBREAKING_1),
            RandomOption(20, EquipmentEnchantment.UNBREAKING_2),
            RandomOption(15, EquipmentEnchantment.UNBREAKING_3),
            RandomOption(0, EquipmentEnchantment.CURSE_OF_VANISHING),
            RandomOption(15, EquipmentEnchantment.MULTISHOT),
            RandomOption(20, EquipmentEnchantment.PIERCING_1),
            RandomOption(15, EquipmentEnchantment.PIERCING_2),
            RandomOption(10, EquipmentEnchantment.PIERCING_3),
            RandomOption(5, EquipmentEnchantment.PIERCING_4),
            RandomOption(15, EquipmentEnchantment.QUICK_CHARGE_1),
            RandomOption(10, EquipmentEnchantment.QUICK_CHARGE_2),
            RandomOption(5, EquipmentEnchantment.QUICK_CHARGE_3),
        )
    ),
    FLINT_AND_STEEL(
        Items.FLINT_AND_STEEL,
        EquipmentSlotGroup.OFFHAND,
        listOf(),
        listOf(
            RandomOption(10, EquipmentEnchantment.MENDING),
            RandomOption(25, EquipmentEnchantment.UNBREAKING_1),
            RandomOption(20, EquipmentEnchantment.UNBREAKING_2),
            RandomOption(15, EquipmentEnchantment.UNBREAKING_3),
            RandomOption(0, EquipmentEnchantment.CURSE_OF_VANISHING),
        )
    ),
    CARROT_ON_A_STICK(
        Items.CARROT_ON_A_STICK,
        EquipmentSlotGroup.OFFHAND,
        listOf(),
        listOf(
            RandomOption(10, EquipmentEnchantment.MENDING),
            RandomOption(25, EquipmentEnchantment.UNBREAKING_1),
            RandomOption(20, EquipmentEnchantment.UNBREAKING_2),
            RandomOption(15, EquipmentEnchantment.UNBREAKING_3),
            RandomOption(0, EquipmentEnchantment.CURSE_OF_VANISHING),
        )
    ),
    WARPED_FUNGUS_ON_A_STICK(
        Items.WARPED_FUNGUS_ON_A_STICK,
        EquipmentSlotGroup.OFFHAND,
        listOf(),
        listOf(
            RandomOption(10, EquipmentEnchantment.MENDING),
            RandomOption(25, EquipmentEnchantment.UNBREAKING_1),
            RandomOption(20, EquipmentEnchantment.UNBREAKING_2),
            RandomOption(15, EquipmentEnchantment.UNBREAKING_3),
            RandomOption(0, EquipmentEnchantment.CURSE_OF_VANISHING),
        )
    ),
    ELYTRA(
        Items.ELYTRA,
        EquipmentSlotGroup.CHEST,
        listOf(
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
        ),
        listOf(
            RandomOption(10, EquipmentEnchantment.MENDING),
            RandomOption(25, EquipmentEnchantment.UNBREAKING_1),
            RandomOption(20, EquipmentEnchantment.UNBREAKING_2),
            RandomOption(15, EquipmentEnchantment.UNBREAKING_3),
            RandomOption(0, EquipmentEnchantment.CURSE_OF_VANISHING),
            RandomOption(0, EquipmentEnchantment.CURSE_OF_BINDING),
        )
    ),
    SHEARS(
        Items.SHEARS,
        EquipmentSlotGroup.OFFHAND,
        listOf(),
        listOf(
            RandomOption(10, EquipmentEnchantment.MENDING),
            RandomOption(25, EquipmentEnchantment.UNBREAKING_1),
            RandomOption(20, EquipmentEnchantment.UNBREAKING_2),
            RandomOption(15, EquipmentEnchantment.UNBREAKING_3),
            RandomOption(0, EquipmentEnchantment.CURSE_OF_VANISHING),
            RandomOption(25, EquipmentEnchantment.EFFICIENCY_1),
            RandomOption(20, EquipmentEnchantment.EFFICIENCY_2),
            RandomOption(15, EquipmentEnchantment.EFFICIENCY_3),
            RandomOption(10, EquipmentEnchantment.EFFICIENCY_4),
            RandomOption(5, EquipmentEnchantment.EFFICIENCY_5),
        )
    );
}