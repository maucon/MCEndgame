package de.fuballer.mcendgame.component.dungeon.boss.db

import de.fuballer.mcendgame.framework.stereotype.Entity
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Creature
import java.util.*

data class DungeonBossesEntity(
    override var id: UUID,

    val world: World,
    val mapTier: Int,
    val bosses: List<Creature>,
    val leaveLocation: Location,
    var progressGranted: Boolean = false,
) : Entity<UUID>
