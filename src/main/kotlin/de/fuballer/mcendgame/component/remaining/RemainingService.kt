package de.fuballer.mcendgame.component.remaining

import de.fuballer.mcendgame.component.dungeon.enemy.custom_entity.data.DataTypeKeys
import de.fuballer.mcendgame.component.remaining.db.RemainingEntity
import de.fuballer.mcendgame.component.remaining.db.RemainingRepository
import de.fuballer.mcendgame.event.DungeonCompleteEvent
import de.fuballer.mcendgame.event.DungeonWorldDeleteEvent
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.PersistentDataUtil
import de.fuballer.mcendgame.util.WorldUtil
import org.bukkit.World
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent

@Component
class RemainingService(
    private val remainingRepo: RemainingRepository
) : Listener {
    @EventHandler
    fun onDungeonComplete(event: DungeonCompleteEvent) {
        val remaining = remainingRepo.findById(event.world.name) ?: return
        remaining.bossAlive = false

        remainingRepo.save(remaining)
    }

    @EventHandler
    fun onDungeonWorldDelete(event: DungeonWorldDeleteEvent) {
        remainingRepo.delete(event.world.name)
    }

    @EventHandler
    fun onEntityDeath(event: EntityDeathEvent) {
        val entity = event.entity
        if (PersistentDataUtil.getValue(entity, DataTypeKeys.IS_ENEMY) != true) return

        val world = entity.world
        if (WorldUtil.isNotDungeonWorld(world)) return

        addMobs(world, -1)
    }

    fun addMobs(world: World, count: Int) {
        val name = world.name

        val entity = remainingRepo.findById(name)
            ?: RemainingEntity(name)

        entity.remaining += count
        remainingRepo.save(entity)
    }
}
