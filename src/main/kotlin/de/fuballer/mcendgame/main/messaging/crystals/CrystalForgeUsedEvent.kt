package de.fuballer.mcendgame.main.messaging.crystals

import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item

data class CrystalForgeUsedEvent(
    val player: Player,
    val crystal: Item,
)