package de.fuballer.mcendgame.component.dungeon.generation

import com.sk89q.worldedit.bukkit.BukkitWorld
import com.sk89q.worldedit.extent.clipboard.Clipboard
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats
import com.sk89q.worldedit.math.BlockVector3
import com.sk89q.worldedit.math.transform.AffineTransform
import de.fuballer.mcendgame.component.dungeon.generation.data.PlaceableRoom
import de.fuballer.mcendgame.component.dungeon.world.WorldManageService
import de.fuballer.mcendgame.framework.annotation.Component
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Player
import java.util.logging.Logger
import kotlin.random.Random

@Component
class DungeonGenerationService(
    private val worldManageService: WorldManageService,
    private val logger: Logger
) {
    fun generateDungeon(
        player: Player,
        mapTier: Int,
        leaveLocation: Location
    ): Location {
        val world = worldManageService.createWorld(player, mapTier)
        val random = Random(world.seed)

        val dungeonLayoutGenerator = NewDungeonLayoutGenerator()
        dungeonLayoutGenerator.generateDungeon(
            random,
            100
        )
        val layoutRooms = dungeonLayoutGenerator.getLayout()

        runBlocking {
            launch { loadLayoutTiles(layoutRooms, world) }
        }

        return Location(world, 5.0, 1.5, 5.0)
    }

    private fun loadLayoutTiles(
        layoutRooms: List<PlaceableRoom>,
        world: World
    ) {
        for (room in layoutRooms) {
            val location = Location(world, room.position.x, room.position.y, room.position.z)
            loadSchematic(room.name, location, room.rotation, world)
        }
    }

    private fun loadSchematic(
        schematicName: String,
        location: Location,
        rotation: Double,
        world: World
    ) {
        val schematicPath = DungeonGenerationSettings.getSchematicPath(schematicName)
        val inputStream = javaClass.getResourceAsStream(schematicPath)!!

        val format = ClipboardFormats.findByAlias("schem")
        if (format == null) {
            logger.severe("Couldn't find schematic: $schematicPath")
            return
        }

        val clipboard = format.load(inputStream)

        val transform = AffineTransform().rotateY(rotation)

        pasteSchematic(clipboard, location, transform, world)
    }

    private fun pasteSchematic(
        clipboard: Clipboard,
        location: Location,
        transform: AffineTransform?,
        world: World
    ) {
        val bukkitWorld = BukkitWorld(world)
        val vector = BlockVector3.at(location.x, location.y, location.z)

        clipboard.paste(bukkitWorld, vector, false, false, transform)
            .close()
    }
}