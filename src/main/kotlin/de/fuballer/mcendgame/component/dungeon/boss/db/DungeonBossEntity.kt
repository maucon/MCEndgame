package de.fuballer.mcendgame.component.dungeon.boss.db

import de.fuballer.mcendgame.component.dungeon.boss.data.BossType
import de.fuballer.mcendgame.framework.stereotype.Entity
import org.bukkit.scheduler.BukkitTask
import java.util.*

data class DungeonBossEntity(
    override var id: UUID,

    var mapTier: Int,
    var abilityTask: BukkitTask?,
    var type: BossType,
) : Entity<UUID>
