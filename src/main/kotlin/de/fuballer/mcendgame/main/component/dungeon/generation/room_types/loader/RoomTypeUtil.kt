package de.fuballer.mcendgame.main.component.dungeon.generation.room_types.loader

import de.fuballer.mcendgame.main.component.dungeon.generation.data.RoomType
import de.fuballer.mcendgame.main.util.random.RandomOption
import net.minecraft.structure.StructureTemplateManager

/**
 * Loads a room template and its mirrored variant, wrapping both as weighted [RandomOption]s.
 *
 * @param weight the selection weight for both variants
 * @param templateManager the manager used to load structure templates
 * @param path the resource path of the structure template
 * @return a list containing the original and mirrored room as [RandomOption]s with the given weight
 */
fun loadRoom(
    weight: Int,
    templateManager: StructureTemplateManager,
    path: String,
): List<RandomOption<RoomType>> {
    return RoomTypeLoader.loadWithMirrored(templateManager, path).map {
        RandomOption(weight, it)
    }
}