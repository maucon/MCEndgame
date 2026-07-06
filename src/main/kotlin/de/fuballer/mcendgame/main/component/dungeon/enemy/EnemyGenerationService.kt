package de.fuballer.mcendgame.main.component.dungeon.enemy

import de.fuballer.mcendgame.main.component.dungeon.enemy.equipment.EquipmentGenerationService
import de.fuballer.mcendgame.main.component.dungeon.enemy.potion_effect.PotionEffectService
import de.fuballer.mcendgame.main.component.dungeon.generation.data.SpawnPosition
import de.fuballer.mcendgame.main.component.entity.EntityTypeStats
import de.fuballer.mcendgame.main.messaging.dungeon.DungeonEnemiesGeneratedCommand
import de.fuballer.mcendgame.main.messaging.dungeon.DungeonGenerateEnemiesCommand
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.setDungeonEnemy
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.setElite
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.setLootGoblin
import de.fuballer.mcendgame.main.util.minecraft.EntityUtil
import de.fuballer.mcendgame.main.util.random.RandomOption
import de.fuballer.mcendgame.main.util.random.RandomUtil
import de.maucon.mauconframework.command.CommandGateway
import de.maucon.mauconframework.di.annotation.Injectable
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.mob.MobEntity
import net.minecraft.server.world.ServerWorld
import kotlin.random.Random

@Injectable
class EnemyGenerationService(
    private val equipmentGenerationService: EquipmentGenerationService,
    private val potionEffectService: PotionEffectService,
) {
    fun generate(
        dungeonWorld: ServerWorld,
        level: Int,
        enemyTypes: List<RandomOption<EntityTypeStats>>,
        applyMisc: (List<LivingEntity>) -> Unit,
        spawnPositions: List<SpawnPosition>,
        isEncounter: Boolean = false,
    ): List<LivingEntity> {
        val random = Random
        val generateDungeonEnemiesCommand = DungeonGenerateEnemiesCommand.of(dungeonWorld, spawnPositions.toMutableList(), isEncounter)
        val cmd = CommandGateway.apply(generateDungeonEnemiesCommand)

        val entities = cmd.spawnPositions.map {
            spawnEnemy(
                dungeonWorld,
                level,
                enemyTypes,
                it,
                random,
                cmd,
            )
        }.toMutableList()

        entities.addAll(cmd.eliteSpawnPositions.map {
            spawnEnemy(
                dungeonWorld,
                level,
                enemyTypes,
                it,
                random,
                cmd,
                isForcedElite = true,
            )
        })

        entities.addAll(cmd.lootGoblinSpawnPositions.map {
            spawnEnemy(
                dungeonWorld,
                level,
                enemyTypes,
                it,
                random,
                cmd,
                isForcedLootGoblin = true,
            )
        })

        applyMisc(entities)

        val command = DungeonEnemiesGeneratedCommand.of(dungeonWorld, entities)
        CommandGateway.apply(command)

        return entities
    }

    private fun spawnEnemy(
        dungeonWorld: ServerWorld,
        level: Int,
        types: List<RandomOption<EntityTypeStats>>,
        location: SpawnPosition,
        random: Random,
        generateEnemiesCommand: DungeonGenerateEnemiesCommand,
        isForcedElite: Boolean = false,
        isForcedLootGoblin: Boolean = false,
    ): LivingEntity {
        val isLootGoblin = isForcedLootGoblin || EnemyGenerationSettings.randomLootGoblin(random)

        val validTypes = if (!isLootGoblin) types else types.filter { it.option.canHaveArmor }
        val type = RandomUtil.pickOne(validTypes, random).option
        val enemyEntity = EntityUtil.spawnEntityWithStats(dungeonWorld, type, location)

        enemyEntity.setDungeonEnemy()
        enemyEntity.setPersistent()

        if (isLootGoblin) enemyEntity.setLootGoblin()

        val isElite = isForcedElite || EnemyGenerationSettings.randomElite(random)
        if (isElite) {
            enemyEntity.setElite()
            applyEliteEffects(enemyEntity)
        }

        setScale(enemyEntity, isElite, random)

        equipmentGenerationService.generate(
            enemyEntity,
            type,
            level,
            dungeonWorld.server,
            isLootGoblin,
            random,
            generateEnemiesCommand,
        )

        potionEffectService.addEffects(enemyEntity, level, type.canBeInvisible, random)

        enemyEntity.heal(1000F)

        return enemyEntity
    }

    private fun applyEliteEffects(entity: MobEntity): Boolean {
        val healthAttributeInstance = entity.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH)
        val newMaxHealth = healthAttributeInstance?.baseValue!! * EnemyGenerationSettings.ELITE_HEALTH_FACTOR
        healthAttributeInstance.baseValue = newMaxHealth

        entity.addStatusEffect(EnemyGenerationSettings.getEliteStatusEffect())

        return true
    }

    private fun setScale(
        entity: MobEntity,
        isElite: Boolean,
        random: Random,
    ) {
        val scale = if (isElite) EnemyGenerationSettings.ELITE_SCALE else EnemyGenerationSettings.getRandomScale(random)
        entity.getAttributeInstance(EntityAttributes.GENERIC_SCALE)?.baseValue = scale
    }
}