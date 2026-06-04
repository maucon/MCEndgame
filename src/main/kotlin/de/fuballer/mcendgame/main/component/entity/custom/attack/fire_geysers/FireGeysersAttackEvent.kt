package de.fuballer.mcendgame.main.component.entity.custom.attack.fire_geysers

import net.minecraft.world.entity.Entity

data class FireGeysersAttackEvent(
    val attacker: Entity,
    val target: Entity?,
    val burstDamageConversion: Double,
    val durationDamageConversion: Double,
    val delay: Int,
    val radius: Int,
    val geyserProbability: Double,
    val geyserCountLimit: Int,
    val indicatorDuration: Int,
    val pillarDuration: Int,
)