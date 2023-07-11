package de.fuballer.mcendgame.domain.equipment.armor

import de.fuballer.mcendgame.domain.equipment.Equipment
import de.fuballer.mcendgame.domain.equipment.ItemAttribute
import de.fuballer.mcendgame.domain.equipment.ItemEnchantment
import de.fuballer.mcendgame.random.RandomOption
import org.bukkit.Material
import org.bukkit.attribute.Attribute

enum class Leggings(
    override val material: Material,
    override val baseAttributes: List<ItemAttribute>
) : Equipment {
    LEATHER(
        Material.LEATHER_LEGGINGS,
        listOf(
            ItemAttribute(Attribute.GENERIC_ARMOR, 2.0)
        )
    ),
    GOLDEN(
        Material.GOLDEN_LEGGINGS,
        listOf(
            ItemAttribute(Attribute.GENERIC_ARMOR, 3.0)
        )
    ),
    CHAINMAIL(
        Material.CHAINMAIL_LEGGINGS,
        listOf(
            ItemAttribute(Attribute.GENERIC_ARMOR, 4.0)
        )
    ),
    IRON(
        Material.IRON_LEGGINGS,
        listOf(
            ItemAttribute(Attribute.GENERIC_ARMOR, 5.0)
        )
    ),
    DIAMOND(
        Material.DIAMOND_LEGGINGS,
        listOf(
            ItemAttribute(Attribute.GENERIC_ARMOR, 6.0),
            ItemAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS, 2.0)
        )
    ),
    NETHERITE(
        Material.NETHERITE_LEGGINGS,
        listOf(
            ItemAttribute(Attribute.GENERIC_ARMOR, 6.0),
            ItemAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS, 3.0),
            ItemAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE, 0.1)
        )
    );

    override val lore = Equipment.LEGS_SLOT_LORE

    override val rolledAttributes = listOf(
        RandomOption(10, ItemAttribute(Attribute.GENERIC_MAX_HEALTH, 2.5)),
        RandomOption(10, ItemAttribute(Attribute.GENERIC_ARMOR, 2.0)),
        RandomOption(10, ItemAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS, 2.5)),
        RandomOption(10, ItemAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE, 0.1))
    )

    override val enchantOptions = listOf(
        RandomOption(10, ItemEnchantment.MENDING),
        RandomOption(35, ItemEnchantment.UNBREAKING_1),
        RandomOption(22, ItemEnchantment.UNBREAKING_2),
        RandomOption(11, ItemEnchantment.UNBREAKING_3),
        RandomOption(0, ItemEnchantment.CURSE_OF_VANISHING),
        RandomOption(20, ItemEnchantment.BLAST_PROTECTION_1),
        RandomOption(15, ItemEnchantment.BLAST_PROTECTION_2),
        RandomOption(10, ItemEnchantment.BLAST_PROTECTION_3),
        RandomOption(5, ItemEnchantment.BLAST_PROTECTION_4),
        RandomOption(0, ItemEnchantment.CURSE_OF_BINDING),
        RandomOption(20, ItemEnchantment.FIRE_PROTECTION_1),
        RandomOption(15, ItemEnchantment.FIRE_PROTECTION_2),
        RandomOption(10, ItemEnchantment.FIRE_PROTECTION_3),
        RandomOption(5, ItemEnchantment.FIRE_PROTECTION_4),
        RandomOption(20, ItemEnchantment.PROJECTILE_PROTECTION_1),
        RandomOption(15, ItemEnchantment.PROJECTILE_PROTECTION_2),
        RandomOption(10, ItemEnchantment.PROJECTILE_PROTECTION_3),
        RandomOption(5, ItemEnchantment.PROJECTILE_PROTECTION_4),
        RandomOption(40, ItemEnchantment.PROTECTION_1),
        RandomOption(30, ItemEnchantment.PROTECTION_2),
        RandomOption(20, ItemEnchantment.PROTECTION_3),
        RandomOption(10, ItemEnchantment.PROTECTION_4),
        RandomOption(8, ItemEnchantment.THORNS_1),
        RandomOption(6, ItemEnchantment.THORNS_2),
        RandomOption(4, ItemEnchantment.THORNS_3),
        RandomOption(28, ItemEnchantment.SWIFT_SNEAK_1),
        RandomOption(18, ItemEnchantment.SWIFT_SNEAK_2),
        RandomOption(8, ItemEnchantment.SWIFT_SNEAK_3),
    )
}
