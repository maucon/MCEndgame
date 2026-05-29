package de.fuballer.mcendgame.main.component.dungeon.generation.room_types

import de.fuballer.mcendgame.main.component.dungeon.generation.data.RoomType
import de.fuballer.mcendgame.main.component.dungeon.generation.room_types.loader.RoomTypeLoader
import de.fuballer.mcendgame.main.component.dungeon.generation.room_types.loader.loadRoom
import de.fuballer.mcendgame.main.messaging.server.ServerStartedEvent
import de.fuballer.mcendgame.main.util.random.RandomOption
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.event.EventSubscriber
import net.minecraft.util.math.BlockPos

@Injectable
object StrongholdRoomTypes {
    lateinit var START_ROOM: RoomType
    lateinit var BOSS_ROOM: RoomType
    lateinit var ROOMS: List<RandomOption<RoomType>>

    @EventSubscriber(sync = true)
    fun on(event: ServerStartedEvent) {
        val templateManager = event.server.structureTemplateManager

        START_ROOM = RoomTypeLoader.load(templateManager, "dungeon/stronghold/start")

        BOSS_ROOM = RoomTypeLoader.load(
            templateManager, "dungeon/stronghold/boss",
            mapOf(
                "dungeon/stronghold/boss2" to BlockPos(0, 0, 48)
            )
        )

        ROOMS = listOf(
            loadRoom(weight = 4, templateManager, "dungeon/stronghold/arches_side-stairs"),
            loadRoom(weight = 4, templateManager, "dungeon/stronghold/arena"),
            loadRoom(weight = 4, templateManager, "dungeon/stronghold/around-bars_workshop-corner_curve"),
            loadRoom(weight = 4, templateManager, "dungeon/stronghold/bridge_dive_plantrooms"),
            loadRoom(weight = 3, templateManager, "dungeon/stronghold/copper-sewers"),
            loadRoom(weight = 1, templateManager, "dungeon/stronghold/decayed-staircase_branching"),
            loadRoom(weight = 7, templateManager, "dungeon/stronghold/flat_chandelier_branching"),
            loadRoom(weight = 1, templateManager, "dungeon/stronghold/giant_tower"),
            loadRoom(weight = 3, templateManager, "dungeon/stronghold/inverted-pyramid_curve"),
            loadRoom(weight = 4, templateManager, "dungeon/stronghold/loop-around-chandelier_curve"),
            loadRoom(weight = 1, templateManager, "dungeon/stronghold/parcour_curve"),
            loadRoom(weight = 4, templateManager, "dungeon/stronghold/slimebounce"),
            loadRoom(weight = 7, templateManager, "dungeon/stronghold/small_connector_curve"),
            loadRoom(weight = 7, templateManager, "dungeon/stronghold/small_connector_sloped"),
            loadRoom(weight = 3, templateManager, "dungeon/stronghold/small_sewers"),
            loadRoom(weight = 5, templateManager, "dungeon/stronghold/small_stair-between-barrels_curve"),
            loadRoom(weight = 5, templateManager, "dungeon/stronghold/stair_chandelier_sidedrop_curve"),
            loadRoom(weight = 7, templateManager, "dungeon/stronghold/stair_elevated_branching"),
            loadRoom(weight = 3, templateManager, "dungeon/stronghold/stairs_statue"),
            loadRoom(weight = 4, templateManager, "dungeon/stronghold/tight_hanging-lamps_curve"),
            loadRoom(weight = 7, templateManager, "dungeon/stronghold/tiny_flat_connector"),
            loadRoom(weight = 4, templateManager, "dungeon/stronghold/tunnelbridge_curve"),
            loadRoom(weight = 4, templateManager, "dungeon/stronghold/zigzag-stairs_ponds"),
            loadRoom(weight = 4, templateManager, "dungeon/stronghold/zigzag_waterfalls_head_curve")
        ).flatten()
    }
}