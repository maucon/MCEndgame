package de.fuballer.mcendgame.component.dungeon.enemy.team

import de.fuballer.mcendgame.event.DungeonEnemySpawnedEvent
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.framework.stereotype.LifeCycleListener
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.world.WorldLoadEvent
import org.bukkit.scoreboard.ScoreboardManager
import org.bukkit.scoreboard.Team

@Component
class TeamService(
    private val scoreboardManager: ScoreboardManager
) : Listener, LifeCycleListener {
    private var team: Team? = null

    @EventHandler
    fun onDungeonOpened(event: WorldLoadEvent) {
        createTeam()
    }

    @EventHandler
    fun onDungeonEnemySpawned(event: DungeonEnemySpawnedEvent) {
        event.entities.forEach { team!!.addEntry(it.uniqueId.toString()) }
    }

    private fun createTeam() {
        val board = scoreboardManager.mainScoreboard
        team = board.getTeam(TeamSettings.TEAM_NAME)
            ?: board.registerNewTeam(TeamSettings.TEAM_NAME)

        team!!.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER)
    }
}
