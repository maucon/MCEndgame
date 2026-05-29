package de.fuballer.mcendgame.main.component.dungeon.enemy.training

import de.fuballer.mcendgame.main.component.dungeon.generation.data.SpawnPosition
import de.fuballer.mcendgame.main.component.entity.custom.CustomEntities
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.setDungeonEnemy
import de.maucon.mauconframework.di.annotation.Injectable
import net.minecraft.entity.SpawnReason
import net.minecraft.server.world.ServerWorld

@Injectable
class TrainingDummyGenerationService {
    fun generate(
        dungeonWorld: ServerWorld,
        spawnPositions: List<SpawnPosition>,
    ) {
        spawnPositions.forEachIndexed { index, spawnPos ->
            val entity = CustomEntities.TRAINING_DUMMY.spawn(
                dungeonWorld,
                spawnPos.blockPos(),
                SpawnReason.STRUCTURE
            ) ?: throw Exception("Couldn't spawn training dummy in world: $dungeonWorld")

            val entityPos = spawnPos.pos
            val entityX = entityPos.x + 0.5
            val entityZ = entityPos.z + 0.5

            entity.refreshPositionAndAngles(
                entityX,
                entityPos.y.toDouble(),
                entityZ,
                spawnPos.rot.toFloat(),
                0F
            )

            val server = dungeonWorld.server ?: return
            TrainingDummyGenerationSettings.getLoadout(index, server).apply(entity)
            entity.setDungeonEnemy(true)
        }
    }
}