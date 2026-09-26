package de.fuballer.mcendgame.main.component.entity.custom.entities.bandit

import de.fuballer.mcendgame.main.util.extension.ItemStackExtension.setTrim
import net.minecraft.core.RegistryAccess
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.DyedItemColor
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.equipment.trim.TrimMaterial
import net.minecraft.world.item.equipment.trim.TrimPattern

data class BanditItemStack(
    val slot: EquipmentSlot,
    private val baseStack: ItemStack,
    private val enchantments: Map<ResourceKey<Enchantment>, Int> = mapOf(),
    private val trimMaterial: ResourceKey<TrimMaterial>? = null,
    private val trimPattern: ResourceKey<TrimPattern>? = null,
    private val dyedColor: DyedItemColor? = null,
) {
    fun getStack(bandit: BanditEntity): ItemStack {
        val stack = baseStack.copy()

        val registryAccess = bandit.registryAccess()
        applyForcedEnchantments(registryAccess, stack)
        if (trimMaterial != null && trimPattern != null) stack.setTrim(registryAccess, trimMaterial, trimPattern)
        dyedColor?.let { stack.set(DataComponents.DYED_COLOR, it) }

        return stack
    }

    private fun applyForcedEnchantments(
        registryAccess: RegistryAccess,
        stack: ItemStack,
    ) {
        if (enchantments.isEmpty()) return
        val enchantmentRegistry = registryAccess.lookupOrThrow(Registries.ENCHANTMENT)

        enchantments.forEach { (enchantmentKey, level) ->
            val enchantmentHolder = enchantmentRegistry.getOrThrow(enchantmentKey)
            stack.enchant(enchantmentHolder, level)
        }
    }
}