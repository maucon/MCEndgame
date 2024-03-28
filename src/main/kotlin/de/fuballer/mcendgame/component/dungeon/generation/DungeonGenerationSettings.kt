package de.fuballer.mcendgame.component.dungeon.generation

object DungeonGenerationSettings {
    fun getSchematicPath(schematicName: String) = "/schematics/test/$schematicName.schem"

    var DUNGEON_WIDTH = 6
    var DUNGEON_BOSS_ROOM_X_TILE = DUNGEON_WIDTH / 2

    const val DUNGEON_Y_POS = 100.0
}
