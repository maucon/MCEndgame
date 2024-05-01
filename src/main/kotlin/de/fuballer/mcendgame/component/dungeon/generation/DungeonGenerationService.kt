package de.fuballer.mcendgame.component.dungeon.generation

import de.fuballer.mcendgame.component.custom_entity.types.CustomEntityType
import de.fuballer.mcendgame.component.dungeon.boss.DungeonBossGenerationService
import de.fuballer.mcendgame.component.dungeon.enemy.generation.EnemyGenerationService
import de.fuballer.mcendgame.component.dungeon.generation.data.Layout
import de.fuballer.mcendgame.component.dungeon.generation.data.SpawnLocationType
import de.fuballer.mcendgame.component.dungeon.type.DungeonTypeService
import de.fuballer.mcendgame.component.dungeon.world.WorldManageService
import de.fuballer.mcendgame.component.portal.PortalService
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
        val world = worldManageService.createWorld(player, mapTier)
        val random = Random(world.seed)
        val dungeonType = dungeonTypeService.getNextDungeonType(player)
        val (mapType, entityTypes, specialEntityTypes, bossEntityType) = dungeonType.roll(random)

        val layoutGenerator = mapType.layoutGeneratorProvider()
        val layout = layoutGenerator.generateDungeon(random, mapTier)

        dungeonBuilderService.build(world, layout.tiles)
        generateEnemies(layout, random, world, mapTier, entityTypes, specialEntityTypes)

        val startLocation = Location(world, 5.0, 1.5, 5.0)
        portalService.createPortal(startLocation, leaveLocation, isInitiallyActive = true)

        return startLocation
    }

    private fun generateEnemies(
        layout: Layout,
        random: Random,
        world: World,
        mapTier: Int,
        entityTypes: List<RandomOption<CustomEntityType>>,
        specialEntityTypes: List<RandomOption<CustomEntityType>>
    ) {
        val normalEnemyLocations = layout.spawnLocations
            .filter { it.type == SpawnLocationType.NORMAL }
            .map { VectorUtil.toLocation(world, it.location) }

        val specialEnemyLocations = layout.spawnLocations
            .filter { it.type == SpawnLocationType.SPECIAL }
            .map { VectorUtil.toLocation(world, it.location) }

        // TODO activate when generating a boss room with boss spawn location
        // val bossLocation = layout.spawnLocations
        //     .filter { it.type == SpawnLocationType.BOSS }
        //     .map { VectorUtil.toLocation(world, it.location) }
        //     .random()

        enemyGenerationService.generate(random, mapTier, world, entityTypes, normalEnemyLocations, specialEntityTypes, specialEnemyLocations)
        // bossGenerationService.generate(bossEntityType, bossLocation, mapTier)
    }
}