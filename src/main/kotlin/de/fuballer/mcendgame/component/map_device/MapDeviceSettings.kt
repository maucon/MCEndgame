package de.fuballer.mcendgame.component.map_device

import de.fuballer.mcendgame.util.ItemCreator
import de.fuballer.mcendgame.util.TextComponent
import de.fuballer.mcendgame.util.extension.ItemStackExtension.setMapDevice
import de.fuballer.mcendgame.util.extension.ItemStackExtension.setMapDeviceAction
import de.fuballer.mcendgame.util.extension.ItemStackExtension.setUnmodifiable
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.util.Vector

object MapDeviceSettings {
    private val ITEM_NAME = TextComponent.create("Map Device", NamedTextColor.DARK_PURPLE)
    private val ITEM_LORE =
        listOf(
            TextComponent.create("Opens portals to dungeons"),
            TextComponent.create("packed with monsters and treasures")
        )

    private val ITEM = ItemCreator.create(
        ItemStack(Material.RESPAWN_ANCHOR),
        ITEM_NAME,
        ITEM_LORE
    ).apply {
        setMapDevice()
        setUnmodifiable()
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

    private val OPEN_PORTALS_ITEM_LINE = TextComponent.create("Open portals", NamedTextColor.GREEN)
    val OPEN_PORTALS_ITEM = ItemCreator.create(
        ItemStack(Material.LIME_STAINED_GLASS_PANE),
        OPEN_PORTALS_ITEM_LINE
    ).apply {
        setMapDeviceAction(MapDeviceAction.OPEN)
    }

    private val CLOSE_PORTALS_ITEM_LINE = TextComponent.create("Close portals", NamedTextColor.RED)
    val CLOSE_PORTALS_ITEM = ItemCreator.create(
        ItemStack(Material.RED_STAINED_GLASS_PANE),
        CLOSE_PORTALS_ITEM_LINE
    ).apply {
        setMapDeviceAction(MapDeviceAction.CLOSE)
    }

    val FILLER_ITEM = ItemCreator.create(ItemStack(Material.GRAY_STAINED_GLASS_PANE), TextComponent.empty(), listOf())

    const val MAP_DEVICE_BLOCK_METADATA_KEY = "MAP_DEVICE"

    val PORTAL_OFFSETS = listOf(
        Vector(-1.0, 0.0, 1.732),
        Vector(1.0, 0.0, 1.732),
        Vector(-1.0, 0.0, -1.732),
        Vector(1.0, 0.0, -1.732),
        Vector(2.0, 0.0, 0.0),
        Vector(-2.0, 0.0, 0.0),
    )
}