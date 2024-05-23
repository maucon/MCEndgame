package de.fuballer.mcendgame.component.dungeon.boss

import de.fuballer.mcendgame.component.custom_entity.types.CustomEntityType
import de.fuballer.mcendgame.component.dungeon.boss.db.DungeonBossesEntity
import de.fuballer.mcendgame.component.dungeon.boss.db.DungeonBossesRepository
import de.fuballer.mcendgame.component.dungeon.generation.DungeonGenerationSettings
import de.fuballer.mcendgame.event.DungeonEnemySpawnedEvent
import de.fuballer.mcendgame.event.EventGateway
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.EntityUtil
import de.fuballer.mcendgame.util.extension.EntityExtension.setDisableDropEquipment
import de.fuballer.mcendgame.util.extension.EntityExtension.setIsBoss
import de.fuballer.mcendgame.util.extension.EntityExtension.setPortalLocation
import de.fuballer.mcendgame.util.extension.ListExtension.cycle
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Creature
import org.bukkit.inventory.ItemStack
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
        assert(locations.size == DungeonGenerationSettings.BOSS_AMOUNT) { "too few boss spawning locations" }
        assert(entityTypes.isNotEmpty()) { "boss entity types cannot be empty" }

        val bosses = locations.zip(entityTypes.cycle())
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
        boss.getAttribute(Attribute.GENERIC_FOLLOW_RANGE)?.baseValue = DungeonBossSettings.FOLLOW_RANGE
        boss.removeWhenFarAway = false

        if (entityType.isRanged) {
            val bow = ItemStack(Material.BOW)
            boss.equipment?.setItemInMainHand(bow)
        }

        return boss
    }
}