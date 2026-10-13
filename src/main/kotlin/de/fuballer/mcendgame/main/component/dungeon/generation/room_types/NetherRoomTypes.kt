package de.fuballer.mcendgame.main.component.dungeon.generation.room_types

import de.fuballer.mcendgame.main.component.dungeon.generation.data.RoomType
import de.fuballer.mcendgame.main.component.dungeon.generation.room_types.loader.RoomTypeLoader
import de.fuballer.mcendgame.main.component.dungeon.generation.room_types.loader.loadRoom
import de.fuballer.mcendgame.main.messaging.server.ServerStartedEvent
import de.fuballer.mcendgame.main.util.random.RandomOption
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.event.EventSubscriber

@Injectable
object NetherRoomTypes {
    lateinit var START_ROOM: RoomType
    lateinit var BOSS_ROOM: RoomType
    lateinit var ROOMS: List<RandomOption<RoomType>>

    @EventSubscriber(sync = true)
    fun on(event: ServerStartedEvent) {
        val templateManager = event.server.structureTemplateManager

        START_ROOM = RoomTypeLoader.load(templateManager, "dungeon/nether/start")

        BOSS_ROOM = RoomTypeLoader.load(templateManager, "dungeon/nether/boss")

        ROOMS = listOf(
            loadRoom(weight = 7, templateManager, "dungeon/nether/small_connector"),
            loadRoom(weight = 4, templateManager, "dungeon/nether/soulsand-basalt-arena_branching"),
            loadRoom(weight = 3, templateManager, "dungeon/nether/bridge-over-warped_brewing-room_curve"),
            loadRoom(weight = 4, templateManager, "dungeon/nether/around-burning-soulsand_elevating_curve"),
            loadRoom(weight = 4, templateManager, "dungeon/nether/soulsand-basalt-arena_spiral_curve"),
            loadRoom(weight = 4, templateManager, "dungeon/nether/tunnel-path-around-lava"),
            loadRoom(weight = 2, templateManager, "dungeon/nether/blackstone-isles-over-magma_curve"),
        ).flatten()
    }
}