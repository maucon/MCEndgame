package de.fuballer.mcendgame.main.component.dungeon.generation.room_types

import de.fuballer.mcendgame.main.component.dungeon.generation.data.RoomType
import de.fuballer.mcendgame.main.component.dungeon.generation.room_types.loader.RoomTypeLoader
import de.fuballer.mcendgame.main.messaging.server.ServerStartedEvent
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.event.EventSubscriber
import net.minecraft.core.BlockPos

@Injectable
object BeastweaverGroveRoomTypes {
    lateinit var BOSS_ROOM: RoomType

    @EventSubscriber(sync = true)
    fun on(event: ServerStartedEvent) {
        val templateManager = event.server.structureManager

        BOSS_ROOM = RoomTypeLoader.load(
            templateManager, "dungeon/beastweaver_grove/boss000",
            mapOf(
                "dungeon/beastweaver_grove/boss001" to BlockPos(0, 0, 48),
                "dungeon/beastweaver_grove/boss010" to BlockPos(0, 48, 0),
                "dungeon/beastweaver_grove/boss011" to BlockPos(0, 48, 48),
                "dungeon/beastweaver_grove/boss100" to BlockPos(48, 0, 0),
                "dungeon/beastweaver_grove/boss101" to BlockPos(48, 0, 48),
                "dungeon/beastweaver_grove/boss110" to BlockPos(48, 48, 0),
                "dungeon/beastweaver_grove/boss111" to BlockPos(48, 48, 48),
            )
        )
    }
}