package de.fuballer.mcendgame.component.dungeon.remaining

import de.fuballer.mcendgame.component.dungeon.remaining.db.RemainingEntity
import de.fuballer.mcendgame.component.dungeon.remaining.db.RemainingRepository
import de.fuballer.mcendgame.event.DungeonCompleteEvent
import de.fuballer.mcendgame.event.DungeonEnemySpawnedEvent
import de.fuballer.mcendgame.event.DungeonEntityDeathEvent
import de.fuballer.mcendgame.event.DungeonWorldDeleteEvent
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.extension.EntityExtension.isBoss
import de.fuballer.mcendgame.util.extension.EntityExtension.isEnemy
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

@Component
class RemainingService(
    private val remainingRepo: RemainingRepository
) : Listener {
    @EventHandler
    fun on(event: DungeonEnemySpawnedEvent) {
        val name = event.world.name

        val entity = remainingRepo.findById(name)
            ?: RemainingEntity(name)

        entity.remaining += event.entities.size
        remainingRepo.save(entity)
    }

    @EventHandler
    fun on(event: DungeonEntityDeathEvent) {
        val entity = event.entity
        if (!entity.isEnemy()) return

        val remainingEntity = remainingRepo.findById(entity.world.name) ?: return
        remainingEntity.remaining -= 1

        if (entity.isBoss()) {
            remainingEntity.bossesSlain += 1
        }

        remainingRepo.save(remainingEntity)
    }

    @EventHandler
    fun on(event: DungeonCompleteEvent) {
        val remaining = remainingRepo.findById(event.world.name) ?: return
        remaining.dungeonCompleted = true

        remainingRepo.save(remaining)
    }

    @EventHandler
    fun on(event: DungeonWorldDeleteEvent) {
        remainingRepo.deleteById(event.world.name)
    }
}