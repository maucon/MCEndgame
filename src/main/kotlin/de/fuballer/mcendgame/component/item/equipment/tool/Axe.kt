package de.fuballer.mcendgame.component.item.equipment.tool

import de.fuballer.mcendgame.component.item.attribute.AttributeType
import de.fuballer.mcendgame.component.item.attribute.data.BaseAttribute
import de.fuballer.mcendgame.component.item.attribute.data.RollableAttribute
import de.fuballer.mcendgame.component.item.equipment.Equipment
import de.fuballer.mcendgame.component.item.equipment.ItemEnchantment
import de.fuballer.mcendgame.util.random.RandomOption
import org.bukkit.Material
import org.bukkit.inventory.EquipmentSlot

enum class Axe(
    override val material: Material,
    override val baseAttributes: List<BaseAttribute>
) : Equipment {
    WOODEN(
        Material.WOODEN_AXE,
        listOf(
            BaseAttribute(AttributeType.ATTACK_DAMAGE, 7.0),
            BaseAttribute(AttributeType.ATTACK_SPEED, 0.8)
        )
    ),
    GOLDEN(
        Material.GOLDEN_AXE,
        listOf(
            BaseAttribute(AttributeType.ATTACK_DAMAGE, 7.0),
            BaseAttribute(AttributeType.ATTACK_SPEED, 1.0)
        )
    ),
    STONE(
        Material.STONE_AXE,
        listOf(
            BaseAttribute(AttributeType.ATTACK_DAMAGE, 9.0),
            BaseAttribute(AttributeType.ATTACK_SPEED, 0.8)
        )
    ),
    IRON(
        Material.IRON_AXE,
        listOf(
            BaseAttribute(AttributeType.ATTACK_DAMAGE, 9.0),
            BaseAttribute(AttributeType.ATTACK_SPEED, 0.9)
        )
    ),
    DIAMOND(
        Material.DIAMOND_AXE,
        listOf(
            BaseAttribute(AttributeType.ATTACK_DAMAGE, 9.0),
            BaseAttribute(AttributeType.ATTACK_SPEED, 1.0)
        )
    ),
    NETHERITE(
        Material.NETHERITE_AXE,
        listOf(
            BaseAttribute(AttributeType.ATTACK_DAMAGE, 10.0),
            BaseAttribute(AttributeType.ATTACK_SPEED, 1.0)
        )
    );

    override val slot = EquipmentSlot.HAND
    override val slotDependentAttributes = false

    override val rollableAttributes = listOf(
        RandomOption(10, RollableAttribute(AttributeType.ATTACK_DAMAGE, 2.0)),
        RandomOption(10, RollableAttribute(AttributeType.ATTACK_SPEED, 0.4)),
        RandomOption(10, RollableAttribute(AttributeType.ATTACK_KNOCKBACK, 0.5)),
        RandomOption(10, RollableAttribute(AttributeType.LUCK, 2.5)),
        RandomOption(5, RollableAttribute(AttributeType.INCREASED_DAMAGE_DEALT, 0.08))
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
        RandomOption(15, ItemEnchantment.FIRE_ASPECT_1),
        RandomOption(7, ItemEnchantment.FIRE_ASPECT_2),
    )
}
