package de.fuballer.mcendgame.component.dungeon.artifact.db

import de.fuballer.mcendgame.framework.stereotype.Entity
import java.util.*

data class HealOnBlockArtifactEntity(
    override var id: UUID,

    var lastActivationTime: Long = 0
) : Entity<UUID>
