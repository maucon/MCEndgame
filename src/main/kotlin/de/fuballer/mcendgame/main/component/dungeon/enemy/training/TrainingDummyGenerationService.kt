package de.fuballer.mcendgame.main.component.dungeon.enemy.training

import de.fuballer.mcendgame.main.component.dungeon.generation.data.SpawnPosition
import de.fuballer.mcendgame.main.component.entity.custom.CustomEntities
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.setDungeonEnemy
import de.maucon.mauconframework.di.annotation.Injectable
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.EntitySpawnReason

@Injectable
class TrainingDummyGenerationService {
    fun generate(
        dungeonWorld: ServerLevel,
        spawnPositions: List<SpawnPosition>,
    ) {
        val server = dungeonWorld.server
        spawnPositions.forEachIndexed { index, spawnPos ->
            val entity = CustomEntities.TRAINING_DUMMY.spawn(
                dungeonWorld,
                spawnPos.blockPos(),
                EntitySpawnReason.STRUCTURE
            ) ?: throw Exception("Couldn't spawn training dummy in world: $dungeonWorld")

            val entityPos = spawnPos.pos
            val entityX = entityPos.x + 0.5
            val entityZ = entityPos.z + 0.5

            entity.snapTo(
                entityX,
                entityPos.y.toDouble(),
                entityZ,
                spawnPos.rot.toFloat(),
                0F
            )

            TrainingDummyGenerationSettings.getLoadout(index, server).apply(entity)
            entity.setDungeonEnemy(true)
        }
    }
}