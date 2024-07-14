package de.fuballer.mcendgame.component.dungeon.generation

import de.fuballer.mcendgame.configuration.PluginConfiguration

object DungeonGenerationSettings {
    const val BOSS_AMOUNT = 3

    fun getFullSchematicPath(schematicPath: String) = "${PluginConfiguration.dataFolder()}/schematics/$schematicPath.schem"
}