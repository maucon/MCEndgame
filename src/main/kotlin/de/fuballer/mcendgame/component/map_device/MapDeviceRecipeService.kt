package de.fuballer.mcendgame.component.map_device

import de.fuballer.mcendgame.event.DiscoverRecipeAddEvent
import de.fuballer.mcendgame.event.EventGateway
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.framework.stereotype.LifeCycleListener
import de.fuballer.mcendgame.util.PluginUtil
import org.bukkit.plugin.java.JavaPlugin

@Component
class MapDeviceRecipeService : LifeCycleListener {
    override fun initialize(plugin: JavaPlugin) {
        val key = PluginUtil.createNamespacedKey(MapDeviceSettings.MAP_DEVICE_ITEM_KEY)
        val recipe = MapDeviceSettings.getMapDeviceCraftingRecipe(key)

        val discoverRecipeAddEvent = DiscoverRecipeAddEvent(key, recipe)
        EventGateway.apply(discoverRecipeAddEvent)
    }
}