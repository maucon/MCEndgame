package de.fuballer.mcendgame.domain.equipment.tool

import de.fuballer.mcendgame.data_class.ItemAttribute
import de.fuballer.mcendgame.domain.equipment.Equipment
import de.fuballer.mcendgame.domain.equipment.ItemEnchantment
import de.fuballer.mcendgame.util.random.RandomOption
import org.bukkit.Material
import org.bukkit.attribute.Attribute

enum class Pickaxe(
    override val material: Material,
    override val baseAttributes: List<ItemAttribute>
) : Equipment {
    WOODEN(
        Material.WOODEN_PICKAXE,
        listOf(
            ItemAttribute(Attribute.GENERIC_ATTACK_DAMAGE, 2.0),
            ItemAttribute(Attribute.GENERIC_ATTACK_SPEED, 1.2)
        )
    ),
    GOLDEN(
        Material.GOLDEN_PICKAXE,
        listOf(
            ItemAttribute(Attribute.GENERIC_ATTACK_DAMAGE, 2.0),
            ItemAttribute(Attribute.GENERIC_ATTACK_SPEED, 1.2)
        )
    ),
    STONE(
        Material.STONE_PICKAXE,
        listOf(
            ItemAttribute(Attribute.GENERIC_ATTACK_DAMAGE, 3.0),
            ItemAttribute(Attribute.GENERIC_ATTACK_SPEED, 1.2)
        )
    ),
    IRON(
        Material.IRON_PICKAXE,
        listOf(
            ItemAttribute(Attribute.GENERIC_ATTACK_DAMAGE, 4.0),
            ItemAttribute(Attribute.GENERIC_ATTACK_SPEED, 1.2)
        )
    ),
    DIAMOND(
        Material.DIAMOND_PICKAXE,
        listOf(
            ItemAttribute(Attribute.GENERIC_ATTACK_DAMAGE, 5.0),
            ItemAttribute(Attribute.GENERIC_ATTACK_SPEED, 1.2)
        )
    ),
    NETHERITE(
        Material.NETHERITE_PICKAXE,
        listOf(
            ItemAttribute(Attribute.GENERIC_ATTACK_DAMAGE, 6.0),
            ItemAttribute(Attribute.GENERIC_ATTACK_SPEED, 1.2)
        )
    );

    override val lore = Equipment.MAIN_HAND_SLOT_LORE

    override val rolledAttributes = listOf(
        RandomOption(10, ItemAttribute(Attribute.GENERIC_ATTACK_DAMAGE, 1.5)),
        RandomOption(10, ItemAttribute(Attribute.GENERIC_ATTACK_SPEED, 0.4)),
        RandomOption(10, ItemAttribute(Attribute.GENERIC_ATTACK_KNOCKBACK, 0.5)),
        RandomOption(10, ItemAttribute(Attribute.GENERIC_MAX_HEALTH, 1.5)),
        RandomOption(10, ItemAttribute(Attribute.GENERIC_LUCK, 2.5))
    )

    override val enchantOptions = listOf(
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
