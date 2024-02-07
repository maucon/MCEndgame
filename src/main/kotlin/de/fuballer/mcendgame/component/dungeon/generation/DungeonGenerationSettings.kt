package de.fuballer.mcendgame.component.dungeon.generation

object DungeonGenerationSettings {
    fun getSchematicPath(dungeonMapType: DungeonMapType, schematicName: String) =
        "/schematics/${dungeonMapType.typeName}/$schematicName.schem"

    var DUNGEON_WIDTH = 6
    var DUNGEON_HEIGHT = 9
    var DUNGEON_JUNCTION_PROBABILITY = .2
    var DUNGEON_MAX_ADJACENT_TILE_DIFF = 8
    var DUNGEON_BOSS_ROOM_X_TILE = DUNGEON_WIDTH / 2

    const val DUNGEON_Y_POS = 100.0
    const val MOB_Y_POS = DUNGEON_Y_POS + 6.2
    const val PORTAL_Y_POS = DUNGEON_Y_POS + 6.0
}
