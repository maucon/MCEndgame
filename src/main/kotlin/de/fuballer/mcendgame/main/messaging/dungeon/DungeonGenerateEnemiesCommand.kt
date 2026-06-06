package de.fuballer.mcendgame.main.messaging.dungeon

import de.fuballer.mcendgame.main.component.dungeon.enemy.equipment.EquipmentGenerationSettings
import de.fuballer.mcendgame.main.component.dungeon.generation.data.SpawnPosition
import de.fuballer.mcendgame.main.component.item.custom.aspect.AspectItem
import de.fuballer.mcendgame.main.util.extension.mixin.WorldMixinExtension.getDungeonAspects
import de.fuballer.mcendgame.main.util.extension.mixin.WorldMixinExtension.getDungeonLevel
import net.minecraft.server.level.ServerLevel

data class DungeonGenerateEnemiesCommand(
    val dungeonWorld: ServerLevel,
    val aspects: Map<AspectItem, Int>,
    val spawnPositions: MutableList<SpawnPosition>,
    val isEncounter: Boolean,
    val eliteSpawnPositions: MutableList<SpawnPosition> = mutableListOf(),
    val lootGoblinSpawnPositions: MutableList<SpawnPosition> = mutableListOf(),
    var uniqueEquipmentProbability: Double = EquipmentGenerationSettings.getUniqueEquipmentBaseProbability(dungeonWorld.getDungeonLevel()),
    var lootGoblinLuckyAttributes: Boolean = false,
    var additionalAttributeProbabilities: MutableList<Double> = mutableListOf(),
) {
    companion object {
        fun of(
            dungeonWorld: ServerLevel,
            spawnPositions: MutableList<SpawnPosition>,
            isEncounter: Boolean,
        ) = DungeonGenerateEnemiesCommand(
            dungeonWorld,
            dungeonWorld.getDungeonAspects(),
            spawnPositions,
            isEncounter,
        )
    }
}