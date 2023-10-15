package de.fuballer.mcendgame.component.dungeon.looting

import de.fuballer.mcendgame.component.dungeon.enemy.custom_entity.Keys
import de.fuballer.mcendgame.component.dungeon.killingstreak.KillStreakSettings
import de.fuballer.mcendgame.component.dungeon.killingstreak.db.KillStreakRepository
import de.fuballer.mcendgame.framework.stereotype.Service
import de.fuballer.mcendgame.helper.WorldHelper
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.inventory.EntityEquipment
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import org.bukkit.persistence.PersistentDataType
import java.util.*

class LootingService(
    private val killStreakRepo: KillStreakRepository
) : Service {
    private val random = Random()

    fun onEntityDeath(event: EntityDeathEvent) {
        val entity = event.entity
        val world = entity.world
        if (WorldHelper.isNotDungeonWorld(world)) return

        if (!canDropEquipment(entity)) return

        val looting = getLootingLevel(entity.killer)
        for (item in getEquipment(entity.equipment)) {
            val killStreak = killStreakRepo.findById(world.name)?.streak ?: 0
            val streakDropChance = 1 + killStreak * KillStreakSettings.GEAR_DROP_CHANCE_MULTIPLIER_PER_STREAK
            val finalDropChance = getItemDropChance(item, looting) * streakDropChance

            if (random.nextDouble() < finalDropChance) {
                val meta = item.itemMeta
                if (meta is Damageable) {
                    meta.damage = (random.nextDouble() * item.type.maxDurability).toInt()
                    item.itemMeta = meta
                }
                world.dropItemNaturally(entity.location, item)
            }
        }
    }

    private fun canDropEquipment(entity: Entity): Boolean {
        if (!entity.persistentDataContainer.has(Keys.DROP_EQUIPMENT_KEY, PersistentDataType.BOOLEAN)) return true
        return entity.persistentDataContainer.get(Keys.DROP_EQUIPMENT_KEY, PersistentDataType.BOOLEAN) ?: return true
    }

    private fun getLootingLevel(player: Player?): Int {
        if (player == null) return 0
        val equipment = player.equipment ?: return 0

        val item = equipment.itemInMainHand
        val meta = item.itemMeta ?: return 0

        return meta.getEnchantLevel(Enchantment.LOOT_BONUS_BLOCKS)
            .coerceAtLeast(meta.getEnchantLevel(Enchantment.LOOT_BONUS_MOBS))
    }

    private fun getItemDropChance(item: ItemStack, looting: Int): Float {
        val typeString = item.type.toString()
        if (typeString.contains("DIAMOND")) {
            return LootingSettings.ITEMS_DROP_CHANCE_DIAMOND + LootingSettings.ITEMS_DROP_CHANCE_DIAMOND_PER_LOOTING * looting
        }
        if (typeString.contains("NETHERITE")) {
            return LootingSettings.ITEMS_DROP_CHANCE_NETHERITE + LootingSettings.ITEMS_DROP_CHANCE_NETHERITE_PER_LOOTING * looting
        }
        if (typeString.contains("TRIDENT")) {
            return 0.0f
        }
        return LootingSettings.ITEMS_DROP_CHANCE + LootingSettings.ITEMS_DROP_CHANCE_PER_LOOTING * looting
    }

    private fun getEquipment(entityEquipment: EntityEquipment?): List<ItemStack> {
        if (entityEquipment == null) return listOf()

        return listOfNotNull(
            entityEquipment.itemInMainHand,
            entityEquipment.itemInOffHand,
            entityEquipment.helmet,
            entityEquipment.chestplate,
            entityEquipment.leggings,
            entityEquipment.boots
        ).filter { it.type != Material.AIR }
    }
}