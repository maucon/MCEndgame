package de.fuballer.mcendgame.component.remaining

import de.fuballer.mcendgame.component.dungeon.enemy.custom_entity.DataTypeKeys
import de.fuballer.mcendgame.component.remaining.db.RemainingEntity
import de.fuballer.mcendgame.component.remaining.db.RemainingRepository
import de.fuballer.mcendgame.event.DungeonCompleteEvent
import de.fuballer.mcendgame.event.DungeonWorldDeleteEvent
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.PersistentDataUtil
import de.fuballer.mcendgame.util.WorldUtil
import org.bukkit.World
import org.bukkit.entity.Monster
import org.bukkit.event.entity.EntityDeathEvent

@Component
class RemainingService(
    private val remainingRepo: RemainingRepository
) {
    fun addMobs(world: World, count: Int) {
        val name = world.name

        val entity = remainingRepo.findById(name)
            ?: RemainingEntity(name)

        entity.remaining += count
        remainingRepo.save(entity)
    }

    fun onDungeonComplete(event: DungeonCompleteEvent) {
        val remaining = remainingRepo.findById(event.world.name) ?: return
        remaining.bossAlive = false

        remainingRepo.save(remaining)
    }

    fun onDungeonWorldDelete(event: DungeonWorldDeleteEvent) {
        remainingRepo.delete(event.world.name)
    }

    fun onEntityDeath(event: EntityDeathEvent) {
        if (event.entity !is Monster) return
        val entity = event.entity as Monster
        if (RemainingSettings.IGNORED_MOBS.contains(entity.type)) return

        if (PersistentDataUtil.getValue(entity, DataTypeKeys.IS_MINION) == true) return

        val world = entity.world
        if (WorldUtil.isNotDungeonWorld(world)) return

        addMobs(world, -1)
    }
}
