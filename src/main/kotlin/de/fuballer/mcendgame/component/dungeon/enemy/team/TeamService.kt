package de.fuballer.mcendgame.component.dungeon.enemy.team

import de.fuballer.mcendgame.configuration.PluginConfiguration
import de.fuballer.mcendgame.event.DungeonEnemySpawnedEvent
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.framework.stereotype.LifeCycleListener
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.world.WorldLoadEvent
import org.bukkit.scoreboard.Team

@Component
class TeamService : Listener, LifeCycleListener {
    private var team: Team? = null

    @EventHandler
    fun on(event: WorldLoadEvent) {
        createTeam()
    }

    @EventHandler
    fun on(event: DungeonEnemySpawnedEvent) {
        event.entities.forEach { team!!.addEntry(it.uniqueId.toString()) }
    }

    private fun createTeam() {
        val board = PluginConfiguration.scoreboardManager().mainScoreboard
        team = (board.getTeam(TeamSettings.TEAM_NAME) ?: board.registerNewTeam(TeamSettings.TEAM_NAME))
            .also { it.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER) }
    }
}
