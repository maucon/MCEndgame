package de.fuballer.mcendgame.component.custom_entity.ability

import de.fuballer.mcendgame.component.custom_entity.ability.db.EntityAbilityEntity
import de.fuballer.mcendgame.component.custom_entity.ability.db.EntityAbilityRepository
import de.fuballer.mcendgame.component.custom_entity.data.CustomEntityType
import de.fuballer.mcendgame.component.custom_entity.data.DataTypeKeys
import de.fuballer.mcendgame.component.dungeon.world.db.WorldManageRepository
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.PersistentDataUtil
import de.fuballer.mcendgame.util.WorldUtil
import org.bukkit.entity.Creature
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.EntitySpawnEvent
import org.bukkit.event.entity.EntityTargetEvent

@Component
class AbilityService(
    private val entityAbilityRepo: EntityAbilityRepository,
    private val worldManageRepo: WorldManageRepository
) : Listener {
    @EventHandler
    fun onEntitySpawn(event: EntitySpawnEvent) {
        val entity = event.entity
        if (WorldUtil.isNotDungeonWorld(entity.world)) return

        val typeString = PersistentDataUtil.getValue(entity, DataTypeKeys.ENTITY_TYPE) ?: return
        val type = CustomEntityType.valueOf(typeString)

        if (type.abilities == null) return

        val entityAbility = EntityAbilityEntity(entity.uniqueId, type)
        entityAbilityRepo.save(entityAbility)
    }

    @EventHandler
    fun onEntityTarget(event: EntityTargetEvent) {
        val entity = event.entity
        if (WorldUtil.isNotDungeonWorld(entity.world)) return

        if (entityAbilityRepo.exists(entity.uniqueId)) {
            startAbilityRunner(event)
        }
    }

    @EventHandler
    fun onEntityDeath(event: EntityDeathEvent) {
        val entity = event.entity
        val uuid = entity.uniqueId

        val entityAbility = entityAbilityRepo.findById(uuid) ?: return
        entityAbility.abilityRunner?.cancel()

        entityAbilityRepo.delete(uuid)
    }

    private fun startAbilityRunner(event: EntityTargetEvent) {
        val entity = event.entity as Creature
        val worldName = entity.world.name
        val uuid = entity.uniqueId

        val entityAbility = entityAbilityRepo.findById(uuid) ?: return
        val abilityRunner = entityAbility.abilityRunner
        if (abilityRunner != null && abilityRunner.isCancelled()) return

        val mapTier = worldManageRepo.findById(worldName)?.mapTier ?: 1

        val runner = EntityAbilityRunner(entity, entityAbility.entityType, mapTier)
        runner.run()

        entityAbility.abilityRunner = runner
        entityAbilityRepo.save(entityAbility)
    }
}