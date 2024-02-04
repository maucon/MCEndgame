package de.fuballer.mcendgame.component.dungeon.seed

import de.fuballer.mcendgame.component.dungeon.seed.db.DungeonSeedRepository
import de.fuballer.mcendgame.framework.annotation.Component
import org.bukkit.entity.Player
import java.util.*

@Component
class DungeonSeedService(
    private val playerDungeonSeedRepo: DungeonSeedRepository
) {
    fun getSeed(player: Player): Long {
        val seed = playerDungeonSeedRepo.findById(player.uniqueId)?.seed
            ?: "${UUID.randomUUID()}"

        playerDungeonSeedRepo.delete(player.uniqueId)

        return seed.hashCode().toLong()
    }
}