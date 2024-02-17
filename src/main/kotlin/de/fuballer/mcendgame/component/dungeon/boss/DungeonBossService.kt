package de.fuballer.mcendgame.component.dungeon.boss

import de.fuballer.mcendgame.component.corruption.CorruptionSettings
import de.fuballer.mcendgame.component.dungeon.boss.db.DungeonBossRepository
import de.fuballer.mcendgame.component.dungeon.world.db.WorldManageRepository
import de.fuballer.mcendgame.event.DungeonCompleteEvent
import de.fuballer.mcendgame.event.DungeonEntityDeathEvent
import de.fuballer.mcendgame.event.EventGateway
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.extension.WorldExtension.isDungeonWorld
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.inventory.ItemStack
import kotlin.random.Random

@Component
class DungeonBossService(
    private val dungeonBossRepo: DungeonBossRepository,
    private val worldManageRepo: WorldManageRepository
) : Listener {
    @EventHandler
    fun on(event: DungeonEntityDeathEvent) {
        val entity = event.entity
        val uuid = entity.uniqueId

        val mapTier = dungeonBossRepo.findById(uuid)?.mapTier ?: return

        val world = entity.world
        val dungeonWorld = worldManageRepo.getById(world.name)

        dropBossLoot(entity, mapTier)

        val dungeonCompleteEvent = DungeonCompleteEvent(dungeonWorld.player, mapTier, world)
        EventGateway.apply(dungeonCompleteEvent)

        dungeonBossRepo.delete(uuid)
    }

    @EventHandler
    fun on(event: EntityDamageEvent) {
        val entity = event.entity
        if (!event.entity.world.isDungeonWorld()) return

        if (dungeonBossRepo.exists(entity.uniqueId)) (entity as LivingEntity).setAI(true)
    }

    private fun dropBossLoot(
        entity: Entity,
        mapTier: Int
    ) {
        val world = entity.world
        val location = entity.location

        val corruptionChance = DungeonBossSettings.calculateCorruptDropChance(mapTier)
        dropCorruptionHearts(CorruptionSettings.getCorruptionItem(), corruptionChance, world, location)

        val doubleCorruptionChance = DungeonBossSettings.calculateDoubleCorruptDropChance(mapTier)
        dropCorruptionHearts(CorruptionSettings.getDoubleCorruptionItem(), doubleCorruptionChance, world, location)
    }

    private fun dropCorruptionHearts(
        corruptionHeart: ItemStack,
        chance: Double,
        world: World,
        loc: Location
    ) {
        val restChance = chance % 1
        val extraDrop = Random.nextDouble() < restChance
        val corruptionCount = chance.toInt() + if (extraDrop) 1 else 0

        corruptionHeart.amount = corruptionCount
        world.dropItemNaturally(loc, corruptionHeart)
    }
}