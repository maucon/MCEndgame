package de.fuballer.mcendgame.main.component.screen

import de.fuballer.mcendgame.main.component.block.blocks.crystalforge.CrystalForgeBlock
import de.fuballer.mcendgame.main.component.block.blocks.crystalforge.CrystalForgeScreenHandler
import de.fuballer.mcendgame.main.component.block.blocks.dungeon_device.DungeonDeviceBlock
import de.fuballer.mcendgame.main.component.block.blocks.dungeon_device.DungeonDeviceScreenHandler
import de.fuballer.mcendgame.main.component.block.blocks.dungeon_device.networking.DungeonDevicePayload
import de.fuballer.mcendgame.main.component.killer.KillerScreenHandler
import de.fuballer.mcendgame.main.component.killer.networking.KillerEntityPayload
import de.fuballer.mcendgame.main.component.totem.TotemScreenHandler
import de.fuballer.mcendgame.main.util.minecraft.RegistryUtil
import de.maucon.mauconframework.di.annotation.Injectable
import net.fabricmc.fabric.api.menu.v1.ExtendedMenuType
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.inventory.MenuType

@Injectable
object CustomScreenHandlerTypes {
    val DUNGEON_DEVICE = ExtendedMenuType(
        { syncId, inventory, payload -> DungeonDeviceScreenHandler(syncId, inventory, payload = payload) },
        DungeonDevicePayload.CODEC,
    ).also { RegistryUtil.registerScreenHandler(DungeonDeviceBlock.ID, it) }

    val KILLER = ExtendedMenuType(
        { syncId, inventory, payload -> KillerScreenHandler(syncId, inventory, payload = payload) },
        KillerEntityPayload.CODEC,
    ).also { RegistryUtil.registerScreenHandler("killer", it) }

    val CRYSTAL_FORGE = MenuType(
        ::CrystalForgeScreenHandler,
        FeatureFlags.VANILLA_SET,
    ).also { RegistryUtil.registerScreenHandler(CrystalForgeBlock.ID, it) }

    val TOTEM = MenuType(
        ::TotemScreenHandler,
        FeatureFlags.VANILLA_SET,
    ).also { RegistryUtil.registerScreenHandler("totem", it) }
}