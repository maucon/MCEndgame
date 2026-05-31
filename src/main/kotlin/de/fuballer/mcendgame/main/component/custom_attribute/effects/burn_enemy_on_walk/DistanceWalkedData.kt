package de.fuballer.mcendgame.main.component.custom_attribute.effects.burn_enemy_on_walk

import net.minecraft.util.math.Vec3d

data class DistanceWalkedData(
    var distance: Double,
    var previousPos: Vec3d,
    var lastUpdatedWorldTime: Long,
)