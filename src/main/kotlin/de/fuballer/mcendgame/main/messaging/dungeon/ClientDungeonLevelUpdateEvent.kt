package de.fuballer.mcendgame.main.messaging.dungeon

import de.fuballer.mcendgame.main.component.dungeon.level.PlayerDungeonLevel
import net.minecraft.server.level.ServerPlayer

data class ClientDungeonLevelUpdateEvent(
    val playerEntity: ServerPlayer,
    val dungeonLevel: PlayerDungeonLevel,
)