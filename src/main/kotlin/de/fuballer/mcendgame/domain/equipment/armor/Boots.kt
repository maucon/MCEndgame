package de.fuballer.mcendgame.domain.equipment.armor

import de.fuballer.mcendgame.domain.attribute.AttributeType
import de.fuballer.mcendgame.domain.attribute.RollableAttribute
import de.fuballer.mcendgame.domain.attribute.RolledAttribute
import de.fuballer.mcendgame.domain.equipment.Equipment
import de.fuballer.mcendgame.domain.equipment.ItemEnchantment
import de.fuballer.mcendgame.util.random.RandomOption
import org.bukkit.Material
import org.bukkit.inventory.EquipmentSlot

enum class Boots(
    override val material: Material,
    override val baseAttributes: List<RolledAttribute>,
) : Equipment {
    LEATHER(
        Material.LEATHER_BOOTS,
        listOf(
            RolledAttribute(AttributeType.ARMOR, 1.0)
        )
    ),
    GOLDEN(
        Material.GOLDEN_BOOTS,
        listOf(
            RolledAttribute(AttributeType.ARMOR, 1.0)
        )
    ),
    CHAINMAIL(
        Material.CHAINMAIL_BOOTS,
        listOf(
            RolledAttribute(AttributeType.ARMOR, 1.0)
        )
    ),
    IRON(
        Material.IRON_BOOTS,
        listOf(
            RolledAttribute(AttributeType.ARMOR, 2.0)
        )
    ),
    DIAMOND(
        Material.DIAMOND_BOOTS,
        listOf(
            RolledAttribute(AttributeType.ARMOR, 3.0),
            RolledAttribute(AttributeType.ARMOR_TOUGHNESS, 2.0)
        )
    ),
    NETHERITE(
        Material.NETHERITE_BOOTS,
        listOf(
            RolledAttribute(AttributeType.ARMOR, 3.0),
            RolledAttribute(AttributeType.ARMOR_TOUGHNESS, 3.0),
            RolledAttribute(AttributeType.KNOCKBACK_RESISTANCE, 0.1)
        )
    );

    override val slot = EquipmentSlot.FEET
    override val extraAttributesInSlot = true

    override val rollableAttributes = listOf(
        RandomOption(10, RollableAttribute(AttributeType.MAX_HEALTH, 2.0)),
        RandomOption(10, RollableAttribute(AttributeType.ARMOR, 1.5)),
        RandomOption(10, RollableAttribute(AttributeType.ARMOR_TOUGHNESS, 2.0)),
        RandomOption(10, RollableAttribute(AttributeType.KNOCKBACK_RESISTANCE, 0.1)),
        RandomOption(10, RollableAttribute(AttributeType.MOVEMENT_SPEED, 0.03))
    )

    override val rollableEnchants = listOf(
        RandomOption(20, ItemEnchantment.MENDING),
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
        RandomOption(25, ItemEnchantment.DEPTH_STRIDER_1),
        RandomOption(15, ItemEnchantment.DEPTH_STRIDER_2),
        RandomOption(5, ItemEnchantment.DEPTH_STRIDER_3),
        RandomOption(20, ItemEnchantment.FEATHER_FALLING_1),
        RandomOption(15, ItemEnchantment.FEATHER_FALLING_2),
        RandomOption(10, ItemEnchantment.FEATHER_FALLING_3),
        RandomOption(5, ItemEnchantment.FEATHER_FALLING_4),
        RandomOption(20, ItemEnchantment.FROST_WALKER_1),
        RandomOption(10, ItemEnchantment.FROST_WALKER_2),
        RandomOption(25, ItemEnchantment.SOUL_SPEED_1),
        RandomOption(15, ItemEnchantment.SOUL_SPEED_2),
        RandomOption(5, ItemEnchantment.SOUL_SPEED_3),
    )
}
