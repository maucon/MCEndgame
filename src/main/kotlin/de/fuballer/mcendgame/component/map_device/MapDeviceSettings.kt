package de.fuballer.mcendgame.component.map_device

import de.fuballer.mcendgame.domain.persistent_data.TypeKeys
import de.fuballer.mcendgame.util.ItemCreatorUtil
import de.fuballer.mcendgame.util.ItemUtil
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe

object MapDeviceSettings {
    private val ITEM_NAME = "${ChatColor.DARK_PURPLE}Map Device"
    private val ITEM_LORE = listOf("${ChatColor.GRAY}${ChatColor.ITALIC}Opens portals to dungeons", "${ChatColor.GRAY}${ChatColor.ITALIC}packed with monsters and treasures")

    private val ITEM = ItemCreatorUtil.create(
        ItemStack(Material.RESPAWN_ANCHOR),
        ITEM_NAME,
        ITEM_LORE
    ).apply {
        ItemUtil.setPersistentData(this, TypeKeys.MAP_DEVICE, true)
        ItemUtil.setPersistentData(this, TypeKeys.UNMODIFIABLE, true)
    }

    fun getMapDeviceItem() = ITEM.clone()

    const val MAP_DEVICE_ITEM_KEY = "map_device"

    fun getMapDeviceCraftingRecipe(key: NamespacedKey) =
        ShapedRecipe(key, ITEM).apply {
            shape("ONO", "NSN", "ONO")
            setIngredient('O', Material.OBSIDIAN)
            setIngredient('N', Material.NETHERITE_INGOT)
            setIngredient('S', Material.NETHER_STAR)
        }

    const val MAP_DEVICE_INVENTORY_TITLE = "Map Device"

    private val OPEN_PORTALS_ITEM_LINE = ChatColor.GREEN.toString() + "Open portals"
    val OPEN_PORTALS_ITEM = ItemCreatorUtil.create(
        ItemStack(Material.LIME_STAINED_GLASS_PANE),
        OPEN_PORTALS_ITEM_LINE,
        listOf()
    ).apply { ItemUtil.setPersistentData(this, TypeKeys.MAP_DEVICE_ACTION, MapDeviceAction.OPEN) }

    private val CLOSE_PORTALS_ITEM_LINE = ChatColor.RED.toString() + "Close portals"
    val CLOSE_PORTALS_ITEM = ItemCreatorUtil.create(
        ItemStack(Material.RED_STAINED_GLASS_PANE),
        CLOSE_PORTALS_ITEM_LINE,
        listOf()
    ).apply { ItemUtil.setPersistentData(this, TypeKeys.MAP_DEVICE_ACTION, MapDeviceAction.CLOSE) }

    val FILLER_ITEM = ItemCreatorUtil.create(ItemStack(Material.GRAY_STAINED_GLASS_PANE), " ", listOf(" "))

    const val MAP_DEVICE_BLOCK_METADATA_KEY = "MAP_DEVICE"
    const val MAP_DEVICE_PORTAL_ENTITY_NAME = "mcEndgame_portal"

    val DUNGEON_INSTANCE_CLOSED = "${ChatColor.RED}Dungeon instance already closed!"
}