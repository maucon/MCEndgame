package de.fuballer.mcendgame.component.dungeon.killstreak.db

import de.fuballer.mcendgame.framework.InMemoryMapRepository
import de.fuballer.mcendgame.framework.annotation.Component
import org.bukkit.entity.Player

@Component
class KillStreakRepository : InMemoryMapRepository<String, KillStreakEntity>() {
    fun findAllByPlayerInBossBar(player: Player) =
        findAll().filter { it.bossBar.players.contains(player) }
}
