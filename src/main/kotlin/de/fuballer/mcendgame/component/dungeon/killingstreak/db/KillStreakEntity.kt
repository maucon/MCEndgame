package de.fuballer.mcendgame.component.dungeon.killingstreak.db

import de.fuballer.mcendgame.component.dungeon.killingstreak.KillStreakSettings
import de.fuballer.mcendgame.framework.stereotype.Entity
import de.fuballer.mcendgame.helper.TimerTask
import org.bukkit.Bukkit
import org.bukkit.boss.BossBar

data class KillStreakEntity(
    override var id: String,

    var streak: Int = 0,
    var timer: Long = 0,
    var bossBar: BossBar = Bukkit.createBossBar("0", KillStreakSettings.BAR_COLOR, KillStreakSettings.BAR_STYLE)
        .apply { progress = 0.0 },
    var updateTask: TimerTask? = null
) : Entity<String>
