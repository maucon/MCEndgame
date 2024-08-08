package de.fuballer.mcendgame.component.dungeon.seed

import de.fuballer.mcendgame.component.dungeon.seed.db.DungeonSeedRepository
import de.fuballer.mcendgame.framework.annotation.Service
import org.bukkit.entity.Player
import java.util.*

@Service
class DungeonSeedService(
    private val playerDungeonSeedRepo: DungeonSeedRepository
) {
    fun getSeed(player: Player): Long {
        val seed = playerDungeonSeedRepo.findById(player.uniqueId)?.seed
            ?: "${UUID.randomUUID()}"

        playerDungeonSeedRepo.deleteById(player.uniqueId)

        return seed.hashCode().toLong()
    }
}