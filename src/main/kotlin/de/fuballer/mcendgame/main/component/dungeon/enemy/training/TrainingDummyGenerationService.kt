package de.fuballer.mcendgame.main.component.dungeon.enemy.training

import de.fuballer.mcendgame.main.component.dungeon.generation.data.SpawnPosition
import de.fuballer.mcendgame.main.component.entity.custom.CustomEntities
import de.maucon.mauconframework.di.annotation.Injectable
import net.minecraft.entity.SpawnReason
import net.minecraft.server.world.ServerWorld
import kotlin.math.atan2

@Injectable
class TrainingDummyGenerationService {
    fun generate(
        dungeonWorld: ServerWorld,
        spawnPositions: List<SpawnPosition>,
        entryPortalPos: SpawnPosition,
    ) {
        val entryPos = entryPortalPos.pos
        val portalCenterX = entryPos.x + 0.5
        val portalCenterZ = entryPos.z + 0.5

        spawnPositions.forEachIndexed { index, spawnPos ->
            val entity = CustomEntities.TRAINING_DUMMY.spawn(
                dungeonWorld,
                spawnPos.blockPos(),
                SpawnReason.STRUCTURE
            ) ?: throw Exception("Couldn't spawn training dummy in world: $dungeonWorld")

            val entityPos = spawnPos.pos
            val entityX = entityPos.x + 0.5
            val entityZ = entityPos.z + 0.5

            val distX = portalCenterX - entityX
            val distZ = portalCenterZ - entityZ
            val yaw = Math.toDegrees(atan2(-distX, distZ)).toFloat()

            entity.refreshPositionAndAngles(
                entityX,
                entityPos.y.toDouble(),
                entityZ,
                spawnPos.rot.toFloat(),
                0F
            )

            val server = dungeonWorld.server ?: return
            TrainingDummyGenerationSettings.getLoadout(index, server).apply(entity)
        }
    }
}