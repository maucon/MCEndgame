package de.fuballer.mcendgame.component.dungeon.generation

import de.fuballer.mcendgame.component.dungeon.boss.DungeonBossGenerationService
import de.fuballer.mcendgame.component.dungeon.enemy.generation.EnemyGenerationService
import de.fuballer.mcendgame.component.dungeon.enemy.generation.EnemySpawnLocations
import de.fuballer.mcendgame.component.dungeon.generation.data.VectorUtil
import de.fuballer.mcendgame.component.dungeon.type.DungeonTypeService
import de.fuballer.mcendgame.component.dungeon.world.WorldManageService
import de.fuballer.mcendgame.component.portal.PortalService
import de.fuballer.mcendgame.framework.annotation.Component
import org.bukkit.Location
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

        val layoutGenerator = mapType.layoutGenerator
        val layout = layoutGenerator.generateDungeon(random, mapTier)

        dungeonBuilderService.build(mapType.schematicFolder, layout.tiles, world)

        val normalEnemyLocations = layout.spawnLocations.toMutableList()
            .map { VectorUtil.toLocation(world, it) }

        val possibleSpecialEnemyLocations = listOf<Location>()
        val enemyLocations = EnemySpawnLocations(world, normalEnemyLocations, possibleSpecialEnemyLocations)

        // TODO get boss location

        enemyGenerationService.generate(random, entityTypes, specialEntityTypes, enemyLocations, mapTier)
//        bossGenerationService.generate(bossEntityType, bossLocation, mapTier)

        val startLocation = Location(world, 5.0, 1.5, 5.0)
        portalService.createPortal(startLocation, leaveLocation, isInitiallyActive = true)

        // TODO spawn inactive portals in bossroom etc

        return startLocation
    }
}