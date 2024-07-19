package de.fuballer.mcendgame.component.item.equipment.armor

import de.fuballer.mcendgame.component.item.attribute.AttributeType
import de.fuballer.mcendgame.component.item.attribute.data.BaseAttribute
import de.fuballer.mcendgame.component.item.attribute.data.RollableAttribute
import de.fuballer.mcendgame.component.item.equipment.Equipment
import de.fuballer.mcendgame.component.item.equipment.ItemEnchantment
import de.fuballer.mcendgame.util.random.RandomOption
import org.bukkit.Material
import org.bukkit.inventory.EquipmentSlot

enum class Helmet(
    override val material: Material,
    override val baseAttributes: List<BaseAttribute>
) : Equipment {
    LEATHER(
        Material.LEATHER_HELMET,
        listOf(
            BaseAttribute(AttributeType.ARMOR, 1.0)
        )
    ),
    GOLDEN(
        Material.GOLDEN_HELMET,
        listOf(
            BaseAttribute(AttributeType.ARMOR, 2.0)
        )
    ),
    CHAINMAIL(
        Material.CHAINMAIL_HELMET,
        listOf(
            BaseAttribute(AttributeType.ARMOR, 2.0)
        )
    ),
    IRON(
        Material.IRON_HELMET,
        listOf(
            BaseAttribute(AttributeType.ARMOR, 2.0)
        )
    ),
    TURTLE(
        Material.TURTLE_HELMET,
        listOf(
            BaseAttribute(AttributeType.ARMOR, 2.0)
        )
    ),
    DIAMOND(
        Material.DIAMOND_HELMET,
        listOf(
            BaseAttribute(AttributeType.ARMOR, 3.0),
            BaseAttribute(AttributeType.ARMOR_TOUGHNESS, 2.0)
        )
    ),
    NETHERITE(
        Material.NETHERITE_HELMET,
        listOf(
            BaseAttribute(AttributeType.ARMOR, 3.0),
            BaseAttribute(AttributeType.ARMOR_TOUGHNESS, 3.0),
            BaseAttribute(AttributeType.KNOCKBACK_RESISTANCE, 0.1)
        )
    );

    override val slot = EquipmentSlot.HEAD
    override val slotDependentAttributes = true

    override val rollableAttributes = listOf(
        RandomOption(10, RollableAttribute(AttributeType.MAX_HEALTH, 2.0)),
        RandomOption(10, RollableAttribute(AttributeType.ARMOR, 2.0)),
        RandomOption(10, RollableAttribute(AttributeType.ARMOR_TOUGHNESS, 2.5)),
        RandomOption(10, RollableAttribute(AttributeType.KNOCKBACK_RESISTANCE, 0.1)),
        RandomOption(8, RollableAttribute(AttributeType.REDUCED_DAMAGE_TAKEN, 0.03)),
        RandomOption(5, RollableAttribute(AttributeType.INCREASED_DAMAGE, 0.05)),
        RandomOption(7, RollableAttribute(AttributeType.DODGE_CHANCE, 0.05))
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
        RandomOption(35, ItemEnchantment.AQUA_AFFINITY_1),
        RandomOption(30, ItemEnchantment.RESPIRATION_1),
        RandomOption(20, ItemEnchantment.RESPIRATION_2),
        RandomOption(10, ItemEnchantment.RESPIRATION_3),
    )
}
