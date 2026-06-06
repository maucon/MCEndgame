package de.fuballer.mcendgame.main.component.entity.custom.entities.scarred_one

import net.minecraft.server.level.ServerPlayer

data class ScarredOneDespawnEvent(
    /**
     * the player who accepted/denied
     */
    val player: ServerPlayer,
    val entity: ScarredOneEntity,
    val accepted: Boolean,
)