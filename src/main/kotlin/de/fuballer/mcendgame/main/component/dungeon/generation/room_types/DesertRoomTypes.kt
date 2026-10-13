package de.fuballer.mcendgame.main.component.dungeon.generation.room_types

import de.fuballer.mcendgame.main.component.dungeon.generation.data.RoomType
import de.fuballer.mcendgame.main.component.dungeon.generation.room_types.loader.RoomTypeLoader
import de.fuballer.mcendgame.main.component.dungeon.generation.room_types.loader.loadRoom
import de.fuballer.mcendgame.main.messaging.server.ServerStartedEvent
import de.fuballer.mcendgame.main.util.random.RandomOption
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.event.EventSubscriber

@Injectable
object DesertRoomTypes {
    lateinit var START_ROOM: RoomType
    lateinit var BOSS_ROOM: RoomType
    lateinit var ROOMS: List<RandomOption<RoomType>>

    @EventSubscriber(sync = true)
    fun on(event: ServerStartedEvent) {
        val templateManager = event.server.structureTemplateManager

        START_ROOM = RoomTypeLoader.load(templateManager, "dungeon/desert/start")

        BOSS_ROOM = RoomTypeLoader.load(templateManager, "dungeon/desert/boss")

        ROOMS = listOf(
            loadRoom(weight = 7, templateManager, "dungeon/desert/small_connector"),
            loadRoom(weight = 4, templateManager, "dungeon/desert/arches_red-sand-side-path_curve"),
            loadRoom(weight = 4, templateManager, "dungeon/desert/arches_red-sand-side-path_mini-drop"),
            loadRoom(weight = 4, templateManager, "dungeon/desert/chandelier-above-fountain"),
            loadRoom(weight = 4, templateManager, "dungeon/desert/levels_drop-into-pool_curve"),
            loadRoom(weight = 4, templateManager, "dungeon/desert/library_curve"),
            loadRoom(weight = 4, templateManager, "dungeon/desert/square_center-flower-pool_lecterns"),
            loadRoom(weight = 4, templateManager, "dungeon/desert/stairs-in-between-levels_curve"),
            loadRoom(weight = 4, templateManager, "dungeon/desert/small_connector_curve"),
            loadRoom(weight = 3, templateManager, "dungeon/desert/small_connector_mini-drop_curve"),
            loadRoom(weight = 4, templateManager, "dungeon/desert/elevated-walk-way_curve"),
            loadRoom(weight = 1, templateManager, "dungeon/desert/stairwell_drop-with-hanging-lantern_branching"),
            loadRoom(weight = 1, templateManager, "dungeon/desert/stairwell_branching"),
        ).flatten()
    }
}