package de.fuballer.mcendgame.component.dungeon.loot

import de.fuballer.mcendgame.component.dungeon.enemy.equipment.enchantment.EquipmentEnchantmentService
import de.fuballer.mcendgame.component.dungeon.modifier.ModifierType
import de.fuballer.mcendgame.component.dungeon.modifier.ModifierUtil
import de.fuballer.mcendgame.component.totem.data.Totem
import de.fuballer.mcendgame.event.DungeonEntityDeathEvent
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.ItemCreatorUtil
import de.fuballer.mcendgame.util.ItemUtil
import de.fuballer.mcendgame.util.extension.EntityExtension.getMapTier
import de.fuballer.mcendgame.util.extension.EntityExtension.isBoss
import de.fuballer.mcendgame.util.extension.EntityExtension.isDropEquipmentDisabled
import de.fuballer.mcendgame.util.extension.EntityExtension.isEnemy
import de.fuballer.mcendgame.util.extension.EntityExtension.isMinion
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
class LootService(
    private val equipmentEnchantmentService: EquipmentEnchantmentService
) : Listener {
    @EventHandler
    fun on(event: DungeonEntityDeathEvent) {
        val entity = event.entity
        if (!entity.isEnemy()) return
        if (entity.isMinion()) return
        if (entity.isBoss()) return

        dropTotem(entity)
        dropCustomItems(entity, entity.world)

        if (entity.isDropEquipmentDisabled()) return
        dropEquipment(entity, entity.world)
    }

    private fun dropEquipment(entity: LivingEntity, world: World) {
        val killer = entity.killer

        val looting = getLootingLevel(killer)
        val magicFindMultiplier = ModifierUtil.getModifierMultiplier(killer, ModifierType.MAGIC_FIND)

        for (item in getEquipment(entity.equipment)) {
            val baseDropChance = getItemDropChance(item, looting)
            val finalDropChance = baseDropChance * magicFindMultiplier

            if (Random.nextDouble() > finalDropChance) continue

            val finalItem = ItemUtil.setRandomDurability(item)
            world.dropItemNaturally(entity.location, finalItem)
        }
    }

    private fun dropCustomItems(entity: LivingEntity, world: World) {
        val mapTier = entity.getMapTier() ?: return
        if (Random.nextDouble() > LootSettings.getCustomItemDropChance(mapTier)) return

        val customItemType = RandomUtil.pick(LootSettings.CUSTOM_ITEM_OPTIONS).option
        val item = ItemCreatorUtil.createCustomItem(customItemType)

        val itemMeta = item.itemMeta!!
        equipmentEnchantmentService.enchantItem(Random, mapTier, itemMeta, customItemType.equipment.rollableEnchants)
        item.itemMeta = itemMeta

        world.dropItemNaturally(entity.location, item)
    }

    private fun getLootingLevel(player: Player?): Int {
        if (player == null) return 0
        val equipment = player.equipment ?: return 0

        val item = equipment.itemInMainHand
        val itemMeta = item.itemMeta ?: return 0

        return itemMeta.getEnchantLevel(Enchantment.FORTUNE)
            .coerceAtLeast(itemMeta.getEnchantLevel(Enchantment.LOOTING))
    }

    private fun getItemDropChance(item: ItemStack, looting: Int): Double {
        val typeString = item.type.toString()

        if (typeString.contains("DIAMOND")) {
            return LootSettings.ITEMS_DROP_CHANCE_DIAMOND + LootSettings.ITEMS_DROP_CHANCE_DIAMOND_PER_LOOTING * looting
        }
        if (typeString.contains("NETHERITE")) {
            return LootSettings.ITEMS_DROP_CHANCE_NETHERITE + LootSettings.ITEMS_DROP_CHANCE_NETHERITE_PER_LOOTING * looting
        }
        if (typeString.contains("TRIDENT") || typeString.contains("MACE")) {
            return 0.0
        }

        return LootSettings.ITEMS_DROP_CHANCE + LootSettings.ITEMS_DROP_CHANCE_PER_LOOTING * looting
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

    private fun dropTotem(entity: LivingEntity) {
        if (Random.nextDouble() > LootSettings.TOTEM_DROP_CHANCE) return

        val mapTier = entity.getMapTier() ?: 1
        val type = RandomUtil.pick(LootSettings.TOTEM_TYPES).option
        val tier = RandomUtil.pick(LootSettings.TOTEM_TIERS, mapTier).option

        val totem = Totem(type, tier)
        val totemItem = totem.toItem()

        entity.world.dropItemNaturally(entity.location, totemItem)
    }
}