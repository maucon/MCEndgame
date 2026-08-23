package de.fuballer.mcendgame.main.component.dungeon.world

import de.fuballer.mcendgame.main.component.dungeon.type.DungeonType
import de.fuballer.mcendgame.main.component.dungeon.world.db.DungeonWorldEntity
import de.fuballer.mcendgame.main.component.dungeon.world.db.DungeonWorldRepository
import de.fuballer.mcendgame.main.component.item.custom.aspect.AspectItem
import de.fuballer.mcendgame.main.configuration.RuntimeConfig
import de.fuballer.mcendgame.main.functional.scheduler.Scheduler
import de.fuballer.mcendgame.main.messaging.server.ServerStartedEvent
import de.fuballer.mcendgame.main.messaging.server.ServerStoppingEvent
import de.fuballer.mcendgame.main.util.extension.mixin.WorldMixinExtension.setCreationTime
import de.fuballer.mcendgame.main.util.extension.mixin.WorldMixinExtension.setDungeonAspects
import de.fuballer.mcendgame.main.util.extension.mixin.WorldMixinExtension.setDungeonExitPos
import de.fuballer.mcendgame.main.util.extension.mixin.WorldMixinExtension.setDungeonLevel
import de.fuballer.mcendgame.main.util.extension.mixin.WorldMixinExtension.setDungeonSeed
import de.fuballer.mcendgame.main.util.extension.mixin.WorldMixinExtension.setDungeonType
import de.fuballer.mcendgame.main.util.extension.mixin.WorldMixinExtension.setOpener
import de.fuballer.mcendgame.main.util.extension.mixin.WorldMixinExtension.setTrainingDungeon
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.di.annotation.Logging
import de.maucon.mauconframework.event.EventGateway
import de.maucon.mauconframework.event.EventSubscriber
import net.minecraft.core.GlobalPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Player
import org.slf4j.Logger
import java.time.Instant

@Injectable
class DungeonWorldService(
    @Logging private val log: Logger,
    private val dungeonWorldRepo: DungeonWorldRepository,
    private val scheduler: Scheduler
) {
    @EventSubscriber(sync = true)
    fun on(event: ServerStartedEvent) {
        scheduler.repeating(DungeonWorldSettings.EMPTY_WORLD_CHECK_PERIOD) { deleteEmptyWorlds() }
    }

    @EventSubscriber(sync = true)
    fun on(event: ServerStoppingEvent) {
        dungeonWorldRepo.findAll()
            .forEach { deleteWorld(it) }
    }

    fun create(
        dungeonLevel: Int,
        opener: Player,
        dungeonSeed: Long,
        affectingAspects: Map<AspectItem, Int>,
        dungeonType: DungeonType,
        dungeonExitPos: GlobalPos,
        training: Boolean = false,
    ): ServerLevel {
        val dungeonWorld = RuntimeConfig.FANTASY
            .openTemporaryLevel(
                DungeonWorldSettings.generateIdentifier(),
                DungeonWorldSettings.getWorldConfig(
                    dungeonType.biome,
                    dungeonType.gameTime,
                )
            )
            .asLevel()

        dungeonWorld.setDungeonSeed(dungeonSeed)
        dungeonWorld.setDungeonLevel(dungeonLevel)
        dungeonWorld.setCreationTime(dungeonWorld.gameTime)
        dungeonWorld.setOpener(opener)
        dungeonWorld.setDungeonAspects(affectingAspects)
        dungeonWorld.setDungeonType(dungeonType)
        dungeonWorld.setDungeonExitPos(dungeonExitPos)
        if (training) dungeonWorld.setTrainingDungeon()

        val entity = DungeonWorldEntity(dungeonWorld)
        dungeonWorldRepo.save(entity)

        return dungeonWorld
    }

    fun createTraining(
        opener: Player,
        dungeonExitPos: GlobalPos,
    ) = create(1, opener, 0L, mapOf(), DungeonType.TRAINING, dungeonExitPos, training = true)

    private fun deleteEmptyWorlds() {
        log.info("Checking for empty worlds")

        dungeonWorldRepo.findAll()
            .map {
                updateDeleteTimer(it)
                dungeonWorldRepo.save(it)
            }
            .filter { it.emptySince.plusSeconds(DungeonWorldSettings.MAX_EMPTY_TIME) < Instant.now() }
            .forEach {
                log.warn("Dungeon world '${it.world.dimension().identifier()}' was empty for too long, deleting it!")
                deleteWorld(it)
            }
    }

    private fun updateDeleteTimer(entity: DungeonWorldEntity) {
        if (entity.world.players().isNotEmpty()) {
            entity.emptySince = Instant.now()
        }
    }

    private fun deleteWorld(entity: DungeonWorldEntity) {
        RuntimeConfig.FANTASY.tickDeleteLevel(entity.world)
        dungeonWorldRepo.delete(entity)

        val event = DungeonWorldClosedEvent(entity.world)
        EventGateway.publish(event)
    }
}