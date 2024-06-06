package de.fuballer.mcendgame.component.cosmetics.player.db

import de.fuballer.mcendgame.component.cosmetics.player.PlayerCosmeticsSettings
import de.fuballer.mcendgame.component.inventory.CustomInventoryType
import de.fuballer.mcendgame.util.InventoryUtil
import de.fuballer.mcendgame.util.ItemUtil
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
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
    var cosmeticEquipment = mutableMapOf<EquipmentSlot, ItemStack?>()

    init {
        this.showHelmet = showHelmet
        cosmeticEquipment[EquipmentSlot.HEAD] = helmet
        cosmeticEquipment[EquipmentSlot.CHEST] = chestplate
        cosmeticEquipment[EquipmentSlot.LEGS] = leggings
        cosmeticEquipment[EquipmentSlot.FEET] = boots
    }

    fun createInventory(): Inventory {
        val inventory = InventoryUtil.createInventory(
            PlayerCosmeticsSettings.INVENTORY_TYPE,
            PlayerCosmeticsSettings.INVENTORY_TITLE,
            CustomInventoryType.COSMETICS
        )

        inventory.setItem(PlayerCosmeticsSettings.SHOW_HELMET_INVENTORY_INDEX, getShowHelmetItem())
        PlayerCosmeticsSettings.EQUIPMENT_INVENTORY_INDICES.forEach {
            inventory.setItem(it.value, cosmeticEquipment[it.key]?.clone())
        }

        return inventory
    }

    fun getShowHelmetItem() =
        if (showHelmet) PlayerCosmeticsSettings.SHOW_HELMET_ITEM.clone()
        else PlayerCosmeticsSettings.HIDE_HELMET_ITEM.clone()

    fun getDisplayedItem(
        player: Player,
        slot: EquipmentSlot
    ): ItemStack {
        if (slot == EquipmentSlot.HEAD && !showHelmet) return ItemStack(Material.AIR)

        val playerEquipment = player.equipment ?: return ItemStack(Material.AIR)
        if (cosmeticEquipment[slot] == null) {
            return playerEquipment.getItem(slot)
        }

        val cosmeticItem = cosmeticEquipment[slot]!!.clone()
        return getItemRepresentingOtherItem(cosmeticItem, playerEquipment.getItem(slot))
    }

    private fun getItemRepresentingOtherItem(
        base: ItemStack,
        projection: ItemStack
    ): ItemStack {
        val result = base.clone()
        val projectionClone = projection.clone()
        ItemUtil.updateAttributesAndLore(projectionClone)

        setRepresentingDurability(result, projectionClone)
        setRepresentingEnchants(result, projectionClone)
        setRepresentingLore(result, projectionClone)
        addCosmeticTag(result)
        setHideAttributes(result)

        return result
    }

    private fun addCosmeticTag(
        item: ItemStack
    ) {
        val meta = item.itemMeta ?: return
        val lore = meta.lore ?: mutableListOf()
        lore.add(0, PlayerCosmeticsSettings.COSMETIC_ITEM_LORE_TAG)

        meta.lore = lore
        item.itemMeta = meta
    }

    private fun setRepresentingDurability(
        base: ItemStack,
        projection: ItemStack
    ) {
        val projectionMeta = projection.itemMeta as? Damageable ?: return
        val projectionDamage = projectionMeta.damage
        val projectionMaxDurability = projection.type.maxDurability
        val projectionDamagePercentage = projectionDamage.toDouble() / projectionMaxDurability

        val baseMeta = base.itemMeta as? Damageable ?: return
        val baseMaxDurability = base.type.maxDurability
        val baseDamage = (baseMaxDurability * projectionDamagePercentage).toInt()
        baseMeta.damage = baseDamage

        base.itemMeta = baseMeta
    }

    private fun setRepresentingEnchants(
        base: ItemStack,
        projection: ItemStack
    ) {
        val baseMeta = base.itemMeta ?: return

        val projectionMeta = projection.itemMeta
        if (projectionMeta == null || projection.type == Material.AIR) {
            baseMeta.enchants.forEach {
                baseMeta.removeEnchant(it.key)
            }
            base.itemMeta = baseMeta
            return
        }

        val projectionEnchants = projectionMeta.enchants
        baseMeta.enchants.forEach {
            baseMeta.removeEnchant(it.key)
        }
        projectionEnchants.forEach {
            baseMeta.addEnchant(it.key, it.value, true)
        }

        base.itemMeta = baseMeta
    }

    private fun setRepresentingLore(
        base: ItemStack,
        projection: ItemStack
    ) {
        val baseMeta = base.itemMeta ?: return

        val projectionLore = projection.itemMeta?.lore
        if (projectionLore == null || projection.type == Material.AIR) {
            baseMeta.lore = listOf()
            base.itemMeta = baseMeta
            return
        }

        baseMeta.lore = projectionLore.toMutableList()

        base.itemMeta = baseMeta
    }

    private fun setHideAttributes(
        base: ItemStack
    ) {
        val baseMeta = base.itemMeta ?: return
        baseMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
        base.itemMeta = baseMeta
    }
}