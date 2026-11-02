package de.fuballer.mcendgame.main.component.dungeon.generation.room_types

import de.fuballer.mcendgame.main.component.dungeon.generation.data.RoomType
import de.fuballer.mcendgame.main.component.dungeon.generation.room_types.loader.RoomTypeLoader
import de.fuballer.mcendgame.main.messaging.server.ServerStartedEvent
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.event.EventSubscriber
import net.minecraft.core.BlockPos

@Injectable
object BurningCathedralRoomTypes {
    lateinit var BOSS_ROOM: RoomType

    @EventSubscriber(sync = true)
    fun on(event: ServerStartedEvent) {
        val templateManager = event.server.structureTemplateManager

        BOSS_ROOM = RoomTypeLoader.load(
            templateManager, "dungeon/burning_cathedral/boss000",
            mapOf(
                "dungeon/burning_cathedral/boss001" to BlockPos(0, 0, 48),
                "dungeon/burning_cathedral/boss010" to BlockPos(0, 48, 0),
                "dungeon/burning_cathedral/boss011" to BlockPos(0, 48, 48),
                "dungeon/burning_cathedral/boss100" to BlockPos(48, 0, 0),
                "dungeon/burning_cathedral/boss101" to BlockPos(48, 0, 48),
                "dungeon/burning_cathedral/boss110" to BlockPos(48, 48, 0),
                "dungeon/burning_cathedral/boss111" to BlockPos(48, 48, 48),
            )
        )
    }
}