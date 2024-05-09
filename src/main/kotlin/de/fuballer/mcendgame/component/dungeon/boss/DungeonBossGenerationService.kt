package de.fuballer.mcendgame.component.dungeon.boss

import de.fuballer.mcendgame.component.custom_entity.types.CustomEntityType
import de.fuballer.mcendgame.component.dungeon.boss.db.DungeonBossesEntity
import de.fuballer.mcendgame.component.dungeon.boss.db.DungeonBossesRepository
import de.fuballer.mcendgame.event.DungeonEnemySpawnedEvent
import de.fuballer.mcendgame.event.EventGateway
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.EntityUtil
import de.fuballer.mcendgame.util.extension.EntityExtension.setDisableDropEquipment
import de.fuballer.mcendgame.util.extension.EntityExtension.setIsBoss
import de.fuballer.mcendgame.util.extension.EntityExtension.setPortalLocation
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Creature
import java.util.*

@Component
class DungeonBossGenerationService(
    private val dungeonBossesRepo: DungeonBossesRepository,
) {
    fun generate(
        mapTier: Int,
        entityTypes: List<CustomEntityType>,
        world: World,
        locations: List<Location>,
        leaveLocation: Location
    ) {
        // TODO what if there are not enough entity types?
        val bosses = locations.zip(entityTypes)
            .map { (location, entityType) -> spawnBoss(entityType, location, mapTier) }

        val entity = DungeonBossesEntity(UUID.randomUUID(), world, mapTier, bosses, leaveLocation)
        dungeonBossesRepo.save(entity)

        val event = DungeonEnemySpawnedEvent(world, bosses.toSet())
        EventGateway.apply(event)
    }

    private fun spawnBoss(
        entityType: CustomEntityType,
        location: Location,
        mapTier: Int
    ): Creature {
        val boss = EntityUtil.spawnCustomEntity(entityType, location, mapTier) as Creature

        boss.setIsBoss()
        boss.setPortalLocation(location)

        boss.setDisableDropEquipment()
        boss.addPotionEffects(DungeonBossSettings.BOSS_POTION_EFFECTS)
        boss.removeWhenFarAway = false
        boss.setAI(false) // TODO boss should unfreeze when targeting player

        return boss
    }
}