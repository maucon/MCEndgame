package de.fuballer.mcendgame.component.item.equipment.tool

import de.fuballer.mcendgame.component.item.attribute.CustomAttributeTypes
import de.fuballer.mcendgame.component.item.attribute.VanillaAttributeTypes
import de.fuballer.mcendgame.component.item.attribute.data.BaseAttribute
import de.fuballer.mcendgame.component.item.attribute.data.DoubleBounds
import de.fuballer.mcendgame.component.item.attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.component.item.equipment.Equipment
import de.fuballer.mcendgame.component.item.equipment.ItemEnchantment
import de.fuballer.mcendgame.util.random.RandomOption
import org.bukkit.Material
import org.bukkit.inventory.EquipmentSlot

@Suppress("unused")
enum class Tool(
    override val material: Material,
    override val baseAttributes: List<BaseAttribute>,
    override val slot: EquipmentSlot,
    override val slotDependentAttributes: Boolean,
    override val rollableCustomAttributes: List<RandomOption<RollableCustomAttribute>>,
    override val rollableEnchants: List<RandomOption<ItemEnchantment>>,
) : Equipment {
    BOW(
        Material.BOW,
        listOf(),
        EquipmentSlot.HAND,
        false,
        listOf(
            RandomOption(10, RollableCustomAttribute(VanillaAttributeTypes.LUCK, DoubleBounds(1.0, 2.5))),
            RandomOption(10, RollableCustomAttribute(CustomAttributeTypes.INCREASED_PROJECTILE_DAMAGE, DoubleBounds(0.03, 0.15))),
            RandomOption(10, RollableCustomAttribute(CustomAttributeTypes.INCREASED_ARROW_VELOCITY, DoubleBounds(0.03, 0.15))),
            RandomOption(5, RollableCustomAttribute(CustomAttributeTypes.INCREASED_DAMAGE, DoubleBounds(0.02, 0.1)))
        ),
        listOf(
            RandomOption(10, ItemEnchantment.MENDING),
            RandomOption(20, ItemEnchantment.UNBREAKING_1),
            RandomOption(15, ItemEnchantment.UNBREAKING_2),
            RandomOption(10, ItemEnchantment.UNBREAKING_3),
            RandomOption(0, ItemEnchantment.CURSE_OF_VANISHING),
            RandomOption(25, ItemEnchantment.POWER_1),
            RandomOption(20, ItemEnchantment.POWER_2),
            RandomOption(15, ItemEnchantment.POWER_3),
            RandomOption(10, ItemEnchantment.POWER_4),
            RandomOption(5, ItemEnchantment.POWER_5),
            RandomOption(20, ItemEnchantment.PUNCH_1),
            RandomOption(10, ItemEnchantment.PUNCH_2),
            RandomOption(15, ItemEnchantment.FLAME),
            RandomOption(10, ItemEnchantment.INFINITY),
        )
    ),
    TRIDENT(
        Material.TRIDENT,
        listOf(
            BaseAttribute(VanillaAttributeTypes.ATTACK_DAMAGE, 9.0),
            BaseAttribute(VanillaAttributeTypes.ATTACK_SPEED, 1.1)
        ),
        EquipmentSlot.HAND,
        false,
        listOf(
            RandomOption(10, RollableCustomAttribute(VanillaAttributeTypes.ATTACK_DAMAGE, DoubleBounds(1.0, 5.0))),
            RandomOption(10, RollableCustomAttribute(VanillaAttributeTypes.ATTACK_SPEED, DoubleBounds(0.08, 0.4))),
            RandomOption(10, RollableCustomAttribute(VanillaAttributeTypes.ATTACK_KNOCKBACK, DoubleBounds(0.1, 0.5))),
            RandomOption(10, RollableCustomAttribute(VanillaAttributeTypes.LUCK, DoubleBounds(1.0, 2.5))),
            RandomOption(5, RollableCustomAttribute(CustomAttributeTypes.INCREASED_DAMAGE, DoubleBounds(0.03, 0.15)))
        ),
        listOf(
            RandomOption(10, ItemEnchantment.MENDING),
            RandomOption(20, ItemEnchantment.UNBREAKING_1),
            RandomOption(15, ItemEnchantment.UNBREAKING_2),
            RandomOption(10, ItemEnchantment.UNBREAKING_3),
            RandomOption(0, ItemEnchantment.CURSE_OF_VANISHING),
            RandomOption(10, ItemEnchantment.CHANNELING),
            RandomOption(15, ItemEnchantment.LOYALTY_1),
            RandomOption(10, ItemEnchantment.LOYALTY_2),
            RandomOption(5, ItemEnchantment.LOYALTY_3),
            RandomOption(25, ItemEnchantment.IMPALING_1),
            RandomOption(20, ItemEnchantment.IMPALING_2),
            RandomOption(15, ItemEnchantment.IMPALING_3),
            RandomOption(10, ItemEnchantment.IMPALING_4),
            RandomOption(5, ItemEnchantment.IMPALING_5),
            RandomOption(15, ItemEnchantment.RIPTIDE_1),
            RandomOption(10, ItemEnchantment.RIPTIDE_2),
            RandomOption(5, ItemEnchantment.RIPTIDE_3),
        )
    ),
    MACE(
        Material.MACE,
        listOf(
            BaseAttribute(VanillaAttributeTypes.ATTACK_DAMAGE, 6.0),
            BaseAttribute(VanillaAttributeTypes.ATTACK_SPEED, 0.6)
        ),
        EquipmentSlot.HAND,
        false,
        listOf(
            RandomOption(10, RollableCustomAttribute(VanillaAttributeTypes.ATTACK_DAMAGE, DoubleBounds(1.0, 5.0))),
            RandomOption(10, RollableCustomAttribute(VanillaAttributeTypes.ATTACK_SPEED, DoubleBounds(0.04, 0.2))),
            RandomOption(10, RollableCustomAttribute(VanillaAttributeTypes.ATTACK_KNOCKBACK, DoubleBounds(0.1, 0.5))),
            RandomOption(10, RollableCustomAttribute(VanillaAttributeTypes.LUCK, DoubleBounds(1.0, 2.5))),
            RandomOption(5, RollableCustomAttribute(CustomAttributeTypes.INCREASED_DAMAGE, DoubleBounds(0.03, 0.15)))
        ),
        listOf(
            RandomOption(10, ItemEnchantment.MENDING),
            RandomOption(20, ItemEnchantment.UNBREAKING_1),
            RandomOption(15, ItemEnchantment.UNBREAKING_2),
            RandomOption(10, ItemEnchantment.UNBREAKING_3),
            RandomOption(0, ItemEnchantment.CURSE_OF_VANISHING),
            RandomOption(15, ItemEnchantment.SMITE_1),
            RandomOption(12, ItemEnchantment.SMITE_2),
            RandomOption(9, ItemEnchantment.SMITE_3),
            RandomOption(6, ItemEnchantment.SMITE_4),
            RandomOption(3, ItemEnchantment.SMITE_5),
            RandomOption(15, ItemEnchantment.BANE_OF_ARTHROPODS_1),
            RandomOption(12, ItemEnchantment.BANE_OF_ARTHROPODS_2),
            RandomOption(9, ItemEnchantment.BANE_OF_ARTHROPODS_3),
            RandomOption(6, ItemEnchantment.BANE_OF_ARTHROPODS_4),
            RandomOption(3, ItemEnchantment.BANE_OF_ARTHROPODS_5),
            RandomOption(10, ItemEnchantment.FIRE_ASPECT_1),
            RandomOption(5, ItemEnchantment.FIRE_ASPECT_2),
            RandomOption(25, ItemEnchantment.DENSITY_1),
            RandomOption(20, ItemEnchantment.DENSITY_2),
            RandomOption(15, ItemEnchantment.DENSITY_3),
            RandomOption(10, ItemEnchantment.DENSITY_4),
            RandomOption(5, ItemEnchantment.DENSITY_5),
            RandomOption(12, ItemEnchantment.BREACH_1),
            RandomOption(9, ItemEnchantment.BREACH_2),
            RandomOption(6, ItemEnchantment.BREACH_3),
            RandomOption(3, ItemEnchantment.BREACH_4),
            RandomOption(12, ItemEnchantment.WINDBURST_1),
            RandomOption(8, ItemEnchantment.WINDBURST_2),
            RandomOption(4, ItemEnchantment.WINDBURST_3),
        )
    ),
    FISHING_ROD(
        Material.FISHING_ROD,
        listOf(),
        EquipmentSlot.OFF_HAND,
        false,
        listOf(
            RandomOption(10, RollableCustomAttribute(VanillaAttributeTypes.ATTACK_DAMAGE, DoubleBounds(1.0, 2.5))),
            RandomOption(10, RollableCustomAttribute(VanillaAttributeTypes.ATTACK_SPEED, DoubleBounds(0.08, 0.4))),
            RandomOption(10, RollableCustomAttribute(VanillaAttributeTypes.ATTACK_KNOCKBACK, DoubleBounds(0.1, 0.5))),
            RandomOption(10, RollableCustomAttribute(VanillaAttributeTypes.LUCK, DoubleBounds(1.0, 2.5)))
        ),
        listOf(
            RandomOption(10, ItemEnchantment.MENDING),
            RandomOption(25, ItemEnchantment.UNBREAKING_1),
            RandomOption(20, ItemEnchantment.UNBREAKING_2),
            RandomOption(15, ItemEnchantment.UNBREAKING_3),
            RandomOption(0, ItemEnchantment.CURSE_OF_VANISHING),
            RandomOption(20, ItemEnchantment.LUCK_OF_THE_SEA_1),
            RandomOption(15, ItemEnchantment.LUCK_OF_THE_SEA_2),
            RandomOption(10, ItemEnchantment.LUCK_OF_THE_SEA_3),
            RandomOption(20, ItemEnchantment.LURE_1),
            RandomOption(15, ItemEnchantment.LURE_2),
            RandomOption(10, ItemEnchantment.LURE_3),
        )
    ),
    SHIELD(
        Material.SHIELD,
        listOf(),
        EquipmentSlot.OFF_HAND,
        false,
        listOf(
            RandomOption(10, RollableCustomAttribute(VanillaAttributeTypes.ATTACK_DAMAGE, DoubleBounds(0.8, 4.0))),
            RandomOption(10, RollableCustomAttribute(VanillaAttributeTypes.ATTACK_SPEED, DoubleBounds(0.08, 0.4))),
            RandomOption(10, RollableCustomAttribute(VanillaAttributeTypes.ATTACK_KNOCKBACK, DoubleBounds(0.1, 0.5))),
            RandomOption(10, RollableCustomAttribute(VanillaAttributeTypes.LUCK, DoubleBounds(1.0, 2.5))),
            RandomOption(10, RollableCustomAttribute(CustomAttributeTypes.REDUCED_DAMAGE_TAKEN, DoubleBounds(0.0016, 0.08))),
            RandomOption(10, RollableCustomAttribute(CustomAttributeTypes.REDUCED_PROJECTILE_DAMAGE_TAKEN, DoubleBounds(0.03, 0.15)))
        ),
        listOf(
            RandomOption(10, ItemEnchantment.MENDING),
            RandomOption(25, ItemEnchantment.UNBREAKING_1),
            RandomOption(20, ItemEnchantment.UNBREAKING_2),
            RandomOption(15, ItemEnchantment.UNBREAKING_3),
            RandomOption(0, ItemEnchantment.CURSE_OF_VANISHING),
        )
    ),
    CROSSBOW(
        Material.CROSSBOW,
        listOf(),
        EquipmentSlot.HAND,
        false,
        listOf(
            RandomOption(10, RollableCustomAttribute(VanillaAttributeTypes.LUCK, DoubleBounds(0.1, 2.5))),
            RandomOption(10, RollableCustomAttribute(CustomAttributeTypes.INCREASED_PROJECTILE_DAMAGE, DoubleBounds(0.03, 0.15))),
            RandomOption(10, RollableCustomAttribute(CustomAttributeTypes.INCREASED_ARROW_VELOCITY, DoubleBounds(0.03, 0.15))),
            RandomOption(5, RollableCustomAttribute(CustomAttributeTypes.INCREASED_DAMAGE, DoubleBounds(0.02, 0.1)))
        ),
        listOf(
            RandomOption(10, ItemEnchantment.MENDING),
            RandomOption(25, ItemEnchantment.UNBREAKING_1),
            RandomOption(20, ItemEnchantment.UNBREAKING_2),
            RandomOption(15, ItemEnchantment.UNBREAKING_3),
            RandomOption(0, ItemEnchantment.CURSE_OF_VANISHING),
            RandomOption(15, ItemEnchantment.MULTISHOT),
            RandomOption(20, ItemEnchantment.PIERCING_1),
            RandomOption(15, ItemEnchantment.PIERCING_2),
            RandomOption(10, ItemEnchantment.PIERCING_3),
            RandomOption(5, ItemEnchantment.PIERCING_4),
            RandomOption(15, ItemEnchantment.QUICK_CHARGE_1),
            RandomOption(10, ItemEnchantment.QUICK_CHARGE_2),
            RandomOption(5, ItemEnchantment.QUICK_CHARGE_3),
        )
    ),
    FLINT_AND_STEEL(
        Material.FLINT_AND_STEEL,
        listOf(),
        EquipmentSlot.OFF_HAND,
        false,
        listOf(
            RandomOption(10, RollableCustomAttribute(VanillaAttributeTypes.ATTACK_DAMAGE, DoubleBounds(1.0, 2.5))),
            RandomOption(10, RollableCustomAttribute(VanillaAttributeTypes.ATTACK_SPEED, DoubleBounds(0.08, 0.4))),
            RandomOption(10, RollableCustomAttribute(VanillaAttributeTypes.ATTACK_KNOCKBACK, DoubleBounds(0.1, 0.5))),
            RandomOption(10, RollableCustomAttribute(VanillaAttributeTypes.LUCK, DoubleBounds(1.0, 2.5)))
        ),
        listOf(
            RandomOption(10, ItemEnchantment.MENDING),
            RandomOption(25, ItemEnchantment.UNBREAKING_1),
            RandomOption(20, ItemEnchantment.UNBREAKING_2),
            RandomOption(15, ItemEnchantment.UNBREAKING_3),
            RandomOption(0, ItemEnchantment.CURSE_OF_VANISHING),
        )
    ),
    CARROT_ON_A_STICK(
        Material.CARROT_ON_A_STICK,
        listOf(),
        EquipmentSlot.OFF_HAND,
        false,
        listOf(
            RandomOption(10, RollableCustomAttribute(VanillaAttributeTypes.ATTACK_DAMAGE, DoubleBounds(1.0, 2.5))),
            RandomOption(10, RollableCustomAttribute(VanillaAttributeTypes.ATTACK_SPEED, DoubleBounds(0.08, 0.4))),
            RandomOption(10, RollableCustomAttribute(VanillaAttributeTypes.ATTACK_KNOCKBACK, DoubleBounds(0.1, 0.5))),
            RandomOption(10, RollableCustomAttribute(VanillaAttributeTypes.LUCK, DoubleBounds(1.0, 2.5)))
        ),
        listOf(
            RandomOption(10, ItemEnchantment.MENDING),
            RandomOption(25, ItemEnchantment.UNBREAKING_1),
            RandomOption(20, ItemEnchantment.UNBREAKING_2),
            RandomOption(15, ItemEnchantment.UNBREAKING_3),
            RandomOption(0, ItemEnchantment.CURSE_OF_VANISHING),
        )
    ),
    WARPED_FUNGUS_ON_A_STICK(
        Material.WARPED_FUNGUS_ON_A_STICK,
        listOf(),
        EquipmentSlot.OFF_HAND,
        false,
        listOf(
            RandomOption(10, RollableCustomAttribute(VanillaAttributeTypes.ATTACK_DAMAGE, DoubleBounds(1.0, 2.5))),
            RandomOption(10, RollableCustomAttribute(VanillaAttributeTypes.ATTACK_SPEED, DoubleBounds(0.08, 0.4))),
            RandomOption(10, RollableCustomAttribute(VanillaAttributeTypes.ATTACK_KNOCKBACK, DoubleBounds(0.1, 0.5))),
            RandomOption(10, RollableCustomAttribute(VanillaAttributeTypes.LUCK, DoubleBounds(1.0, 2.5)))
        ),
        listOf(
            RandomOption(10, ItemEnchantment.MENDING),
            RandomOption(25, ItemEnchantment.UNBREAKING_1),
            RandomOption(20, ItemEnchantment.UNBREAKING_2),
            RandomOption(15, ItemEnchantment.UNBREAKING_3),
            RandomOption(0, ItemEnchantment.CURSE_OF_VANISHING),
        )
    ),
    ELYTRA(
        Material.ELYTRA,
        listOf(),
        EquipmentSlot.CHEST,
        true,
        listOf(),
        listOf(
            RandomOption(10, ItemEnchantment.MENDING),
            RandomOption(25, ItemEnchantment.UNBREAKING_1),
            RandomOption(20, ItemEnchantment.UNBREAKING_2),
            RandomOption(15, ItemEnchantment.UNBREAKING_3),
            RandomOption(0, ItemEnchantment.CURSE_OF_VANISHING),
            RandomOption(0, ItemEnchantment.CURSE_OF_BINDING),
        )
    ),
    SHEARS(
        Material.SHEARS,
        listOf(),
        EquipmentSlot.OFF_HAND,
        false,
        listOf(
            RandomOption(10, RollableCustomAttribute(VanillaAttributeTypes.ATTACK_DAMAGE, DoubleBounds(1.0, 2.5))),
            RandomOption(10, RollableCustomAttribute(VanillaAttributeTypes.ATTACK_SPEED, DoubleBounds(0.08, 0.4))),
            RandomOption(10, RollableCustomAttribute(VanillaAttributeTypes.ATTACK_KNOCKBACK, DoubleBounds(0.1, 0.5))),
            RandomOption(10, RollableCustomAttribute(VanillaAttributeTypes.LUCK, DoubleBounds(1.0, 2.5)))
        ),
        listOf(
            RandomOption(10, ItemEnchantment.MENDING),
            RandomOption(25, ItemEnchantment.UNBREAKING_1),
            RandomOption(20, ItemEnchantment.UNBREAKING_2),
            RandomOption(15, ItemEnchantment.UNBREAKING_3),
            RandomOption(0, ItemEnchantment.CURSE_OF_VANISHING),
            RandomOption(25, ItemEnchantment.EFFICIENCY_1),
            RandomOption(20, ItemEnchantment.EFFICIENCY_2),
            RandomOption(15, ItemEnchantment.EFFICIENCY_3),
            RandomOption(10, ItemEnchantment.EFFICIENCY_4),
            RandomOption(5, ItemEnchantment.EFFICIENCY_5),
        )
    );
}