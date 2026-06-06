package de.fuballer.mcendgame.main.component.dungeon.generation.encounter.encounters.scarred_one.messaging

import net.minecraft.server.level.ServerLevel

data class CollectScarredOneEffectCountCommand(
    val world: ServerLevel,
    var positive: Int,
    var negative: Int,
)