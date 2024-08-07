package de.fuballer.mcendgame.component.dungeon.type

import de.fuballer.mcendgame.component.dungeon.type.db.PlayerDungeonTypeEntity
import de.fuballer.mcendgame.component.dungeon.type.db.PlayerDungeonTypeRepository
import de.fuballer.mcendgame.event.DungeonCompleteEvent
import de.fuballer.mcendgame.framework.annotation.Service
import de.fuballer.mcendgame.util.random.RandomUtil
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

@Service
class DungeonTypeService(
    private val playerDungeonTypeRepo: PlayerDungeonTypeRepository
) : Listener {
    @EventHandler
    fun on(event: DungeonCompleteEvent) {
        playerDungeonTypeRepo.deleteById(event.player.uniqueId)
    }

    fun getNextDungeonType(player: Player): DungeonType {
        val playerId = player.uniqueId

        if (playerDungeonTypeRepo.exists(playerId)) {
            val entity = playerDungeonTypeRepo.getById(playerId)
            return entity.dungeonType
        }

        val dungeonType = RandomUtil.pick(DungeonTypeSettings.DUNGEON_TYPE_WEIGHTS).option

        val entity = PlayerDungeonTypeEntity(playerId, dungeonType)
            .also { playerDungeonTypeRepo.save(it) }

        return entity.dungeonType
    }
}
