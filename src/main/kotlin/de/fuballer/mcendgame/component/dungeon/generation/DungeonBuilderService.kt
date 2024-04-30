package de.fuballer.mcendgame.component.dungeon.generation

import com.sk89q.worldedit.bukkit.BukkitWorld
import com.sk89q.worldedit.extent.clipboard.Clipboard
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats
import com.sk89q.worldedit.math.BlockVector3
import com.sk89q.worldedit.math.transform.AffineTransform
import de.fuballer.mcendgame.component.dungeon.generation.data.PlaceableTile
import de.fuballer.mcendgame.framework.annotation.Component
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.bukkit.Location
import org.bukkit.World
import java.util.logging.Logger

@Component
class DungeonBuilderService(
    private val log: Logger
) {
    fun build(
        schematicFolder: String,
        layout: List<PlaceableTile>,
        world: World
    ) {
        runBlocking {
            launch { loadLayoutTiles(schematicFolder, layout, world) }
        }
    }

    private fun loadLayoutTiles(
        schematicFolder: String,
        layout: List<PlaceableTile>,
        world: World
    ) {
        for (tile in layout) {
            val (schematicName, position, rotation) = tile
            val location = Location(world, position.x, position.y, position.z)
            val schematicPath = "$schematicFolder/$schematicName"

            loadSchematic(schematicPath, location, rotation, world)
        }
    }

    private fun loadSchematic( // TODO load schematics once
        schematicPath: String,
        location: Location,
        rotation: Double,
        world: World
    ) {
        val fullSchematicPath = DungeonGenerationSettings.getFullSchematicPath(schematicPath)
        val inputStream = javaClass.getResourceAsStream(fullSchematicPath)!!

        val format = ClipboardFormats.findByAlias("schem")
        if (format == null) {
            log.severe("Couldn't find schematic: $fullSchematicPath")
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