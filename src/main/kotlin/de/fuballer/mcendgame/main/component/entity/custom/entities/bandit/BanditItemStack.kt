package de.fuballer.mcendgame.main.component.entity.custom.entities.bandit

import de.fuballer.mcendgame.main.util.extension.ItemStackExtension.setTrim
import net.minecraft.resources.ResourceKey
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.equipment.trim.TrimMaterial
import net.minecraft.world.item.equipment.trim.TrimPattern

data class BanditItemStack(
    private val slot: EquipmentSlot,
    private val baseStack: ItemStack,
    private val trimMaterial: ResourceKey<TrimMaterial>? = null,
    private val trimPattern: ResourceKey<TrimPattern>? = null,
) {
    fun equip(bandit: BanditEntity) {
        val stack = baseStack.copy()

        if (trimMaterial != null && trimPattern != null) {
            val registryAccess = bandit.registryAccess()
            stack.setTrim(registryAccess, trimMaterial, trimPattern)
        }

        bandit.setItemSlot(slot, stack)
    }
}