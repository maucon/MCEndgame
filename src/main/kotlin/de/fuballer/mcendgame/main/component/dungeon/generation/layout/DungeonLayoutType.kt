package de.fuballer.mcendgame.main.component.dungeon.generation.layout

import de.fuballer.mcendgame.main.component.dungeon.generation.room_types.DesertRoomTypes
import de.fuballer.mcendgame.main.component.dungeon.generation.room_types.NetherRoomTypes
import de.fuballer.mcendgame.main.component.dungeon.generation.room_types.StrongholdRoomTypes
import de.fuballer.mcendgame.main.component.dungeon.generation.room_types.TrainingRoomTypes

enum class DungeonLayoutType(
    val layoutGeneratorProvider: () -> LayoutGenerator
) {
    TRAINING({ SingleRoomLayoutGenerator(TrainingRoomTypes.ROOM) }),
    STRONGHOLD({ LinearLayoutGenerator(StrongholdRoomTypes.START_ROOM, StrongholdRoomTypes.BOSS_ROOM, StrongholdRoomTypes.ROOMS) }),
    NETHER({ LinearLayoutGenerator(NetherRoomTypes.START_ROOM, NetherRoomTypes.BOSS_ROOM, NetherRoomTypes.ROOMS) }),
    DESERT({ LinearLayoutGenerator(DesertRoomTypes.START_ROOM, DesertRoomTypes.BOSS_ROOM, DesertRoomTypes.ROOMS) }),
    BEASTWEAVER_GROVE({ SingleRoomLayoutGenerator(TrainingRoomTypes.ROOM) }),
}