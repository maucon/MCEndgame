package de.fuballer.mcendgame.main.component.dungeon.generation

import de.fuballer.mcendgame.main.component.dungeon.generation.builder.DungeonBuilderService
import de.fuballer.mcendgame.main.component.dungeon.generation.layout.DungeonLayoutType
import de.fuballer.mcendgame.main.component.dungeon.world.DungeonWorldService
import de.fuballer.mcendgame.main.configuration.RuntimeConfig
import de.fuballer.mcendgame.main.messaging.dungeon.DungeonGeneratedEvent
import de.fuballer.mcendgame.main.messaging.dungeon.OpenTrainingDungeonButtonPressedEvent
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.event.EventGateway
import de.maucon.mauconframework.event.EventSubscriber
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.GlobalPos
import kotlin.random.Random

@Injectable
class TrainingDungeonGenerationService(
    private val dungeonWorldService: DungeonWorldService,
    private val dungeonBuilderService: DungeonBuilderService,
) {
    @EventSubscriber(sync = true)
    fun on(event: OpenTrainingDungeonButtonPressedEvent) {
        val player = event.player
        val originWorld = player.entityWorld as ServerWorld
        val dungeonDevicePos = event.blockEntity.pos
        val dungeonDeviceGlobalPos = GlobalPos(originWorld.registryKey, dungeonDevicePos)

        val random = Random.Default

        val mapType = DungeonLayoutType.TRAINING
        val layoutGenerator = mapType.layoutGeneratorProvider()
        val layout = layoutGenerator.generateDungeon(random, 1, 1)

        RuntimeConfig.SERVER.execute {
            val dungeonWorld = dungeonWorldService.createTraining(player, dungeonDeviceGlobalPos)

            dungeonBuilderService.build(dungeonWorld, layout.rooms)

            //enemyGenerationService.generate(dungeonWorld, dungeonLevel, enemyTypes, applyMisc, layout.enemySpawnPos)

            val dungeonGeneratedEvent = DungeonGeneratedEvent(player, originWorld, dungeonWorld, layout.spawnPos, dungeonDevicePos, isTraining = true)
            EventGateway.publish(dungeonGeneratedEvent)
        }
    }
}