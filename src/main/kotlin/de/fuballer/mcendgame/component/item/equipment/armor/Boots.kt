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

enum class Boots(
    override val material: Material,
    override val baseAttributes: List<BaseAttribute>,
) : Equipment {
    LEATHER(
        Material.LEATHER_BOOTS,
        listOf(
            BaseAttribute(VanillaAttributeTypes.ARMOR, 1.0)
        )
    ),
    GOLDEN(
        Material.GOLDEN_BOOTS,
        listOf(
            BaseAttribute(VanillaAttributeTypes.ARMOR, 1.0)
        )
    ),
    CHAINMAIL(
        Material.CHAINMAIL_BOOTS,
        listOf(
            BaseAttribute(VanillaAttributeTypes.ARMOR, 1.0)
        )
    ),
    IRON(
        Material.IRON_BOOTS,
        listOf(
            BaseAttribute(VanillaAttributeTypes.ARMOR, 2.0)
        )
    ),
    DIAMOND(
        Material.DIAMOND_BOOTS,
        listOf(
            BaseAttribute(VanillaAttributeTypes.ARMOR, 3.0),
            BaseAttribute(VanillaAttributeTypes.ARMOR_TOUGHNESS, 2.0)
        )
    ),
    NETHERITE(
        Material.NETHERITE_BOOTS,
        listOf(
            BaseAttribute(VanillaAttributeTypes.ARMOR, 3.0),
            BaseAttribute(VanillaAttributeTypes.ARMOR_TOUGHNESS, 3.0),
            BaseAttribute(VanillaAttributeTypes.KNOCKBACK_RESISTANCE, 0.1)
        )
    );

    override val slot = EquipmentSlot.FEET
    override val slotDependentAttributes = true

    override val rollableCustomAttributes = listOf(
        RandomOption(10, RollableCustomAttribute(VanillaAttributeTypes.MAX_HEALTH, DoubleBounds(1.0, 2.5))),
        RandomOption(10, RollableCustomAttribute(VanillaAttributeTypes.ARMOR, DoubleBounds(0.5, 1.5))),
        RandomOption(10, RollableCustomAttribute(VanillaAttributeTypes.ARMOR_TOUGHNESS, DoubleBounds(0.5, 2.0))),
        RandomOption(10, RollableCustomAttribute(VanillaAttributeTypes.KNOCKBACK_RESISTANCE, DoubleBounds(0.02, 0.1))),
        RandomOption(10, RollableCustomAttribute(VanillaAttributeTypes.MOVEMENT_SPEED, DoubleBounds(0.003, 0.015))),
        RandomOption(8, RollableCustomAttribute(CustomAttributeTypes.REDUCED_DAMAGE_TAKEN, DoubleBounds(0.005, 0.03))),
        RandomOption(8, RollableCustomAttribute(CustomAttributeTypes.REDUCED_PROJECTILE_DAMAGE_TAKEN, DoubleBounds(0.01, 0.07))),
        RandomOption(5, RollableCustomAttribute(CustomAttributeTypes.INCREASED_DAMAGE, DoubleBounds(0.01, 0.05))),
        RandomOption(7, RollableCustomAttribute(CustomAttributeTypes.DODGE_CHANCE, DoubleBounds(0.02, 0.1))),
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
        RandomOption(3, ItemEnchantment.THORNS_1),
        RandomOption(2, ItemEnchantment.THORNS_2),
        RandomOption(1, ItemEnchantment.THORNS_3),
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