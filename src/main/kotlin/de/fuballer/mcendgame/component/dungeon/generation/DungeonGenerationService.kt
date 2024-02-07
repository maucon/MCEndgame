package de.fuballer.mcendgame.component.dungeon.generation

import com.sk89q.worldedit.bukkit.BukkitWorld
import com.sk89q.worldedit.extent.clipboard.Clipboard
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats
import com.sk89q.worldedit.math.BlockVector3
import com.sk89q.worldedit.math.transform.AffineTransform
import de.fuballer.mcendgame.component.custom_entity.types.CustomEntityType
import de.fuballer.mcendgame.component.dungeon.boss.DungeonBossGenerationService
import de.fuballer.mcendgame.component.dungeon.enemy.generation.EnemyGenerationService
import de.fuballer.mcendgame.component.dungeon.generation.data.LayoutTile
import de.fuballer.mcendgame.component.dungeon.leave.DungeonLeaveService
import de.fuballer.mcendgame.component.dungeon.leave.db.DungeonLeaveEntity
import de.fuballer.mcendgame.component.dungeon.leave.db.DungeonLeaveRepository
import de.fuballer.mcendgame.component.dungeon.type.DungeonTypeService
import de.fuballer.mcendgame.component.dungeon.world.WorldManageService
import de.fuballer.mcendgame.framework.annotation.Component
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Player
import java.awt.Point
import java.util.logging.Logger
import kotlin.random.Random

@Component
class DungeonGenerationService(
    private val dungeonLeaveRepo: DungeonLeaveRepository,
    private val worldManageService: WorldManageService,
    private val dungeonBossGenerationService: DungeonBossGenerationService,
    private val dungeonLeaveService: DungeonLeaveService,
    private val enemyGenerationService: EnemyGenerationService,
    private val dungeonTypeService: DungeonTypeService,
    private val logger: Logger
) {
    fun generateDungeon(
        player: Player,
        mapTier: Int,
        leaveLocation: Location
    ): Location {
        val world = worldManageService.createWorld(player, mapTier)
        val random = Random(world.seed)
        val dungeonType = dungeonTypeService.getRandomDungeonType(player)
        val rolledDungeonType = dungeonType.roll(random)

        val dungeonLayoutGenerator = DungeonLayoutGenerator()
        dungeonLayoutGenerator.generateDungeon(
            random,
            DungeonGenerationSettings.DUNGEON_WIDTH,
            DungeonGenerationSettings.DUNGEON_HEIGHT,
            DungeonGenerationSettings.DUNGEON_JUNCTION_PROBABILITY,
            DungeonGenerationSettings.DUNGEON_MAX_ADJACENT_TILE_DIFF
        )
        val layoutTiles = dungeonLayoutGenerator.getLayout()
        val bossRoomPos = dungeonLayoutGenerator.getBossRoomPos()
        val startRoomPos = dungeonLayoutGenerator.getStartRoomPos()

        runBlocking {
            launch { loadBossRoom(bossRoomPos, rolledDungeonType.mapType, world) }
            launch { loadLayoutTiles(layoutTiles, rolledDungeonType.mapType, world) }
        }

        spawnBoss(rolledDungeonType.bossEntityType, bossRoomPos, mapTier, world)
        enemyGenerationService.summonMonsters(random, rolledDungeonType.entityTypes, rolledDungeonType.specialEntityTypes, layoutTiles, startRoomPos, mapTier, world)

        val entity = DungeonLeaveEntity(world.name, mutableListOf(), leaveLocation)
        dungeonLeaveRepo.save(entity)

        val startPortalLocation = Location(world, (-16 * startRoomPos.x - 8).toDouble(), DungeonGenerationSettings.MOB_Y_POS, (-16 * startRoomPos.y - 8).toDouble())
        dungeonLeaveService.createPortal(world.name, startPortalLocation, true)

        createLeavePortals(layoutTiles, startRoomPos, bossRoomPos, world)

        return getStartLocation(layoutTiles, startRoomPos, world)
    }

    private fun loadBossRoom(
        bossRoomPos: Point,
        dungeonMapType: DungeonMapType,
        world: World
    ) {
        loadSchematic("boss", dungeonMapType, Location(world, (-bossRoomPos.x - 2) * 16.0, DungeonGenerationSettings.DUNGEON_Y_POS, bossRoomPos.y * 16.0), 0, world)
    }

    private fun loadLayoutTiles(
        layoutTiles: Array<Array<LayoutTile>>,
        dungeonMapType: DungeonMapType,
        world: World
    ) {
        for (x in layoutTiles.indices) {
            for (y in layoutTiles[0].indices) {

                val schematicName = layoutTiles[x][y].getSchematicName()
                val schematicLocation = Location(world, (-x - 1) * 16.0, DungeonGenerationSettings.DUNGEON_Y_POS, (-y - 1) * 16.0)
                val schematicRotation = (schematicName + schematicName).indexOf(layoutTiles[x][y].getRequiredWaysString())

                loadSchematic(schematicName, dungeonMapType, schematicLocation, schematicRotation, world)
            }
        }
    }

    private fun loadSchematic(
        schematicName: String,
        dungeonMapType: DungeonMapType,
        location: Location,
        rotation: Int,
        world: World
    ) {
        val schematicPath = DungeonGenerationSettings.getSchematicPath(dungeonMapType, schematicName)
        val inputStream = javaClass.getResourceAsStream(schematicPath)!!

        val format = ClipboardFormats.findByAlias("schem")
        if (format == null) {
            logger.severe("Couldn't find schematic: $schematicPath")
            return
        }

        val clipboard = format.load(inputStream)

        val transform = AffineTransform()
            .rotateY((rotation * 90).toDouble())

        pasteSchematic(clipboard, location, rotation, transform, world)
    }

    private fun pasteSchematic(
        clipboard: Clipboard,
        location: Location,
        rotation: Int,
        transform: AffineTransform?,
        world: World
    ) {
        val bukkitWorld = BukkitWorld(world)
        val vector = BlockVector3.at(
            location.x + if (rotation == 3 || rotation == 2) 15 else 0,
            location.y,
            location.z + if (rotation == 1 || rotation == 2) 15 else 0
        )

        clipboard.paste(bukkitWorld, vector, false, false, transform)
            .close()
    }

    private fun spawnBoss(
        entityType: CustomEntityType,
        bossRoomPos: Point,
        mapTier: Int, world: World
    ) {
        val bossLocation = Location(world, -bossRoomPos.x * 16.0 - 8, DungeonGenerationSettings.MOB_Y_POS - .2, -bossRoomPos.y * 16.0 + 24)
        dungeonBossGenerationService.spawnNewMapBoss(entityType, bossLocation, mapTier)
    }

    private fun getStartLocation(layoutTiles: Array<Array<LayoutTile>>, startRoomPos: Point, world: World): Location {
        var startLocation = Location(world, -startRoomPos.x * 16.0 - 8, DungeonGenerationSettings.MOB_Y_POS, -startRoomPos.y * 16.0 - 8)

        when (layoutTiles[startRoomPos.x][startRoomPos.y].getWays()[0]) {
            "up" -> {
                startLocation.yaw = 0f
                startLocation = startLocation.add(0.0, 0.0, 1.0)
            }

            "right" -> {
                startLocation.yaw = 90f
                startLocation = startLocation.add(-1.0, 0.0, 0.0)
            }

            "down" -> {
                startLocation.yaw = 180f
                startLocation = startLocation.add(0.0, 0.0, -1.0)
            }

            "left" -> {
                startLocation.yaw = 270f
                startLocation = startLocation.add(1.0, 0.0, 0.0)
            }
        }

        return startLocation
    }

    private fun createLeavePortals(
        layoutTiles: Array<Array<LayoutTile>>,
        startPoint: Point,
        bossPoint: Point,
        world: World
    ) {
        for (x in layoutTiles.indices) {
            for (y in layoutTiles[0].indices) {
                if (startPoint.x == x && startPoint.y == y) continue
                if (layoutTiles[x][y].getWaysAmount() != 1) continue

                dungeonLeaveService.createPortal(
                    world.name,
                    Location(world, (-x * 16.0 - 8), DungeonGenerationSettings.PORTAL_Y_POS, (-y * 16.0 - 8)),
                    false
                )
            }
        }

        dungeonLeaveService.createPortal(world.name, Location(world, -bossPoint.x * 16.0 - 8, DungeonGenerationSettings.PORTAL_Y_POS, -bossPoint.y * 16.0 + 24), false)
    }
}
