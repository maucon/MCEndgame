package de.fuballer.mcendgame.component.cosmetics.player.db

import de.fuballer.mcendgame.component.cosmetics.player.PlayerCosmeticsSettings
import de.fuballer.mcendgame.component.inventory.CustomInventoryType
import de.fuballer.mcendgame.util.InventoryUtil
import org.bukkit.Material
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import java.util.*

class PlayerCosmeticsEntity(
    override var id: UUID,
    showHelmet: Boolean = true,
    helmet: ItemStack? = null,
    chestplate: ItemStack? = null,
    leggings: ItemStack? = null,
    boots: ItemStack? = null,
) : de.fuballer.mcendgame.framework.stereotype.Entity<UUID> {

    var showHelmet: Boolean
    var helmet: ItemStack?
    var chestplate: ItemStack?
    var leggings: ItemStack?
    var boots: ItemStack?

    init {
        this.showHelmet = showHelmet
        this.helmet = helmet
        this.chestplate = chestplate
        this.leggings = leggings
        this.boots = boots
    }

    fun createInventory(): Inventory {
        val inventory = InventoryUtil.createInventory(
            PlayerCosmeticsSettings.INVENTORY_TYPE,
            PlayerCosmeticsSettings.INVENTORY_TITLE,
            CustomInventoryType.COSMETICS
        )

        inventory.setItem(PlayerCosmeticsSettings.SHOW_HELMET_INVENTORY_INDEX, getShowHelmetItem())
        inventory.setItem(PlayerCosmeticsSettings.HELMET_INVENTORY_INDEX, helmet?.clone())
        inventory.setItem(PlayerCosmeticsSettings.CHESTPLATE_INVENTORY_INDEX, chestplate?.clone())
        inventory.setItem(PlayerCosmeticsSettings.LEGGINGS_INVENTORY_INDEX, leggings?.clone())
        inventory.setItem(PlayerCosmeticsSettings.BOOTS_INVENTORY_INDEX, boots?.clone())

        return inventory
    }

    fun getShowHelmetItem() =
        if (showHelmet) PlayerCosmeticsSettings.SHOW_HELMET_ITEM.clone()
        else PlayerCosmeticsSettings.HIDE_HELMET_ITEM.clone()

    fun getHelmetItemOrAir(ignoreShown: Boolean = true) = if (helmet != null && (ignoreShown || showHelmet)) helmet!! else ItemStack(Material.AIR)
    fun getChestplateItemOrAir() = chestplate ?: ItemStack(Material.AIR)
    fun getLeggingsItemOrAir() = leggings ?: ItemStack(Material.AIR)
    fun getBootsItemOrAir() = boots ?: ItemStack(Material.AIR)
}