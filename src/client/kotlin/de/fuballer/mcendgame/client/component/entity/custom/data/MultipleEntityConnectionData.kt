package de.fuballer.mcendgame.client.component.entity.custom.data

import net.minecraft.world.phys.Vec3

data class MultipleEntityConnectionData(
    var offset: Vec3 = Vec3.ZERO,
    var originEntity: EntityConnectionPointData = EntityConnectionPointData(),
    var connectedEntities: List<EntityConnectionPointData> = mutableListOf(),
)

data class EntityConnectionPointData(
    var pos: Vec3 = Vec3.ZERO,
    var blockLight: Int = 0,
    var skyLight: Int = 15,
    var connectionDuration: Float = 0F
)