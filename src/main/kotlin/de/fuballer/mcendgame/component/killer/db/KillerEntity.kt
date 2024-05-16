package de.fuballer.mcendgame.component.killer.db

import de.fuballer.mcendgame.component.inventory.CustomInventoryType
import de.fuballer.mcendgame.component.killer.KillerSettings
import de.fuballer.mcendgame.util.InventoryUtil
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Ageable
import org.bukkit.entity.LivingEntity
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionEffect
import java.text.DecimalFormat
import java.util.*

private val DECIMAL_FORMAT = DecimalFormat("#.##")

class KillerEntity(
    override var id: UUID,
    entity: LivingEntity
) : de.fuballer.mcendgame.framework.stereotype.Entity<UUID> {

    private var spawnEgg: ItemStack
    private var equipment: Array<ItemStack?>
    private var potions: List<ItemStack>
    private var damage: ItemStack
    private var health: ItemStack

    init {
        spawnEgg = getSpawnEgg(entity)
        equipment = getEquipment(entity)
        potions = getPotions(entity)
        damage = getDamage(entity)
        health = getHealth(entity)
    }

    fun createInventory(playerName: String): Inventory {
        val inventory = InventoryUtil.createInventory(
            InventoryType.CHEST,
            "${KillerSettings.INVENTORY_TITLE} - $playerName",
            CustomInventoryType.KILLER
        )

        inventory.setItem(9, spawnEgg)
        inventory.setItem(10, damage)
        inventory.setItem(11, health)

        (0..5).forEach { inventory.setItem(12 + it, equipment[it]) }
        potions.indices.forEach { inventory.setItem(26 - it, potions[it]) }

        return inventory
    }

    private fun getSpawnEgg(entity: LivingEntity): ItemStack {
        val spawnEgg = ItemStack(KillerSettings.DEFAULT_SPAWN_EGG)

        val meta = spawnEgg.itemMeta ?: return spawnEgg
        meta.setDisplayName(ChatColor.BLUE.toString() + (entity.customName ?: entity.name))

        if (entity is Ageable && !entity.isAdult) meta.lore = KillerSettings.BABY_LORE

        return spawnEgg.apply { itemMeta = meta }
    }

    private fun getEquipment(entity: LivingEntity): Array<ItemStack?> {
        val equipment = arrayOfNulls<ItemStack>(6)
        val entityEquipment = entity.equipment ?: return equipment

        equipment[0] = entityEquipment.itemInMainHand.clone()
        equipment[1] = entityEquipment.itemInOffHand.clone()
        equipment[2] = entityEquipment.helmet?.clone()
        equipment[3] = entityEquipment.chestplate?.clone()
        equipment[4] = entityEquipment.leggings?.clone()
        equipment[5] = entityEquipment.boots?.clone()

        return equipment
    }

    private fun getPotions(entity: LivingEntity): List<ItemStack> {
        return entity.activePotionEffects.map {
            val potion = ItemStack(Material.POTION, 1)
            val meta = potion.itemMeta as PotionMeta? ?: return@map potion

            meta.setDisplayName(KillerSettings.POTION_EFFECT_ITEM_NAME)
            meta.addCustomEffect(PotionEffect(it.type, 0, it.amplifier), true)

            potion.apply { itemMeta = meta }
        }
    }

    private fun getDamage(entity: LivingEntity): ItemStack {
        val damage = ItemStack(Material.RED_DYE, 1)
        val meta = damage.itemMeta ?: return damage

        val attribute = entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE) ?: return damage

        val formattedValue = DECIMAL_FORMAT.format(attribute.baseValue)
        meta.setDisplayName(ChatColor.RED.toString() + formattedValue + " Damage")
        damage.itemMeta = meta

        return damage
    }

    private fun getHealth(entity: LivingEntity): ItemStack {
        val health = ItemStack(Material.GREEN_DYE, 1)
        val meta = health.itemMeta ?: return health

        val attribute = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH) ?: return health

        val formattedValue = DECIMAL_FORMAT.format(attribute.baseValue)
        meta.setDisplayName(ChatColor.RED.toString() + formattedValue + " Health")
        health.itemMeta = meta

        return health
    }
}