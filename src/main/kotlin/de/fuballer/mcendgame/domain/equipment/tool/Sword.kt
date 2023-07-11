package de.fuballer.mcendgame.domain.equipment.tool

import de.fuballer.mcendgame.domain.equipment.Equipment
import de.fuballer.mcendgame.domain.equipment.ItemAttribute
import de.fuballer.mcendgame.domain.equipment.ItemEnchantment
import de.fuballer.mcendgame.random.RandomOption
import org.bukkit.Material
import org.bukkit.attribute.Attribute

enum class Sword(
    override val material: Material,
    override val baseAttributes: List<ItemAttribute>
) : Equipment {
    WOODEN(
        Material.WOODEN_SWORD,
        listOf(
            ItemAttribute(Attribute.GENERIC_ATTACK_DAMAGE, 4.0),
            ItemAttribute(Attribute.GENERIC_ATTACK_SPEED, 1.6)
        )
    ),
    GOLDEN(
        Material.GOLDEN_SWORD,
        listOf(
            ItemAttribute(Attribute.GENERIC_ATTACK_DAMAGE, 4.0),
            ItemAttribute(Attribute.GENERIC_ATTACK_SPEED, 1.6)
        )
    ),
    STONE(
        Material.STONE_SWORD,
        listOf(
            ItemAttribute(Attribute.GENERIC_ATTACK_DAMAGE, 5.0),
            ItemAttribute(Attribute.GENERIC_ATTACK_SPEED, 1.6)
        )
    ),
    IRON(
        Material.IRON_SWORD,
        listOf(
            ItemAttribute(Attribute.GENERIC_ATTACK_DAMAGE, 6.0),
            ItemAttribute(Attribute.GENERIC_ATTACK_SPEED, 1.6)
        )
    ),
    DIAMOND(
        Material.DIAMOND_SWORD,
        listOf(
            ItemAttribute(Attribute.GENERIC_ATTACK_DAMAGE, 7.0),
            ItemAttribute(Attribute.GENERIC_ATTACK_SPEED, 1.6)
        )
    ),
    NETHERITE(
        Material.NETHERITE_SWORD,
        listOf(
            ItemAttribute(Attribute.GENERIC_ATTACK_DAMAGE, 8.0),
            ItemAttribute(Attribute.GENERIC_ATTACK_SPEED, 1.6)
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
        RandomOption(15, ItemEnchantment.BANE_OF_ARTHROPODS_1),
        RandomOption(12, ItemEnchantment.BANE_OF_ARTHROPODS_2),
        RandomOption(9, ItemEnchantment.BANE_OF_ARTHROPODS_3),
        RandomOption(6, ItemEnchantment.BANE_OF_ARTHROPODS_4),
        RandomOption(3, ItemEnchantment.BANE_OF_ARTHROPODS_5),
        RandomOption(10, ItemEnchantment.FIRE_ASPECT_1),
        RandomOption(5, ItemEnchantment.FIRE_ASPECT_2),
        RandomOption(15, ItemEnchantment.LOOTING_1),
        RandomOption(10, ItemEnchantment.LOOTING_2),
        RandomOption(5, ItemEnchantment.LOOTING_3),
        RandomOption(10, ItemEnchantment.KNOCKBACK_1),
        RandomOption(5, ItemEnchantment.KNOCKBACK_2),
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
        RandomOption(15, ItemEnchantment.SWEEPING_EDGE_1),
        RandomOption(10, ItemEnchantment.SWEEPING_EDGE_2),
        RandomOption(5, ItemEnchantment.SWEEPING_EDGE_3),
    )
}
