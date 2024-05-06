package de.fuballer.mcendgame.component.dungeon.generation.layout_generator.linear

import com.sk89q.worldedit.extent.clipboard.Clipboard
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats
import com.sk89q.worldedit.math.BlockVector3
import com.sk89q.worldedit.world.block.BlockTypes
import de.fuballer.mcendgame.component.dungeon.generation.DungeonGenerationSettings
import de.fuballer.mcendgame.component.dungeon.generation.data.SpawnLocation
import de.fuballer.mcendgame.util.VectorUtil
import org.bukkit.util.Vector
import java.io.ByteArrayOutputStream

private val DOOR_MARKER_BLOCK = BlockTypes.BLACK_WOOL!!.id
private val MONSTER_MARKER_BLOCK = BlockTypes.WHITE_WOOL!!.id
private val BOSS_MARKER_BLOCK = BlockTypes.DRAGON_HEAD!!.id

object RoomTypeLoader {
    fun load(schematicPath: String, complexity: Int): RoomType {
        val fullSchematicPath = DungeonGenerationSettings.getFullSchematicPath(schematicPath)
        val inputStream = javaClass.getResourceAsStream(fullSchematicPath)!!

        val format = ClipboardFormats.findByAlias("schem")!!
        val clipboard = format.load(inputStream)

        val size = clipboard.dimensions.subtract(1, 1, 1)
        val locations = findMarkedLocations(clipboard, size)

        val byteStream = ByteArrayOutputStream()
        clipboard.save(byteStream, format)
        val cleanSchematicData = byteStream.toByteArray()

        clipboard.close()

        return RoomType(
            cleanSchematicData,
            VectorUtil.fromBlockVector3(size),
            complexity,
            locations.doors,
            locations.spawnLocations,
            locations.bossSpawnLocations
        )
    }

    private fun findMarkedLocations(clipboard: Clipboard, size: BlockVector3): TileLocations {
        val doors = mutableListOf<Door>()
        val spawnLocations = mutableListOf<SpawnLocation>()
        val bossSpawnLocations = mutableListOf<SpawnLocation>()

        for (x in 0..size.x) {
            for (y in 0..size.y) {
                for (z in 0..size.z) {

                    val position = clipboard.origin.add(x, y, z)
                    val block = clipboard.getFullBlock(position)

                    when (block.blockType.id) {
                        DOOR_MARKER_BLOCK -> {
                            val door = getDoor(x, y, z, size)
                            doors.add(door)

                            replaceBlockWithAir(position, clipboard)
                        }

                        MONSTER_MARKER_BLOCK -> {
                            val centeredBlockPosition = Vector(x + 0.5, y + 0.0, z + 0.5)
                            val spawnLocation = SpawnLocation(centeredBlockPosition)
                            spawnLocations.add(spawnLocation)

//                            replaceBlockWithAir(position, clipboard)
                        }

                        BOSS_MARKER_BLOCK -> {
                            val rotation = block.toBlockState().states.entries
                                .find { (property, _) -> property.name == "rotation" }?.value as Int

                            val centeredBlockPosition = Vector(x + 0.5, y + 0.0, z + 0.5)
                            val spawnLocation = SpawnLocation(centeredBlockPosition, rotation.toDouble())
                            bossSpawnLocations.add(spawnLocation)

//                            replaceBlockWithAir(position, clipboard)
                        }
                    }
                }
            }
        }

        return TileLocations(doors, spawnLocations, bossSpawnLocations)
    }

    private fun getDoor(x: Int, y: Int, z: Int, size: BlockVector3) =
        Door(
            Vector(x, y, z),
            getDoorDirection(x, z, size)
        )

    private fun getDoorDirection(x: Int, z: Int, size: BlockVector3) =
        if (x == 0) {
            Vector(-1, 0, 0)
        } else if (z == 0) {
            Vector(0, 0, -1)
        } else if (x == size.x) {
            Vector(1, 0, 0)
        } else {
            Vector(0, 0, 1)
        }

    private fun replaceBlockWithAir(position: BlockVector3, clipboard: Clipboard) {
        val airBlock = BlockTypes.AIR!!.applyBlock(position)
        clipboard.setBlock(position.x, position.y, position.z, airBlock)
    }
}