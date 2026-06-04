package de.fuballer.mcendgame.main.component.entity.custom.attack.teleport

import de.fuballer.mcendgame.main.component.entity.custom.interfaces.TeleportAttackMob
import net.minecraft.world.entity.Entity

data class TeleportAttackEvent<T>(
    val attacker: T,
    val target: Entity,
    val chosePositionDelayTicks: Int,
    val teleportDelayTicks: Int,
) where T : Entity, T : TeleportAttackMob