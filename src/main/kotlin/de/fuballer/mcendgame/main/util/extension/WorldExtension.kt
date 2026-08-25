package de.fuballer.mcendgame.main.util.extension

import de.fuballer.mcendgame.main.MCEndgame
import de.fuballer.mcendgame.main.runtime_worlds.RuntimeWorlds
import net.minecraft.world.level.Level

object WorldExtension {
    fun Level.isDungeonWorld(): Boolean {
        if (dimension().identifier().namespace != MCEndgame.MOD_ID) return false
        return dimension().identifier().path.startsWith(RuntimeWorlds.DUNGEON_WORLD_PREFIX)
    }
}