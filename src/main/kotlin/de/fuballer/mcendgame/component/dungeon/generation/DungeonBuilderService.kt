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

@Component
class DungeonBuilderService {
    fun build(
        world: World,
        tiles: List<PlaceableTile>
    ) {
        runBlocking {
            for (tile in tiles) {
                launch { placeTile(world, tile) }
            }
        }
    }

    private fun placeTile(
        world: World,
        tile: PlaceableTile
    ) {
        val (tileData, position, rotation) = tile
        val location = Location(world, position.x, position.y, position.z)

        placeTile(tileData, world, location, rotation)
    }

    private fun placeTile(
        tileData: ByteArray,
        world: World,
        location: Location,
        rotation: Double
    ) {
        val format = ClipboardFormats.findByAlias("schem")!!
        val clipboard = format.load(tileData.inputStream())
        val transform = AffineTransform().rotateY(rotation)

        pasteClipboard(world, location, clipboard, transform)
    }

    private fun pasteClipboard(
        world: World,
        location: Location,
        clipboard: Clipboard,
        transform: AffineTransform?
    ) {
        val bukkitWorld = BukkitWorld(world)
        val vector = BlockVector3.at(location.x, location.y, location.z)

        clipboard.paste(bukkitWorld, vector, false, false, transform)
            .close()
    }
}