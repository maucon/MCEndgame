package de.fuballer.mcendgame.component.custom_entity.ability

import de.fuballer.mcendgame.component.custom_entity.ability.db.EntityAbilityEntity
import de.fuballer.mcendgame.component.custom_entity.ability.db.EntityAbilityRepository
import de.fuballer.mcendgame.component.custom_entity.ability.runner.EntityRunner
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.extension.EntityExtension.getCustomEntityType
import de.fuballer.mcendgame.util.extension.EntityExtension.getMapTier
import org.bukkit.entity.Creature
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.EntityTargetEvent
import java.util.*

@Component
class AbilityService(
    private val entityAbilityRepo: EntityAbilityRepository
) : Listener {
    @EventHandler
    fun on(event: EntityTargetEvent) {
        val entity = event.entity as? Creature ?: return
        if (event.target !is Player) return

        val type = entity.getCustomEntityType() ?: return
        if (type.abilities == null) return

        val entityAbility = entityAbilityRepo.findById(entity.uniqueId)
            ?: EntityAbilityEntity(entity.uniqueId, type).also { entityAbilityRepo.save(it) }

        val runnerInactive = entityAbility.runner?.isCancelled() ?: true

        if (runnerInactive) {
            startRunner(entity, entityAbility)
        }
    }

    @EventHandler
    fun on(event: EntityDeathEvent) {
        val id = event.entity.uniqueId
        removeRunner(id)
    }

    private fun startRunner(entity: Creature, entityAbility: EntityAbilityEntity) {
        val mapTier = entity.getMapTier() ?: return

        val abilityCooldown = AbilitySettings.getAbilityCooldown(mapTier)
        entityAbility.runner = EntityRunner(entity, entityAbility.entityType, abilityCooldown) { removeRunner(entityAbility.id) }

        entityAbilityRepo.save(entityAbility)
    }

    private fun removeRunner(id: UUID) {
        val entityAbility = entityAbilityRepo.findById(id) ?: return
        entityAbility.runner?.cancel()

        entityAbilityRepo.delete(entityAbility)
    }
}