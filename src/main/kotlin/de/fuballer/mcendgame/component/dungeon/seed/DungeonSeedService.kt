package de.fuballer.mcendgame.component.dungeon.seed

import de.fuballer.mcendgame.component.dungeon.seed.player.db.PlayerDungeonSeedRepository
import de.fuballer.mcendgame.domain.technical.persistent_data.TypeKeys
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.PersistentDataUtil
import org.bukkit.World
import org.bukkit.entity.Player
import java.util.*

@Component
class DungeonSeedService(
    private val playerDungeonSeedRepo: PlayerDungeonSeedRepository
) {
    fun setSeed(player: Player, world: World) {
        val seed = playerDungeonSeedRepo.findById(player.uniqueId)?.seed
            ?: "${UUID.randomUUID()}"

        playerDungeonSeedRepo.delete(player.uniqueId)
        PersistentDataUtil.setValue(world, TypeKeys.SEED, seed)
    }
}