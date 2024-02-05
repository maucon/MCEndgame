package de.fuballer.mcendgame.component.dungeon.boss

import de.fuballer.mcendgame.component.dungeon.boss.db.DungeonBossEntity
import de.fuballer.mcendgame.component.dungeon.boss.db.DungeonBossRepository
import de.fuballer.mcendgame.domain.entity.CustomEntityType
import de.fuballer.mcendgame.event.DungeonEnemySpawnedEvent
import de.fuballer.mcendgame.event.EventGateway
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.technical.extension.EntityExtension.setDisableDropEquipment
import de.fuballer.mcendgame.util.EntityUtil
import org.bukkit.Location
import org.bukkit.entity.Creature

@Component
class DungeonBossGenerationService(
    private val dungeonBossRepo: DungeonBossRepository,
) {
    fun spawnNewMapBoss(
        entityType: CustomEntityType,
        location: Location,
        mapTier: Int
    ): Creature {
        location.yaw = 180f

        val boss = EntityUtil.spawnCustomEntity(entityType, location, mapTier) as Creature

        boss.setDisableDropEquipment()
        boss.addPotionEffects(DungeonBossSettings.BOSS_POTION_EFFECTS)
        boss.removeWhenFarAway = false
        boss.setAI(false)

        val event = DungeonEnemySpawnedEvent(location.world!!, setOf(boss))
        EventGateway.apply(event)

        val entity = DungeonBossEntity(boss.uniqueId, mapTier)
        dungeonBossRepo.save(entity)

        return boss
    }
}