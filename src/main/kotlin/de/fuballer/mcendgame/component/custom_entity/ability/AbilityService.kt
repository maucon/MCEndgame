package de.fuballer.mcendgame.component.custom_entity.ability

import de.fuballer.mcendgame.component.custom_entity.ability.db.EntityAbilityEntity
import de.fuballer.mcendgame.component.custom_entity.ability.db.EntityAbilityRepository
import de.fuballer.mcendgame.domain.TimerTask
import de.fuballer.mcendgame.domain.persistent_data.TypeKeys
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.framework.stereotype.LifeCycleListener
import de.fuballer.mcendgame.util.PersistentDataUtil
import de.fuballer.mcendgame.util.WorldUtil
import org.bukkit.entity.Creature
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.EntityTargetEvent
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

@Component
class AbilityService(
    private val entityAbilityRepo: EntityAbilityRepository
) : Listener, LifeCycleListener {
    override fun initialize(plugin: JavaPlugin) {
        startInactiveCheckTimer()
    }

    @EventHandler
    fun onEntityTarget(event: EntityTargetEvent) {
        val entity = event.entity as? Creature ?: return
        if (WorldUtil.isNotDungeonWorld(entity.world)) return
        if (event.target !is Player) return

        val type = PersistentDataUtil.getValue(entity, TypeKeys.CUSTOM_ENTITY_TYPE) ?: return
        if (type.abilities == null) return

        val entityAbility = entityAbilityRepo.findById(entity.uniqueId)
            ?: EntityAbilityEntity(entity.uniqueId, type).also { entityAbilityRepo.save(it) }

        startAbilityRunner(entity, entityAbility)
    }

    @EventHandler
    fun onEntityDeath(event: EntityDeathEvent) {
        val entity = event.entity
        val uuid = entity.uniqueId

        val entityAbility = entityAbilityRepo.findById(uuid) ?: return
        entityAbility.abilityRunner?.cancel()

        entityAbilityRepo.delete(uuid)
    }

    private fun startInactiveCheckTimer() {
        Timer().schedule(
            TimerTask { removeInactive() },
            AbilitySettings.INACTIVE_CHECK_PERIOD,
            AbilitySettings.INACTIVE_CHECK_PERIOD
        )
    }

    private fun removeInactive() {
        entityAbilityRepo.findAll()
            .filter { it.abilityRunner == null || it.abilityRunner!!.isCancelled() }
            .forEach {
                entityAbilityRepo.delete(it.id)
            }
    }

    private fun startAbilityRunner(entity: Creature, entityAbility: EntityAbilityEntity) {
        val abilityRunner = entityAbility.abilityRunner
        if (abilityRunner != null && !abilityRunner.isCancelled()) return

        val mapTier = PersistentDataUtil.getValue(entity, TypeKeys.MAP_TIER) ?: return

        val abilityCooldown = AbilitySettings.getAbilityCooldown(mapTier)
        val runner = EntityAbilityRunner(entity, entityAbility.entityType, abilityCooldown)
        runner.run()

        entityAbility.abilityRunner = runner
        entityAbilityRepo.save(entityAbility)
    }
}