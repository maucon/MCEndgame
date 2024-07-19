package de.fuballer.mcendgame.component.item.equipment.armor

import de.fuballer.mcendgame.component.item.attribute.AttributeType
import de.fuballer.mcendgame.component.item.attribute.data.BaseAttribute
import de.fuballer.mcendgame.component.item.attribute.data.RollableAttribute
import de.fuballer.mcendgame.component.item.equipment.Equipment
import de.fuballer.mcendgame.component.item.equipment.ItemEnchantment
import de.fuballer.mcendgame.util.random.RandomOption
import org.bukkit.Material
import org.bukkit.inventory.EquipmentSlot

enum class Chestplate(
    override val material: Material,
    override val baseAttributes: List<BaseAttribute>
) : Equipment {
    LEATHER(
        Material.LEATHER_CHESTPLATE,
        listOf(
            BaseAttribute(AttributeType.ARMOR, 3.0)
        )
    ),
    GOLDEN(
        Material.GOLDEN_CHESTPLATE,
        listOf(
            BaseAttribute(AttributeType.ARMOR, 5.0)
        )
    ),
    CHAINMAIL(
        Material.CHAINMAIL_CHESTPLATE,
        listOf(
            BaseAttribute(AttributeType.ARMOR, 5.0)
        )
    ),
    IRON(
        Material.IRON_CHESTPLATE,
        listOf(
            BaseAttribute(AttributeType.ARMOR, 6.0)
        )
    ),
    DIAMOND(
        Material.DIAMOND_CHESTPLATE,
        listOf(
            BaseAttribute(AttributeType.ARMOR, 8.0),
            BaseAttribute(AttributeType.ARMOR_TOUGHNESS, 2.0)
        )
    ),
    NETHERITE(
        Material.NETHERITE_CHESTPLATE,
        listOf(
            BaseAttribute(AttributeType.ARMOR, 8.0),
            BaseAttribute(AttributeType.ARMOR_TOUGHNESS, 3.0),
            BaseAttribute(AttributeType.KNOCKBACK_RESISTANCE, 0.1)
        )
    );

    override val slot = EquipmentSlot.CHEST
    override val slotDependentAttributes = true

    override val rollableAttributes = listOf(
        RandomOption(10, RollableAttribute(AttributeType.MAX_HEALTH, 2.0)),
        RandomOption(10, RollableAttribute(AttributeType.ARMOR, 2.0)),
        RandomOption(10, RollableAttribute(AttributeType.ARMOR_TOUGHNESS, 2.5)),
        RandomOption(10, RollableAttribute(AttributeType.KNOCKBACK_RESISTANCE, 0.1)),
        RandomOption(8, RollableAttribute(AttributeType.REDUCED_DAMAGE_TAKEN, 0.04)),
        RandomOption(5, RollableAttribute(AttributeType.INCREASED_DAMAGE, 0.05)),
        RandomOption(7, RollableAttribute(AttributeType.DODGE_CHANCE, 0.07))
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
        RandomOption(3, ItemEnchantment.THORNS_1),
        RandomOption(2, ItemEnchantment.THORNS_2),
        RandomOption(1, ItemEnchantment.THORNS_3),
    )
}
