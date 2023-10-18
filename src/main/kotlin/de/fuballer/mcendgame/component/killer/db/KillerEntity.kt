package de.fuballer.mcendgame.component.killer.db

import de.fuballer.mcendgame.component.killer.KillerSettings
import de.fuballer.mcendgame.util.PluginUtil
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Ageable
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionEffect
import java.util.*

class KillerEntity(
    override var id: UUID,
    entity: Entity
) : de.fuballer.mcendgame.framework.stereotype.Entity<UUID> {
    private var spawnEgg: ItemStack
    private var equipment: Array<ItemStack?>
    private var potions: List<ItemStack>
    private var health: ItemStack

    init {
        spawnEgg = getSpawnEgg(entity)
        equipment = getEquipment(entity)
        potions = getPotions(entity)
        health = getHealth(entity)
    }

    fun createInventory(playerName: String): Inventory {
        val inventory = PluginUtil.createInventory(
            InventoryType.CHEST,
            "${KillerSettings.INVENTORY_TITLE} - $playerName"
        )

        inventory.setItem(9, spawnEgg)
        inventory.setItem(10, health)
        (0..5).forEach { inventory.setItem(12 + it, equipment[it]) }
        potions.indices.forEach { inventory.setItem(26 - it, potions[it]) }

        return inventory
    }

    private fun getSpawnEgg(entity: Entity): ItemStack {
        val type = entity.type
        val spawnEgg = ItemStack(KillerSettings.ENTITY_SPAWN_EGGS.getOrDefault(type, KillerSettings.DEFAULT_SPAWN_EGG), 1)

        val meta = spawnEgg.itemMeta ?: return spawnEgg
        meta.setDisplayName(ChatColor.BLUE.toString() + entity.name)

        if (entity is Ageable && !entity.isAdult) meta.lore = KillerSettings.BABY_LORE

        return spawnEgg.apply { itemMeta = meta }
    }

    private fun getEquipment(entity: Entity): Array<ItemStack?> {
        val equipment = arrayOfNulls<ItemStack>(6)

        if (entity !is LivingEntity) return equipment
        val entityEquipment = entity.equipment ?: return equipment

        equipment[0] = entityEquipment.itemInMainHand.clone()
        equipment[1] = entityEquipment.itemInOffHand.clone()
        equipment[2] = entityEquipment.helmet?.clone()
        equipment[3] = entityEquipment.chestplate?.clone()
        equipment[4] = entityEquipment.leggings?.clone()
        equipment[5] = entityEquipment.boots?.clone()

        return equipment
    }

    private fun getPotions(entity: Entity): List<ItemStack> {
        if (entity !is LivingEntity) return listOf()
        return entity.activePotionEffects.map {
            val potion = ItemStack(Material.POTION, 1)
            val meta = potion.itemMeta as PotionMeta? ?: return@map potion

            meta.setDisplayName(KillerSettings.POTION_EFFECT_ITEM_NAME)
            meta.addCustomEffect(PotionEffect(it.type, 0, it.amplifier), true)

            potion.apply { itemMeta = meta }
        }
    }

    private fun getHealth(entity: Entity): ItemStack {
        val health = ItemStack(Material.RED_DYE, 1)
        val meta = health.itemMeta ?: return health

        meta.setDisplayName(ChatColor.RED.toString() + "0 Health")
        health.itemMeta = meta

        if (entity !is LivingEntity) return health
        val attribute = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH) ?: return health

        meta.setDisplayName(ChatColor.RED.toString() + attribute.value.toInt() + " Health")
        health.itemMeta = meta
        return health
    }
}