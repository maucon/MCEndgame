package de.fuballer.mcendgame.main.component.dungeon.enemy.equipment.enchantment

import de.fuballer.mcendgame.main.component.item.equipment.enchantment.EquipmentEnchantment
import de.fuballer.mcendgame.main.util.random.RandomOption
import de.fuballer.mcendgame.main.util.random.RandomUtil
import de.maucon.mauconframework.di.annotation.Injectable
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.item.ItemStack
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.server.MinecraftServer
import kotlin.random.Random

@Injectable
class EnchantmentService {
    fun enchantItem(
        itemStack: ItemStack,
        enchants: List<RandomOption<EquipmentEnchantment>>,
        level: Int,
        server: MinecraftServer,
        random: Random,
    ) {
        selectEnchantments(level, enchants, server, random).forEach {
            itemStack.addEnchantment(it.key, it.value)
        }
    }

    private fun selectEnchantments(
        level: Int,
        enchants: List<RandomOption<EquipmentEnchantment>>,
        server: MinecraftServer,
        random: Random,
    ): Map<RegistryEntry<Enchantment>, Int> {
        if (enchants.isEmpty()) return mapOf()

        val enchantmentRegistry = server.registryManager.getWrapperOrThrow(RegistryKeys.ENCHANTMENT)

        val pickedEnchantments = mutableMapOf<RegistryEntry<Enchantment>, Int>()
        repeat(EnchantmentSettings.calculateEnchantTries(level)) {
            val potentialEnchant = RandomUtil.pickOne(enchants, random).option
            val potentialEnchantEntry = enchantmentRegistry.getOrThrow(potentialEnchant.enchantment)

            val compatible = EnchantmentHelper.isCompatible(pickedEnchantments.keys, potentialEnchantEntry)
            if (!compatible) return@repeat

            pickedEnchantments[potentialEnchantEntry] = potentialEnchant.level
        }

        return pickedEnchantments
    }
}