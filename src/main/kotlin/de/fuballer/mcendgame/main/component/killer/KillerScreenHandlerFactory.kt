package de.fuballer.mcendgame.main.component.killer

import de.fuballer.mcendgame.main.component.killer.networking.KillerEntityPayload
import net.fabricmc.fabric.api.menu.v1.ExtendedMenuProvider
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu

class KillerScreenHandlerFactory(
    val payload: KillerEntityPayload,
    val title: Component,
    val handlerFactory: (Int, Inventory, Player) -> AbstractContainerMenu,
) : ExtendedMenuProvider<KillerEntityPayload> {
    override fun getScreenOpeningData(player: ServerPlayer) = payload

    override fun getDisplayName() = title

    override fun createMenu(
        syncId: Int,
        playerInventory: Inventory,
        player: Player,
    ) = handlerFactory(syncId, playerInventory, player)
}