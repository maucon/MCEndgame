package de.fuballer.mcendgame.component.dungeon.boss

import de.fuballer.mcendgame.component.dungeon.boss.db.DungeonBossesRepository
import de.fuballer.mcendgame.component.dungeon.enemy.EnemyHealingService.Companion.heal
import de.fuballer.mcendgame.component.dungeon.world.db.WorldManageRepository
import de.fuballer.mcendgame.component.portal.PortalService
import de.fuballer.mcendgame.event.DungeonCompleteEvent
import de.fuballer.mcendgame.event.DungeonEntityDeathEvent
import de.fuballer.mcendgame.event.EventGateway
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.EntityUtil
import de.fuballer.mcendgame.util.extension.EntityExtension.getLootMultiplier
import de.fuballer.mcendgame.util.extension.EntityExtension.getPortalLocation
import de.fuballer.mcendgame.util.extension.EntityExtension.isBoss
import de.fuballer.mcendgame.util.extension.EntityExtension.setLootMultiplier
import de.fuballer.mcendgame.util.extension.WorldExtension.isDungeonWorld
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Creature
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent

@Component
class DungeonBossService(
    private val dungeonBossesRepo: DungeonBossesRepository,
    private val worldManageRepo: WorldManageRepository,
    private val portalService: PortalService
) : Listener {
    @EventHandler
    fun on(event: DungeonEntityDeathEvent) {
        val entity = event.entity
        if (!entity.isBoss()) return

        val bossWorld = entity.world
        val bossesEntity = dungeonBossesRepo.findByWorld(bossWorld) ?: return
        val dungeonWorld = worldManageRepo.getById(bossWorld.name)

        val portalLocation = entity.getPortalLocation()!!
        portalLocation.world = entity.world
        portalService.createPortal(portalLocation, bossesEntity.leaveLocation)

        empowerOtherBosses(bossesEntity.bosses)

        if (bossesEntity.progressGranted) return

        bossesEntity.progressGranted = true
        dungeonBossesRepo.save(bossesEntity)

        val dungeonCompleteEvent = DungeonCompleteEvent(dungeonWorld.player, bossesEntity.mapTier, bossWorld)
        EventGateway.apply(dungeonCompleteEvent)
    }

    @EventHandler
    fun on(event: EntityDamageEvent) { // FIXME
        val entity = event.entity
        if (!entity.world.isDungeonWorld()) return
        if (!entity.isBoss()) return

        (entity as LivingEntity).setAI(true)
    }

    private fun empowerOtherBosses(bosses: List<Creature>) {
        bosses.filter { it.isValid }
            .onEach {
                EntityUtil.increaseBaseAttribute(it, Attribute.GENERIC_MAX_HEALTH, 1.15)
                EntityUtil.increaseBaseAttribute(it, Attribute.GENERIC_ATTACK_DAMAGE, 1.1)
                EntityUtil.increaseBaseAttribute(it, Attribute.GENERIC_MOVEMENT_SPEED, 1.05)

                it.heal()

                val newLootMultiplier = it.getLootMultiplier() * DungeonBossSettings.EMPOWERED_LOOT_MULTIPLIER
                it.setLootMultiplier(newLootMultiplier)
            }
    }
}