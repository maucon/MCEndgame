package de.fuballer.mcendgame.main.messaging.misc

import net.minecraft.server.level.ServerLevel

data class DamageItemStackCommand(
    var damage: Int,
    val world: ServerLevel,
)