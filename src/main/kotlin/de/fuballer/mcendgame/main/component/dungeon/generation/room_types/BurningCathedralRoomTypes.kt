package de.fuballer.mcendgame.main.component.dungeon.generation.room_types

import de.fuballer.mcendgame.main.component.dungeon.generation.data.RoomType
import de.fuballer.mcendgame.main.component.dungeon.generation.room_types.loader.RoomTypeLoader
import de.fuballer.mcendgame.main.messaging.server.ServerStartedEvent
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.event.EventSubscriber

@Injectable
object BurningCathedralRoomTypes {
    lateinit var BOSS_ROOM: RoomType

    @EventSubscriber(sync = true)
    fun on(event: ServerStartedEvent) {
        val templateManager = event.server.structureTemplateManager

        BOSS_ROOM = RoomTypeLoader.load(
            templateManager, "dungeon/burning_cathedral/boss",
        )
    }
}