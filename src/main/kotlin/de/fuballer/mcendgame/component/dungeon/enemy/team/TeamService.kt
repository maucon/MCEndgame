package de.fuballer.mcendgame.component.dungeon.enemy.team

import de.fuballer.mcendgame.configuration.PluginConfiguration
import de.fuballer.mcendgame.event.DungeonEnemySpawnedEvent
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.framework.stereotype.LifeCycleListener
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.world.WorldLoadEvent
import org.bukkit.scoreboard.Team

const val teamName = "mcendgame_enemy"

@Component
class TeamService : Listener, LifeCycleListener {
    private var team: Team? = null

    @EventHandler
    fun onDungeonEnemySpawned(event: DungeonEnemySpawnedEvent) {
        event.entities.forEach { team!!.addEntry(it.uniqueId.toString()) }
    }

    @EventHandler
    fun onDungeonOpened(event: WorldLoadEvent) {
        createTeam()
    }

    private fun createTeam() {
        val board = PluginConfiguration.scoreboardManager().mainScoreboard
        team = board.getTeam(teamName) ?: board.registerNewTeam(teamName)
        team!!.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER)
    }
}