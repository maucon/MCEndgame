package de.fuballer.mcendgame.component.custom_entity.ability

import de.fuballer.mcendgame.component.custom_entity.ability.db.EntityAbilityEntity
import de.fuballer.mcendgame.component.custom_entity.ability.db.EntityAbilityRepository
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.framework.stereotype.LifeCycleListener
import de.fuballer.mcendgame.util.SchedulingUtil
import de.fuballer.mcendgame.util.extension.EntityExtension.getCustomEntityType
import de.fuballer.mcendgame.util.extension.EntityExtension.getMapTier
import org.bukkit.entity.Creature
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.EntityTargetEvent
import org.bukkit.plugin.java.JavaPlugin

@Component
class AbilityService(
    private val entityAbilityRepo: EntityAbilityRepository
) : Listener, LifeCycleListener {
    override fun initialize(plugin: JavaPlugin) {
        SchedulingUtil.runTaskTimer(AbilitySettings.INACTIVE_CHECK_PERIOD) {
            removeInactive()
        }
    }

    @EventHandler
    fun on(event: EntityTargetEvent) {
        val entity = event.entity as? Creature ?: return
        if (event.target !is Player) return

        val type = entity.getCustomEntityType() ?: return
        if (type.abilities == null) return

        val entityAbility = entityAbilityRepo.findById(entity.uniqueId)
            ?: EntityAbilityEntity(entity.uniqueId, type).also { entityAbilityRepo.save(it) }

        startAbilityRunner(entity, entityAbility)
    }

    @EventHandler
    fun on(event: EntityDeathEvent) {
        val entity = event.entity
        val uuid = entity.uniqueId

        val entityAbility = entityAbilityRepo.findById(uuid) ?: return
        entityAbility.abilityRunner?.cancel()

        entityAbilityRepo.deleteById(uuid)
    }

    private fun removeInactive() {
        entityAbilityRepo.findAll()
            .filter { it.abilityRunner == null || it.abilityRunner!!.isCancelled() }
            .forEach {
                entityAbilityRepo.delete(it)
            }
    }

    private fun startAbilityRunner(entity: Creature, entityAbility: EntityAbilityEntity) {
        val abilityRunner = entityAbility.abilityRunner
        if (abilityRunner != null && !abilityRunner.isCancelled()) return

        val mapTier = entity.getMapTier() ?: return

        val abilityCooldown = AbilitySettings.getAbilityCooldown(mapTier)
        val runner = EntityAbilityRunner(entity, entityAbility.entityType, abilityCooldown)
        runner.run()

        entityAbility.abilityRunner = runner
        entityAbilityRepo.save(entityAbility)
    }
}