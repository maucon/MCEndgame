package de.fuballer.mcendgame.component.dungeon.killstreak.db

import de.fuballer.mcendgame.framework.stereotype.Entity
import de.fuballer.mcendgame.technical.TimerTask
import org.bukkit.boss.BossBar

data class KillStreakEntity(
    override var id: String,
    var bossBar: BossBar,

    var streak: Int = 0,
    var timer: Long = 0,
    var updateTask: TimerTask? = null
) : Entity<String>
