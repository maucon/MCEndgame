package de.fuballer.mcendgame.main.component.dungeon.enemy.equipment.enchantment

import de.fuballer.mcendgame.main.component.item.equipment.enchantment.EquipmentEnchantment
import de.fuballer.mcendgame.main.util.random.RandomOption
import de.fuballer.mcendgame.main.util.random.RandomUtil
import de.maucon.mauconframework.di.annotation.Injectable
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.server.MinecraftServer
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentHelper
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
            itemStack.enchant(it.key, it.value)
        }
    }

    private fun selectEnchantments(
        level: Int,
        enchants: List<RandomOption<EquipmentEnchantment>>,
        server: MinecraftServer,
        random: Random,
    ): Map<Holder<Enchantment>, Int> {
        if (enchants.isEmpty()) return mapOf()

        val enchantmentRegistry = server.registryAccess().lookupOrThrow(Registries.ENCHANTMENT)

        val pickedEnchantments = mutableMapOf<Holder<Enchantment>, Int>()
        repeat(EnchantmentSettings.calculateEnchantTries(level)) {
            val potentialEnchant = RandomUtil.pickOne(enchants, random).option
            val potentialEnchantEntry = enchantmentRegistry.getOrThrow(potentialEnchant.enchantment)

            val compatible = EnchantmentHelper.isEnchantmentCompatible(pickedEnchantments.keys, potentialEnchantEntry)
            if (!compatible) return@repeat

            pickedEnchantments[potentialEnchantEntry] = potentialEnchant.level
        }

        return pickedEnchantments
    }
}