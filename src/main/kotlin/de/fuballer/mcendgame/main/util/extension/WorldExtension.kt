package de.fuballer.mcendgame.main.util.extension

import de.fuballer.mcendgame.main.MCEndgame
import de.fuballer.mcendgame.main.component.dungeon.world.DungeonWorldSettings
import net.minecraft.world.level.Level

object WorldExtension {
    fun Level.isDungeonWorld(): Boolean {
        if (dimension().identifier().namespace != MCEndgame.MOD_ID) return false
        return dimension().identifier().path.startsWith(DungeonWorldSettings.DUNGEON_WORLD_PREFIX)
    }
}