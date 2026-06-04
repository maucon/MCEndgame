package de.fuballer.mcendgame.main.component.custom_attribute.effects.burn_enemy_on_walk

import net.minecraft.world.phys.Vec3

data class DistanceWalkedData(
    var distance: Double,
    var previousPos: Vec3,
    var lastUpdatedWorldTime: Long,
)