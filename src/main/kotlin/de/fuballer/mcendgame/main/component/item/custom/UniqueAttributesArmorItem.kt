package de.fuballer.mcendgame.main.component.item.custom

import net.minecraft.item.ArmorItem
import net.minecraft.item.ArmorMaterial
import net.minecraft.item.ItemStack
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.text.MutableText

abstract class UniqueAttributesArmorItem(
    material: RegistryEntry<ArmorMaterial>,
    type: Type,
    val settings: Settings,
) : ArmorItem(material, type, settings), UniqueAttributesItemInterface {
    override fun getDefaultStack() = getRolledStack(this, true)

    override fun getName(stack: ItemStack): MutableText = super.getName(stack).copy().withColor(getNameColor())
}