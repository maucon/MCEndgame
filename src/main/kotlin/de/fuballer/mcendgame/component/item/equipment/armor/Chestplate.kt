package de.fuballer.mcendgame.component.item.equipment.armor

import de.fuballer.mcendgame.component.attribute.AttributeType
import de.fuballer.mcendgame.component.attribute.RollableAttribute
import de.fuballer.mcendgame.component.attribute.RolledAttribute
import de.fuballer.mcendgame.component.item.equipment.Equipment
import de.fuballer.mcendgame.component.item.equipment.ItemEnchantment
import de.fuballer.mcendgame.util.random.RandomOption
import org.bukkit.Material
import org.bukkit.inventory.EquipmentSlot

enum class Chestplate(
    override val material: Material,
    override val baseAttributes: List<RolledAttribute>
) : Equipment {
    LEATHER(
        Material.LEATHER_CHESTPLATE,
        listOf(
            RolledAttribute(AttributeType.ARMOR, 3.0)
        )
    ),
    GOLDEN(
        Material.GOLDEN_CHESTPLATE,
        listOf(
            RolledAttribute(AttributeType.ARMOR, 5.0)
        )
    ),
    CHAINMAIL(
        Material.CHAINMAIL_CHESTPLATE,
        listOf(
            RolledAttribute(AttributeType.ARMOR, 5.0)
        )
    ),
    IRON(
        Material.IRON_CHESTPLATE,
        listOf(
            RolledAttribute(AttributeType.ARMOR, 6.0)
        )
    ),
    DIAMOND(
        Material.DIAMOND_CHESTPLATE,
        listOf(
            RolledAttribute(AttributeType.ARMOR, 8.0),
            RolledAttribute(AttributeType.ARMOR_TOUGHNESS, 2.0)
        )
    ),
    NETHERITE(
        Material.NETHERITE_CHESTPLATE,
        listOf(
            RolledAttribute(AttributeType.ARMOR, 8.0),
            RolledAttribute(AttributeType.ARMOR_TOUGHNESS, 3.0),
            RolledAttribute(AttributeType.KNOCKBACK_RESISTANCE, 0.1)
        )
    );

    override val slot = EquipmentSlot.CHEST
    override val extraAttributesInSlot = true

    override val rollableAttributes = listOf(
        RandomOption(10, RollableAttribute(AttributeType.MAX_HEALTH, 2.0)),
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
    )
}
