package de.fuballer.mcendgame.main.component.dungeon.generation

import de.fuballer.mcendgame.main.component.block.blocks.dungeon_device.DungeonDeviceBlockEntity
import de.fuballer.mcendgame.main.component.dungeon.enemy.EnemyGenerationService
import de.fuballer.mcendgame.main.component.dungeon.enemy.boss.BossGenerationService
import de.fuballer.mcendgame.main.component.dungeon.generation.builder.DungeonBuilderService
import de.fuballer.mcendgame.main.component.dungeon.generation.encounter.DungeonEncounterGenerationService
import de.fuballer.mcendgame.main.component.dungeon.seed.DungeonSeedService
import de.fuballer.mcendgame.main.component.dungeon.world.DungeonWorldService
import de.fuballer.mcendgame.main.component.item.custom.aspect.AspectItem
import de.fuballer.mcendgame.main.component.item.custom.aspect.AspectService
import de.fuballer.mcendgame.main.configuration.RuntimeConfig
import de.fuballer.mcendgame.main.messaging.dungeon.*
import de.fuballer.mcendgame.main.util.extension.mixin.PlayerEntityMixinExtension.getDungeonLevel
import de.maucon.mauconframework.command.CommandGateway
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.event.EventGateway
import de.maucon.mauconframework.event.EventSubscriber
import net.minecraft.core.GlobalPos
import kotlin.random.Random

@Injectable
class DungeonGenerationService(
    private val dungeonWorldService: DungeonWorldService,
    private val dungeonBuilderService: DungeonBuilderService,
    private val dungeonEncounterGenerationService: DungeonEncounterGenerationService,
    private val enemyGenerationService: EnemyGenerationService,
    private val bossGenerationService: BossGenerationService,
    private val dungeonSeedService: DungeonSeedService,
    private val aspectService: AspectService
) {
    @EventSubscriber(sync = true)
    fun on(event: OpenDungeonButtonPressedEvent) {
        val player = event.player
        val originWorld = player.level()
        val dungeonDevicePos = event.dungeonDeviceBlockEntity.blockPos
        val dungeonDeviceGlobalPos = GlobalPos(originWorld.dimension(), dungeonDevicePos)
        val affectingAspects = getAffectingAspects(event.dungeonDeviceBlockEntity)
        val playerSeed = dungeonSeedService.rollSeed(player)

        val playerDungeonLevel = player.getDungeonLevel().level
        val seed = playerSeed.seed

        val dungeonTypeCommand = SelectDungeonTypeCommand(playerSeed.type, affectingAspects)
        val dungeonType = CommandGateway.apply(dungeonTypeCommand).dungeonType

        val random = Random(seed)

        val (mapType, enemyTypes, bossTypes, applyMisc) = dungeonType.roll(random)

        val dungeonGeneratingEvent = DungeonGeneratingEvent(player, affectingAspects)
        EventGateway.publish(dungeonGeneratingEvent)

        val dungeonGenerateCommand = DungeonGenerateCommand(playerDungeonLevel, dungeonType.enemyCount, dungeonType.bossCount, affectingAspects)
        val (dungeonLevel, enemyCount, bossCount, _) = CommandGateway.apply(dungeonGenerateCommand)

        val layoutGenerator = mapType.layoutGeneratorProvider()
        val layout = layoutGenerator.generateDungeon(random, dungeonLevel, bossCount, enemyCount)

        RuntimeConfig.SERVER.execute {
            val dungeonWorld = dungeonWorldService.create(dungeonLevel, player, affectingAspects, dungeonType, dungeonDeviceGlobalPos)

            dungeonBuilderService.build(dungeonWorld, layout.rooms)
            dungeonEncounterGenerationService.generate(dungeonWorld, playerSeed, dungeonLevel, layout.encounterLocations, layout.startEncounterLocations, affectingAspects, random)

            enemyGenerationService.generate(dungeonWorld, dungeonLevel, enemyTypes, applyMisc, layout.enemySpawnPos)
            bossGenerationService.generate(dungeonWorld, bossTypes, applyMisc, layout.bossSpawnPos)

            val dungeonGeneratedEvent = DungeonGeneratedEvent(player, originWorld, dungeonWorld, layout.spawnPos, dungeonDevicePos)
            EventGateway.publish(dungeonGeneratedEvent)
        }
    }

    private fun getAffectingAspects(dungeonDeviceBlockEntity: DungeonDeviceBlockEntity): Map<AspectItem, Int> {
        val affectingItems = dungeonDeviceBlockEntity.getItems()
        val affectingAspects = aspectService.getAffectingAspects(affectingItems)
        dungeonDeviceBlockEntity.setChanged()
        return affectingAspects
    }
}