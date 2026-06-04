package de.fuballer.mcendgame.main.component.item_filter

import de.maucon.mauconframework.command.cancellable.CancellableCommand
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item

data class PlayerItemPickupCommand(
    val player: Player,
    val item: Item,
) : CancellableCommand()