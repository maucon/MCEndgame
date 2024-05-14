package de.fuballer.mcendgame.component.dungeon.generation

import de.fuballer.mcendgame.component.custom_entity.types.CustomEntityType
import de.fuballer.mcendgame.component.dungeon.boss.DungeonBossGenerationService
import de.fuballer.mcendgame.component.dungeon.enemy.generation.EnemyGenerationService
import de.fuballer.mcendgame.component.dungeon.generation.data.Layout
import de.fuballer.mcendgame.component.dungeon.type.DungeonTypeService
import de.fuballer.mcendgame.component.dungeon.world.WorldManageService
import de.fuballer.mcendgame.component.portal.PortalService
import de.fuballer.mcendgame.event.DungeonGeneratedEvent
import de.fuballer.mcendgame.event.EventGateway
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.VectorUtil
import de.fuballer.mcendgame.util.random.RandomOption
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Player
import kotlin.random.Random

@Component
class DungeonGenerationService(
    private val worldManageService: WorldManageService,
    private val dungeonBuilderService: DungeonBuilderService,
    private val dungeonTypeService: DungeonTypeService,
    private val enemyGenerationService: EnemyGenerationService,
    private val bossGenerationService: DungeonBossGenerationService,
    private val portalService: PortalService
) {
    fun generateDungeon(
        player: Player,
        mapTier: Int,
        leaveLocation: Location
    ): Location {
        val world = worldManageService.createWorld(player)
        val random = Random(world.seed)
        val dungeonType = dungeonTypeService.getNextDungeonType(player)
        val (mapType, entityTypes, bossEntityTypes) = dungeonType.roll(random)

        val layoutGenerator = mapType.layoutGeneratorProvider()
        val layout = layoutGenerator.generateDungeon(random, mapTier)

        dungeonBuilderService.build(world, layout.tiles)

        generateEnemies(layout, random, world, mapTier, entityTypes)
        generateBosses(layout, world, mapTier, bossEntityTypes, leaveLocation)

        val startLocation = VectorUtil.toLocation(world, layout.startLocation.location, layout.startLocation.rotation)
        portalService.createPortal(startLocation, leaveLocation)

        val event = DungeonGeneratedEvent(world, leaveLocation)
        EventGateway.apply(event)

        return startLocation
    }

    private fun generateEnemies(
        layout: Layout,
        random: Random,
        world: World,
        mapTier: Int,
        entityTypes: List<RandomOption<CustomEntityType>>,
    ) {
        val enemySpawnLocations = layout.enemySpawnLocations
            .map { VectorUtil.toLocation(world, it.location) }

        enemyGenerationService.generate(random, mapTier, world, entityTypes, enemySpawnLocations)
    }

    private fun generateBosses(
        layout: Layout,
        world: World,
        mapTier: Int,
        bossEntityTypes: List<CustomEntityType>,
        leaveLocation: Location
    ) {
        val bossSpawnLocations = layout.bossSpawnLocations
            .map { VectorUtil.toLocation(world, it.location, it.rotation) }

        bossGenerationService.generate(mapTier, bossEntityTypes, world, bossSpawnLocations, leaveLocation)
    }
}