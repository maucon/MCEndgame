package de.fuballer.mcendgame.component.item.attribute.effects.summon_suport_wolf

import org.bukkit.Color
import org.bukkit.DyeColor
import org.bukkit.Material
import org.bukkit.entity.Wolf
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ColorableArmorMeta

enum class SupportWolfType(
    private val stringName: String,
    private val variant: Wolf.Variant,
    private val collarColor: DyeColor,
    private val armorColor: Color,
) {
    SLOWING("Slowing", Wolf.Variant.SNOWY, DyeColor.LIGHT_BLUE, Color.WHITE),
    LIFE_STEALING("Life Stealing", Wolf.Variant.RUSTY, DyeColor.ORANGE, Color.ORANGE),
    WEAKENING("Weakening", Wolf.Variant.WOODS, DyeColor.GREEN, Color.GREEN),
    INCITING("Inciting", Wolf.Variant.BLACK, DyeColor.RED, Color.RED),
    HASTING("Hasting", Wolf.Variant.STRIPED, DyeColor.YELLOW, Color.YELLOW),
    ;

    fun getVariant() = variant

    fun getCollarColor() = collarColor

    fun getArmor(): ItemStack {
        val armor = ItemStack(Material.WOLF_ARMOR)

        val meta = armor.itemMeta as ColorableArmorMeta
        meta.setColor(armorColor)
        armor.itemMeta = meta

        return armor
    }

    companion object {
        fun getByString(string: String): SupportWolfType = entries.first { it.stringName == string }

        fun getAsStringList() = entries.map { it.stringName }
    }
}