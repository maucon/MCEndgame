package de.fuballer.mcendgame.component.dungeon.boss

import de.fuballer.mcendgame.component.corruption.CorruptionSettings
import de.fuballer.mcendgame.component.dungeon.boss.data.BossType
import de.fuballer.mcendgame.component.dungeon.boss.data.DungeonBossAbilitiesRunnable
import de.fuballer.mcendgame.component.dungeon.boss.db.DungeonBossEntity
import de.fuballer.mcendgame.component.dungeon.boss.db.DungeonBossRepository
import de.fuballer.mcendgame.component.dungeon.enemy.custom_entity.data.CustomEntityType
import de.fuballer.mcendgame.component.dungeon.enemy.custom_entity.data.DataTypeKeys
import de.fuballer.mcendgame.component.dungeon.world.db.WorldManageRepository
import de.fuballer.mcendgame.event.DungeonCompleteEvent
import de.fuballer.mcendgame.event.DungeonEnemySpawnedEvent
import de.fuballer.mcendgame.event.EventGateway
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.DungeonUtil
import de.fuballer.mcendgame.util.PersistentDataUtil
import de.fuballer.mcendgame.util.PluginUtil.runTaskTimer
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
import org.bukkit.event.entity.EntityTargetEvent
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
    fun onEntityTarget(event: EntityTargetEvent) {
        val entity = event.entity
        if (WorldUtil.isNotDungeonWorld(entity.world)) return

        if (dungeonBossRepo.exists(entity.uniqueId)) onBossTarget(event)
    }

    @EventHandler
    fun onEntityDamage(event: EntityDamageEvent) {
        val entity = event.entity
        if (WorldUtil.isNotDungeonWorld(event.entity.world)) return

        if (dungeonBossRepo.exists(entity.uniqueId)) (entity as LivingEntity).setAI(true)
    }

    fun spawnNewMapBoss(
        bossType: BossType,
        location: Location,
        mapTier: Int
    ): Creature {
        location.yaw = 180f

        val boss = CustomEntityType.spawnCustomEntity(bossType.customEntityType, location, mapTier) as Creature

        PersistentDataUtil.setValue(boss, DataTypeKeys.DROP_EQUIPMENT, false)
        setBossAttributes(boss, mapTier, bossType)

        val event = DungeonEnemySpawnedEvent(location.world!!, setOf(boss))
        EventGateway.apply(event)

        val entity = DungeonBossEntity(boss.uniqueId, mapTier, null, bossType)
        dungeonBossRepo.save(entity)

        return boss
    }

    private fun setBossAttributes(boss: Creature, mapTier: Int, bossType: BossType) {
        boss.addPotionEffects(DungeonBossSettings.BOSS_POTION_EFFECTS)
        boss.removeWhenFarAway = false
        boss.setAI(false)

        val newHealth = bossType.baseHealth + mapTier * bossType.healthPerTier
        val newDamage = bossType.baseDamage + mapTier * bossType.damagePerTier
        val newSpeed = bossType.speed

        DungeonUtil.setBasicAttributes(boss, newHealth, newDamage, newSpeed)
    }

    private fun onBossTarget(event: EntityTargetEvent) {
        val entity = event.entity
        val worldName = entity.world.name
        val uuid = entity.uniqueId

        val dungeonBossEntity = dungeonBossRepo.findById(uuid) ?: return
        val abilityTask = dungeonBossEntity.abilityTask
        if (abilityTask != null && !abilityTask.isCancelled) return

        val mapTier = worldManageRepo.findById(worldName)?.mapTier ?: 1
        val runnable = DungeonBossAbilitiesRunnable(
            dungeonBossRepo,
            entity as Creature,
            dungeonBossEntity.type,
            mapTier
        ).runTaskTimer(0, DungeonBossSettings.BOSS_ABILITY_CHECK_PERIOD.toLong())

        dungeonBossEntity.abilityTask = runnable
        dungeonBossRepo.save(dungeonBossEntity)
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
