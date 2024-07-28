package de.fuballer.mcendgame.component.item.equipment.armor

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

enum class Leggings(
    override val material: Material,
    override val baseAttributes: List<BaseAttribute>
) : Equipment {
    LEATHER(
        Material.LEATHER_LEGGINGS,
        listOf(
            BaseAttribute(VanillaAttributeTypes.ARMOR, 2.0)
        )
    ),
    GOLDEN(
        Material.GOLDEN_LEGGINGS,
        listOf(
            BaseAttribute(VanillaAttributeTypes.ARMOR, 3.0)
        )
    ),
    CHAINMAIL(
        Material.CHAINMAIL_LEGGINGS,
        listOf(
            BaseAttribute(VanillaAttributeTypes.ARMOR, 4.0)
        )
    ),
    IRON(
        Material.IRON_LEGGINGS,
        listOf(
            BaseAttribute(VanillaAttributeTypes.ARMOR, 5.0)
        )
    ),
    DIAMOND(
        Material.DIAMOND_LEGGINGS,
        listOf(
            BaseAttribute(VanillaAttributeTypes.ARMOR, 6.0),
            BaseAttribute(VanillaAttributeTypes.ARMOR_TOUGHNESS, 2.0)
        )
    ),
    NETHERITE(
        Material.NETHERITE_LEGGINGS,
        listOf(
            BaseAttribute(VanillaAttributeTypes.ARMOR, 6.0),
            BaseAttribute(VanillaAttributeTypes.ARMOR_TOUGHNESS, 3.0),
            BaseAttribute(VanillaAttributeTypes.KNOCKBACK_RESISTANCE, 0.1)
        )
    );

    override val slot = EquipmentSlot.LEGS
    override val slotDependentAttributes = true

    val rollableCustomAttributes = listOf(
        RandomOption(10, RollableCustomAttribute(VanillaAttributeTypes.MAX_HEALTH, DoubleBounds(2.0))),
        RandomOption(10, RollableCustomAttribute(VanillaAttributeTypes.ARMOR, DoubleBounds(2.0))),
        RandomOption(10, RollableCustomAttribute(VanillaAttributeTypes.ARMOR_TOUGHNESS, DoubleBounds(2.5))),
        RandomOption(10, RollableCustomAttribute(VanillaAttributeTypes.KNOCKBACK_RESISTANCE, DoubleBounds(0.1))),
        RandomOption(8, RollableCustomAttribute(CustomAttributeTypes.REDUCED_DAMAGE_TAKEN, DoubleBounds(0.04))),
        RandomOption(5, RollableCustomAttribute(CustomAttributeTypes.INCREASED_DAMAGE, DoubleBounds(0.05))),
        RandomOption(7, RollableCustomAttribute(CustomAttributeTypes.DODGE_CHANCE, DoubleBounds(0.07)))
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
        RandomOption(28, ItemEnchantment.SWIFT_SNEAK_1),
        RandomOption(18, ItemEnchantment.SWIFT_SNEAK_2),
        RandomOption(8, ItemEnchantment.SWIFT_SNEAK_3),
    )
}