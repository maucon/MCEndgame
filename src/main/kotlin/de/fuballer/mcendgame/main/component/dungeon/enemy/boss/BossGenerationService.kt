package de.fuballer.mcendgame.main.component.dungeon.enemy.boss

import de.fuballer.mcendgame.main.component.dungeon.generation.data.SpawnPosition
import de.fuballer.mcendgame.main.component.entity.EntityTypeStats
import de.fuballer.mcendgame.main.messaging.dungeon.DungeonEnemiesGeneratedCommand
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.setDungeonBoss
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.setDungeonBossSpawnPosition
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.setDungeonEnemy
import de.fuballer.mcendgame.main.util.extension.mixin.WorldMixinExtension.setTotalBossCount
import de.fuballer.mcendgame.main.util.minecraft.EntityUtil
import de.maucon.mauconframework.command.CommandGateway
import de.maucon.mauconframework.di.annotation.Injectable
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.ai.attributes.Attributes
import kotlin.random.Random

@Injectable
class BossGenerationService {
    fun generate(
        dungeonWorld: ServerLevel,
        types: List<EntityTypeStats>,
        applyMisc: (List<LivingEntity>) -> Unit,
        locations: List<SpawnPosition>,
    ) {
        val random = Random
        val shuffledTypes = types.shuffled(random)
        val bosses = locations.mapIndexed { index, pos ->
            val type = shuffledTypes[index % shuffledTypes.size]
            spawnBoss(dungeonWorld, type, pos, random)
        }

        applyMisc(bosses)

        dungeonWorld.setTotalBossCount(bosses.size)

        val command = DungeonEnemiesGeneratedCommand.of(dungeonWorld, bosses)
        CommandGateway.apply(command)
    }

    private fun spawnBoss(
        dungeonWorld: ServerLevel,
        type: EntityTypeStats,
        spawnPosition: SpawnPosition,
        random: Random,
    ): Mob {
        val bossEntity = EntityUtil.spawnEntityWithStats(dungeonWorld, type, spawnPosition)

        bossEntity.setPersistenceRequired()
        setScale(bossEntity, random)

        bossEntity.setNoAi(true)

        bossEntity.setDungeonEnemy()
        bossEntity.setDungeonBoss()
        bossEntity.setDungeonBossSpawnPosition(spawnPosition)

        return bossEntity
    }

    private fun setScale(
        entity: Mob,
        random: Random,
    ) {
        val scale = DungeonBossSettings.getRandomScale(random)
        entity.getAttribute(Attributes.SCALE)?.baseValue = scale
    }
}