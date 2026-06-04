package de.fuballer.mcendgame.main.component.dungeon.world.db

import de.maucon.mauconframework.stereotype.Entity
import net.minecraft.server.level.ServerLevel
import java.time.Instant
import java.util.*

data class DungeonWorldEntity(
    override var id: UUID,

    var world: ServerLevel,
    var emptySince: Instant = Instant.now()
) : Entity<UUID> {
    constructor(
        world: ServerLevel,
    ) : this(UUID.randomUUID(), world)
}