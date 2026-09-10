package de.fuballer.mcendgame.main.messaging.dungeon

import de.fuballer.mcendgame.main.component.dungeon.generation.data.SpawnPosition
import de.fuballer.mcendgame.main.component.item.custom.aspect.AspectItem
import de.fuballer.mcendgame.main.util.extension.mixin.WorldMixinExtension.getDungeonAspects
import net.minecraft.server.level.ServerLevel

data class DungeonGenerateBanditsCommand(
    val dungeonWorld: ServerLevel,
    val aspects: Map<AspectItem, Int>,
    val possibleSpawnPositions: MutableList<SpawnPosition>,
    val chosenSpawnPositions: MutableList<SpawnPosition> = mutableListOf(),
) {
    constructor(
        dungeonWorld: ServerLevel,
        possibleSpawnPositions: MutableList<SpawnPosition>,
    ) : this(dungeonWorld, dungeonWorld.getDungeonAspects(), possibleSpawnPositions, mutableListOf())

    fun addBandits(count: Int) {
        val banditSpawnPositions = possibleSpawnPositions.shuffled().take(count)
        chosenSpawnPositions.addAll(banditSpawnPositions)
    }
}