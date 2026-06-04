package de.fuballer.mcendgame.main.component.dungeon.generation.encounter.encounters.scarred_one.messaging

import de.fuballer.mcendgame.main.component.entity.custom.entities.scarred_one.ScarredOneEntity
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer

data class ScarredOneInteractEvent(
    val scarredOne: ScarredOneEntity,
    val player: ServerPlayer,
    val serverWorld: ServerLevel,
)