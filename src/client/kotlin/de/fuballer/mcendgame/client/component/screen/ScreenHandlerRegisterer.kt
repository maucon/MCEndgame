package de.fuballer.mcendgame.client.component.screen

import de.fuballer.mcendgame.main.component.screen.CustomScreenHandlerTypes
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.initializer.Initializer
import net.minecraft.client.gui.screens.MenuScreens

@Injectable
object ScreenHandlerRegisterer {
    @Initializer
    fun register() {
        MenuScreens.register(CustomScreenHandlerTypes.DUNGEON_DEVICE, ::DungeonDeviceScreen)
        MenuScreens.register(CustomScreenHandlerTypes.KILLER, ::KillerScreen)
        MenuScreens.register(CustomScreenHandlerTypes.CRYSTAL_FORGE, ::CrystalForgeScreen)
        MenuScreens.register(CustomScreenHandlerTypes.TOTEM, ::TotemScreen)
    }
}