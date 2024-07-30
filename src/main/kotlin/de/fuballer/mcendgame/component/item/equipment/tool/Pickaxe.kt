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

enum class Pickaxe(
    override val material: Material,
    override val baseAttributes: List<BaseAttribute>
) : Equipment {
    WOODEN(
        Material.WOODEN_PICKAXE,
        listOf(
            BaseAttribute(VanillaAttributeTypes.ATTACK_DAMAGE, 2.0),
            BaseAttribute(VanillaAttributeTypes.ATTACK_SPEED, 1.2)
        )
    ),
    GOLDEN(
        Material.GOLDEN_PICKAXE,
        listOf(
            BaseAttribute(VanillaAttributeTypes.ATTACK_DAMAGE, 2.0),
            BaseAttribute(VanillaAttributeTypes.ATTACK_SPEED, 1.2)
        )
    ),
    STONE(
        Material.STONE_PICKAXE,
        listOf(
            BaseAttribute(VanillaAttributeTypes.ATTACK_DAMAGE, 3.0),
            BaseAttribute(VanillaAttributeTypes.ATTACK_SPEED, 1.2)
        )
    ),
    IRON(
        Material.IRON_PICKAXE,
        listOf(
            BaseAttribute(VanillaAttributeTypes.ATTACK_DAMAGE, 4.0),
            BaseAttribute(VanillaAttributeTypes.ATTACK_SPEED, 1.2)
        )
    ),
    DIAMOND(
        Material.DIAMOND_PICKAXE,
        listOf(
            BaseAttribute(VanillaAttributeTypes.ATTACK_DAMAGE, 5.0),
            BaseAttribute(VanillaAttributeTypes.ATTACK_SPEED, 1.2)
        )
    ),
    NETHERITE(
        Material.NETHERITE_PICKAXE,
        listOf(
            BaseAttribute(VanillaAttributeTypes.ATTACK_DAMAGE, 6.0),
            BaseAttribute(VanillaAttributeTypes.ATTACK_SPEED, 1.2)
        )
    );

    override val slot = EquipmentSlot.HAND
    override val slotDependentAttributes = false

    override val rollableCustomAttributes = listOf(
        RandomOption(10, RollableCustomAttribute(VanillaAttributeTypes.ATTACK_DAMAGE, DoubleBounds(5.0))),
        RandomOption(10, RollableCustomAttribute(VanillaAttributeTypes.ATTACK_SPEED, DoubleBounds(0.4))),
        RandomOption(10, RollableCustomAttribute(VanillaAttributeTypes.ATTACK_KNOCKBACK, DoubleBounds(0.5))),
        RandomOption(10, RollableCustomAttribute(VanillaAttributeTypes.LUCK, DoubleBounds(2.5))),
        RandomOption(5, RollableCustomAttribute(CustomAttributeTypes.INCREASED_DAMAGE, DoubleBounds(0.15)))
    )

    override val rollableEnchants = listOf(
        RandomOption(10, ItemEnchantment.MENDING),
        RandomOption(20, ItemEnchantment.UNBREAKING_1),
        RandomOption(15, ItemEnchantment.UNBREAKING_2),
        RandomOption(10, ItemEnchantment.UNBREAKING_3),
        RandomOption(0, ItemEnchantment.CURSE_OF_VANISHING),
        RandomOption(25, ItemEnchantment.EFFICIENCY_1),
        RandomOption(20, ItemEnchantment.EFFICIENCY_2),
        RandomOption(15, ItemEnchantment.EFFICIENCY_3),
        RandomOption(10, ItemEnchantment.EFFICIENCY_4),
        RandomOption(5, ItemEnchantment.EFFICIENCY_5),
        RandomOption(10, ItemEnchantment.FORTUNE_1),
        RandomOption(6, ItemEnchantment.FORTUNE_2),
        RandomOption(3, ItemEnchantment.FORTUNE_3),
        RandomOption(15, ItemEnchantment.SILK_TOUCH),
        RandomOption(25, ItemEnchantment.SHARPNESS_1),
        RandomOption(20, ItemEnchantment.SHARPNESS_2),
        RandomOption(15, ItemEnchantment.SHARPNESS_3),
        RandomOption(10, ItemEnchantment.SHARPNESS_4),
        RandomOption(5, ItemEnchantment.SHARPNESS_5),
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
    )
}