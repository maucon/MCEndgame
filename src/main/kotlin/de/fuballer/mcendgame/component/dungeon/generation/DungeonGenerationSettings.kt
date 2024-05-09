package de.fuballer.mcendgame.component.dungeon.generation

object DungeonGenerationSettings {
    const val BOSS_AMOUNT = 3

    fun getFullSchematicPath(schematicPath: String) = "/schematics/$schematicPath.schem"
}