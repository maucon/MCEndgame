package de.fuballer.mcendgame.main.messaging.totem_encounter

import net.minecraft.world.entity.player.Player

data class TotemEncounterActivatedEvent(
    val player: Player
)