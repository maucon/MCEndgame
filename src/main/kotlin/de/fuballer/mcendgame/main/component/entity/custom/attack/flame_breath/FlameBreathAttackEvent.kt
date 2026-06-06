package de.fuballer.mcendgame.main.component.entity.custom.attack.flame_breath

import net.minecraft.world.entity.Entity

class FlameBreathAttackEvent(
    val attacker: Entity,
    val target: Entity?,
    val damageConversion: Double,
    val delay: Int,
    val duration: Int,
    val angle: Double,
    val entityWidthOffsetFactor: Double,
    val entityHeightOffsetFactor: Double,
)