package de.fuballer.mcendgame.component.dungeon.boss

import de.fuballer.mcendgame.component.crafting.corruption.CorruptionSettings
import de.fuballer.mcendgame.component.crafting.imitation.ImitationSettings
import de.fuballer.mcendgame.component.crafting.refinement.RefinementSettings
import de.fuballer.mcendgame.component.crafting.reshaping.ReshapingSettings
import de.fuballer.mcendgame.component.crafting.transfiguration.TransfigurationSettings
import de.fuballer.mcendgame.component.dungeon.boss.db.DungeonBossesRepository
import de.fuballer.mcendgame.component.dungeon.world.db.WorldManageRepository
import de.fuballer.mcendgame.component.portal.PortalService
import de.fuballer.mcendgame.event.DungeonCompleteEvent
import de.fuballer.mcendgame.event.DungeonEntityDeathEvent
import de.fuballer.mcendgame.event.EventGateway
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.extension.EntityExtension.isBoss
import de.fuballer.mcendgame.util.extension.WorldExtension.isDungeonWorld
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Creature
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.inventory.ItemStack
import kotlin.random.Random

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

        portalService.createPortal(entity.location, bossesEntity.leaveLocation)

        dropBossLoot(entity, bossesEntity.mapTier)
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
                // TODO empower stats and drops
            }
    }

    private fun dropBossLoot(
        entity: Entity,
        mapTier: Int
    ) {
        val world = entity.world
        val location = entity.location

        val corruptionChance = DungeonBossSettings.calculateCorruptDropChance(mapTier)
        dropCorruptionHearts(CorruptionSettings.getCorruptionItem(), corruptionChance, world, location)

        val doubleCorruptionChance = DungeonBossSettings.calculateDoubleCorruptDropChance(mapTier)
        dropCorruptionHearts(CorruptionSettings.getDoubleCorruptionItem(), doubleCorruptionChance, world, location)

        // TODO only for testing -> remove
        world.dropItemNaturally(location, ImitationSettings.getImitationItem())
        world.dropItemNaturally(location, RefinementSettings.getRefinementItem())
        world.dropItemNaturally(location, ReshapingSettings.getReshapingItem())
        world.dropItemNaturally(location, TransfigurationSettings.getTransfigurationItem())
    }

    private fun dropCorruptionHearts(
        corruptionHeart: ItemStack,
        chance: Double,
        world: World,
        location: Location
    ) {
        val restChance = chance % 1
        val extraDrop = Random.nextDouble() < restChance
        val corruptionCount = chance.toInt() + if (extraDrop) 1 else 0

        corruptionHeart.amount = corruptionCount
        world.dropItemNaturally(location, corruptionHeart)
    }
}