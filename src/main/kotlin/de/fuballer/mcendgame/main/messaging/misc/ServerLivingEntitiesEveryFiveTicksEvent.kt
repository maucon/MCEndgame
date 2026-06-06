package de.fuballer.mcendgame.main.messaging.misc

import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.LivingEntity

data class ServerLivingEntitiesEveryFiveTicksEvent(
    val entities: List<LivingEntity>,
    val world: ServerLevel,
)