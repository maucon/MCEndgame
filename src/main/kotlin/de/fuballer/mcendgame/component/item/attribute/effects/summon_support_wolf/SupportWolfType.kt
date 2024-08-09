package de.fuballer.mcendgame.component.item.attribute.effects.summon_support_wolf

import de.fuballer.mcendgame.util.EntityUtil
import org.bukkit.Color
import org.bukkit.DyeColor
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Wolf
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ColorableArmorMeta

enum class SupportWolfType(
    private val stringName: String,
    private val variant: Wolf.Variant,
    private val collarColor: DyeColor,
    private val armorColor: Color,
    private val scale: Double = 1.0
) {
    SLOWING("Slowing", Wolf.Variant.SNOWY, DyeColor.LIGHT_BLUE, Color.WHITE, 1.05),
    LIFE_STEALING("Life Stealing", Wolf.Variant.RUSTY, DyeColor.ORANGE, Color.ORANGE),
    WEAKENING("Weakening", Wolf.Variant.WOODS, DyeColor.GREEN, Color.GREEN, 0.95),
    INCITING("Inciting", Wolf.Variant.BLACK, DyeColor.RED, Color.RED, 1.1),
    HASTING("Hasting", Wolf.Variant.STRIPED, DyeColor.YELLOW, Color.YELLOW, 0.9),
    ;

    fun updateWolf(wolf: Wolf) {
        wolf.variant = variant
        wolf.collarColor = collarColor

        val armor = ItemStack(Material.WOLF_ARMOR)

        val meta = armor.itemMeta as ColorableArmorMeta
        meta.setColor(armorColor)
        armor.itemMeta = meta

        wolf.equipment.setItem(EquipmentSlot.BODY, armor)
        EntityUtil.setAttribute(wolf, Attribute.GENERIC_SCALE, scale)

        wolf.isSilent = true
        wolf.isCollidable = false
        wolf.isInvulnerable = true
    }

    companion object {
        fun fromString(string: String) = entries.first { it.stringName == string }

        fun asStringList() = entries.map { it.stringName }
    }
}