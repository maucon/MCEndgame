package de.fuballer.mcendgame.component.dungeon.boss

import de.fuballer.mcendgame.component.corruption.CorruptionSettings
import de.fuballer.mcendgame.component.dungeon.boss.db.DungeonBossEntity
import de.fuballer.mcendgame.component.dungeon.boss.db.DungeonBossRepository
import de.fuballer.mcendgame.component.dungeon.world.db.WorldManageRepository
import de.fuballer.mcendgame.domain.entity.CustomEntityType
import de.fuballer.mcendgame.domain.persistent_data.DataTypeKeys
import de.fuballer.mcendgame.event.DungeonCompleteEvent
import de.fuballer.mcendgame.event.DungeonEnemySpawnedEvent
import de.fuballer.mcendgame.event.EventGateway
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.EntityUtil
import de.fuballer.mcendgame.util.PersistentDataUtil
import de.fuballer.mcendgame.util.WorldUtil
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Creature
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.inventory.ItemStack
import java.util.*

@Component
class DungeonBossService(
    private val dungeonBossRepo: DungeonBossRepository,
    private val worldManageRepo: WorldManageRepository
) : Listener {
    private val random = Random()

    @EventHandler
    fun onEntityDeath(event: EntityDeathEvent) {
        val entity = event.entity
        val uuid = entity.uniqueId

        val mapTier = dungeonBossRepo.findById(uuid)?.mapTier ?: return

        val world = entity.world
        val dungeonWorld = worldManageRepo.getById(world.name)

        event.drops.clear()
        dropBossLoot(entity, mapTier)

        val dungeonCompleteEvent = DungeonCompleteEvent(dungeonWorld.player, mapTier, world)
        EventGateway.apply(dungeonCompleteEvent)

        dungeonBossRepo.delete(uuid)
    }

    @EventHandler
    fun onEntityDamage(event: EntityDamageEvent) {
        val entity = event.entity
        if (WorldUtil.isNotDungeonWorld(event.entity.world)) return

        if (dungeonBossRepo.exists(entity.uniqueId)) (entity as LivingEntity).setAI(true)
    }

    fun spawnNewMapBoss(
        entityType: CustomEntityType,
        location: Location,
        mapTier: Int
    ): Creature {
        location.yaw = 180f

        val boss = EntityUtil.spawnCustomEntity(entityType, location, mapTier) as Creature

        PersistentDataUtil.setValue(boss, DataTypeKeys.DROP_EQUIPMENT, false)

        boss.addPotionEffects(DungeonBossSettings.BOSS_POTION_EFFECTS)
        boss.removeWhenFarAway = false
        boss.setAI(false)

        val event = DungeonEnemySpawnedEvent(location.world!!, setOf(boss))
        EventGateway.apply(event)

        val entity = DungeonBossEntity(boss.uniqueId, mapTier)
        dungeonBossRepo.save(entity)

        return boss
    }

    private fun dropBossLoot(
        entity: Entity,
        mapTier: Int
    ) {
        val world = entity.world
        val loc = entity.location

        val corruptionChance = DungeonBossSettings.calculateCorruptDropChance(mapTier)
        dropCorruptionHearts(CorruptionSettings.getCorruptionItem(), corruptionChance, world, loc)

        val doubleCorruptionChance = DungeonBossSettings.calculateDoubleCorruptDropChance(mapTier)
        dropCorruptionHearts(CorruptionSettings.getDoubleCorruptionItem(), doubleCorruptionChance, world, loc)
    }

    private fun dropCorruptionHearts(
        corruptionHeart: ItemStack,
        chance: Double,
        world: World,
        loc: Location
    ) {
        val restChance = chance % 1
        val extraDrop = random.nextDouble() < restChance
        val corruptionCount = chance.toInt() + if (extraDrop) 1 else 0

        corruptionHeart.amount = corruptionCount
        world.dropItemNaturally(loc, corruptionHeart)
    }
}
