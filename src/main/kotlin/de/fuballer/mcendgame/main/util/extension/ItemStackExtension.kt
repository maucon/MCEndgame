package de.fuballer.mcendgame.main.util.extension

import de.fuballer.mcendgame.main.component.item.equipment.Equipment
import net.minecraft.core.RegistryAccess
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.equipment.trim.ArmorTrim
import net.minecraft.world.item.equipment.trim.TrimMaterial
import net.minecraft.world.item.equipment.trim.TrimPattern

object ItemStackExtension {
    fun ItemStack.isSameIgnoringDurability(other: ItemStack): Boolean {
        if (!`is`(other.item)) return false
        val ownComponents = components.filter { it != DataComponents.DAMAGE }.associateBy { it.type }
        val otherComponents = other.components.filter { it != DataComponents.DAMAGE }.associateBy { it.type }
        return ownComponents == otherComponents
    }

    fun ItemStack.isForgeable() = Equipment.fromItem(item) != null

    fun ItemStack.setTrim(
        access: RegistryAccess,
        material: ResourceKey<TrimMaterial>,
        pattern: ResourceKey<TrimPattern>,
    ) {
        val trimMaterials = access.lookupOrThrow(Registries.TRIM_MATERIAL)
        val trimPatterns = access.lookupOrThrow(Registries.TRIM_PATTERN)
        set(
            DataComponents.TRIM,
            ArmorTrim(
                trimMaterials.getOrThrow(material),
                trimPatterns.getOrThrow(pattern),
            )
        )
    }
}