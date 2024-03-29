package de.fuballer.mcendgame.component.dungeon.looting

import de.fuballer.mcendgame.component.crafting.refinement.RefinementSettings
import de.fuballer.mcendgame.component.dungeon.enemy.equipment.enchantment.EquipmentEnchantmentService
import de.fuballer.mcendgame.component.dungeon.killstreak.KillStreakSettings
import de.fuballer.mcendgame.component.dungeon.killstreak.db.KillStreakRepository
import de.fuballer.mcendgame.event.DungeonEntityDeathEvent
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.ItemUtil
import de.fuballer.mcendgame.util.extension.EntityExtension.getMapTier
import de.fuballer.mcendgame.util.extension.EntityExtension.isDropEquipmentDisabled
import de.fuballer.mcendgame.util.extension.EntityExtension.isEnemy
import de.fuballer.mcendgame.util.extension.EntityExtension.isSpecial
import de.fuballer.mcendgame.util.random.RandomUtil
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.inventory.EntityEquipment
import org.bukkit.inventory.ItemStack
import kotlin.random.Random

@Component
class LootingService(
    private val killStreakRepo: KillStreakRepository,
    private val equipmentEnchantmentService: EquipmentEnchantmentService
) : Listener {
    @EventHandler
    fun on(event: DungeonEntityDeathEvent) {
        val entity = event.entity

        if (entity.isDropEquipmentDisabled()) return
        if (!entity.isEnemy()) return

        dropEquipment(entity, entity.world)

        if (entity.isSpecial()) {
            dropCustomItems(entity, entity.world)
            dropOrbOfRefinement(entity, entity.world)
        }
    }

    private fun dropEquipment(entity: LivingEntity, world: World) {
        val looting = getLootingLevel(entity.killer)
        val killStreak = killStreakRepo.findById(world.name)?.streak ?: 0
        val streakDropChance = 1 + killStreak * KillStreakSettings.GEAR_DROP_CHANCE_MULTIPLIER_PER_STREAK

        for (item in getEquipment(entity.equipment)) {
            val finalDropChance = getItemDropChance(item, looting) * streakDropChance
            if (Random.nextDouble() > finalDropChance) continue

            val finalItem = ItemUtil.setRandomDurability(item)
            world.dropItemNaturally(entity.location, finalItem)
        }
    }

    private fun dropCustomItems(entity: LivingEntity, world: World) {
        val mapTier = entity.getMapTier() ?: return
        if (Random.nextDouble() > LootingSettings.getCustomItemDropChance(mapTier)) return

        val customItemType = RandomUtil.pick(LootingSettings.CUSTOM_ITEM_OPTIONS).option
        val item = ItemUtil.createCustomItem(customItemType)

        val itemMeta = item.itemMeta!!
        equipmentEnchantmentService.enchantItem(Random, mapTier, itemMeta, customItemType.equipment.rollableEnchants)
        item.itemMeta = itemMeta

        world.dropItemNaturally(entity.location, item)
    }

    private fun dropOrbOfRefinement(entity: LivingEntity, world: World) {
        val mapTier = entity.getMapTier() ?: return
        if (Random.nextDouble() > LootingSettings.getOrbOfRefinementDropChance(mapTier)) return

        world.dropItemNaturally(entity.location, RefinementSettings.getRefinementItem())
    }

    private fun getLootingLevel(player: Player?): Int {
        if (player == null) return 0
        val equipment = player.equipment ?: return 0

        val item = equipment.itemInMainHand
        val itemMeta = item.itemMeta ?: return 0

        return itemMeta.getEnchantLevel(Enchantment.LOOT_BONUS_BLOCKS)
            .coerceAtLeast(itemMeta.getEnchantLevel(Enchantment.LOOT_BONUS_MOBS))
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