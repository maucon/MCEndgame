package de.fuballer.mcendgame.domain.equipment.armor

import de.fuballer.mcendgame.domain.attribute.AttributeType
import de.fuballer.mcendgame.domain.attribute.RollableAttribute
import de.fuballer.mcendgame.domain.attribute.RolledAttribute
import de.fuballer.mcendgame.domain.equipment.Equipment
import de.fuballer.mcendgame.domain.equipment.ItemEnchantment
import de.fuballer.mcendgame.util.random.RandomOption
import org.bukkit.Material

enum class Helmet(
    override val material: Material,
    override val baseAttributes: List<RolledAttribute>
) : Equipment {
    LEATHER(
        Material.LEATHER_HELMET,
        listOf(
            RolledAttribute(AttributeType.ARMOR, 1.0)
        )
    ),
    GOLDEN(
        Material.GOLDEN_HELMET,
        listOf(
            RolledAttribute(AttributeType.ARMOR, 2.0)
        )
    ),
    CHAINMAIL(
        Material.CHAINMAIL_HELMET,
        listOf(
            RolledAttribute(AttributeType.ARMOR, 2.0)
        )
    ),
    IRON(
        Material.IRON_HELMET,
        listOf(
            RolledAttribute(AttributeType.ARMOR, 2.0)
        )
    ),
    TURTLE(
        Material.TURTLE_HELMET,
        listOf(
            RolledAttribute(AttributeType.ARMOR, 2.0)
        )
    ),
    DIAMOND(
        Material.DIAMOND_HELMET,
        listOf(
            RolledAttribute(AttributeType.ARMOR, 3.0),
            RolledAttribute(AttributeType.ARMOR_TOUGHNESS, 2.0)
        )
    ),
    NETHERITE(
        Material.NETHERITE_HELMET,
        listOf(
            RolledAttribute(AttributeType.ARMOR, 3.0),
            RolledAttribute(AttributeType.ARMOR_TOUGHNESS, 3.0),
            RolledAttribute(AttributeType.KNOCKBACK_RESISTANCE, 0.1)
        )
    );

    override val lore = Equipment.HEAD_SLOT_LORE

    override val rollableAttributes = listOf(
        RandomOption(10, RollableAttribute(AttributeType.MAX_HEALTH, 2.5)),
        RandomOption(10, RollableAttribute(AttributeType.ARMOR, 2.0)),
        RandomOption(10, RollableAttribute(AttributeType.ARMOR_TOUGHNESS, 2.5)),
        RandomOption(10, RollableAttribute(AttributeType.KNOCKBACK_RESISTANCE, 0.1))
    )

    override val rollableEnchants = listOf(
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
        RandomOption(35, ItemEnchantment.AQUA_AFFINITY_1),
        RandomOption(30, ItemEnchantment.RESPIRATION_1),
        RandomOption(20, ItemEnchantment.RESPIRATION_2),
        RandomOption(10, ItemEnchantment.RESPIRATION_3),
    )
}
