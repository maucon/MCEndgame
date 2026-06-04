package de.fuballer.mcendgame.main.component.stats

import com.mojang.logging.LogUtils
import de.fuballer.mcendgame.main.component.dungeon.completion.DungeonCompletedEvent
import de.fuballer.mcendgame.main.component.entity.custom.entities.scarred_one.ScarredOneDespawnEvent
import de.fuballer.mcendgame.main.component.entity.custom.entities.training_dummy.TrainingDummyEntity
import de.fuballer.mcendgame.main.component.item.custom.crystal.item.*
import de.fuballer.mcendgame.main.messaging.crystals.CrystalForgeUsedEvent
import de.fuballer.mcendgame.main.messaging.dungeon.DungeonEnemyDeathEvent
import de.fuballer.mcendgame.main.messaging.dungeon.DungeonGeneratingEvent
import de.fuballer.mcendgame.main.messaging.dungeon.DungeonPlayerDeathEvent
import de.fuballer.mcendgame.main.messaging.misc.LivingEntityDamagedEvent
import de.fuballer.mcendgame.main.messaging.misc.PlayerAfterDimensionChangeEvent
import de.fuballer.mcendgame.main.messaging.portal.PortalUsedEvent
import de.fuballer.mcendgame.main.messaging.totem_encounter.TotemEncounterActivatedEvent
import de.fuballer.mcendgame.main.util.extension.DamageTypeExtension.isOf
import de.fuballer.mcendgame.main.util.extension.WorldExtension.isDungeonWorld
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.isDungeonBoss
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.isDungeonEnemy
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.isElite
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.isLootGoblin
import de.fuballer.mcendgame.main.util.extension.mixin.WorldMixinExtension.getDungeonLevel
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.event.EventSubscriber
import net.minecraft.server.level.ServerPlayer
import net.minecraft.stats.Stats
import net.minecraft.world.damagesource.DamageTypes
import net.minecraft.world.entity.player.Player

@Injectable
class CustomStatsService {
    private val log = LogUtils.getLogger()

    @EventSubscriber
    fun on(event: PortalUsedEvent) {
        event.player.awardStat(CustomStats.PORTALS_USED, 1)
    }

    @EventSubscriber
    fun on(event: PlayerAfterDimensionChangeEvent) {
        if (event.oldWorld.isDungeonWorld() || !event.newWorld.isDungeonWorld()) return
        event.player.awardStat(CustomStats.DUNGEONS_JOINED, 1)
    }

    @EventSubscriber
    fun on(event: DungeonCompletedEvent) {
        if (event.isClient) return
        for (player in event.players) {
            player.awardStat(CustomStats.DUNGEONS_COMPLETED, 1)
        }

        val dungeonLevel = event.dungeonWorld.getDungeonLevel()
        for (player in event.players) {
            val serverPlayer = player as? ServerPlayer ?: continue
            val currentHighest = serverPlayer.stats.getValue(
                Stats.CUSTOM.get(CustomStats.HIGHEST_COMPLETED_LEVEL)
            )
            if (dungeonLevel > currentHighest) {
                serverPlayer.awardStat(CustomStats.HIGHEST_COMPLETED_LEVEL, dungeonLevel - currentHighest)
            }
        }
    }

    @EventSubscriber
    fun on(event: DungeonPlayerDeathEvent) {
        if (event.isClient) return
        event.player.awardStat(CustomStats.DUNGEON_DEATHS, 1)
    }

    @EventSubscriber
    fun on(event: DungeonEnemyDeathEvent) {
        if (event.isClient) return
        val player = event.killer as? Player ?: return

        player.awardStat(CustomStats.ENEMIES_KILLED, 1)

        if (event.enemyEntity.isDungeonBoss()) {
            player.awardStat(CustomStats.BOSSES_KILLED, 1)
        }
        if (event.enemyEntity.isLootGoblin()) {
            player.awardStat(CustomStats.LOOT_GOBLINS_KILLED, 1)
        }
        if (event.enemyEntity.isElite()) {
            player.awardStat(CustomStats.ELITES_KILLED, 1)
        }
    }

    @EventSubscriber
    fun onDamageDealt(event: LivingEntityDamagedEvent) {
        val damaged = event.damaged

        if (!damaged.isDungeonEnemy()) return
        if (!damaged.level().isDungeonWorld()) return
        if (damaged is TrainingDummyEntity) return
        val player = event.damageSource.entity as? ServerPlayer ?: return

        player.awardStat(CustomStats.DUNGEON_DAMAGE_DEALT, (event.amount * 10).toInt())
    }

    @EventSubscriber
    fun onDamageTaken(event: LivingEntityDamagedEvent) {
        val player = event.damaged as? ServerPlayer ?: return

        if (!player.level().isDungeonWorld()) return
        if (event.damageSource.type().isOf(DamageTypes.GENERIC_KILL)) return

        player.awardStat(CustomStats.DUNGEON_DAMAGE_TAKEN, (event.amount * 10).toInt())
    }

    @EventSubscriber
    fun on(event: CrystalForgeUsedEvent) {
        val player = event.player
        player.awardStat(CustomStats.CRYSTAL_FORGE_USED, 1)

        when (event.crystal) {
            is CalibrationCrystalItem -> player.awardStat(CustomStats.CALIBRATION_CRYSTAL_USED, 1)
            is SacrificeCrystalItem -> player.awardStat(CustomStats.SACRIFICE_CRYSTAL_USED, 1)
            is PermutationCrystalItem -> player.awardStat(CustomStats.PERMUTATION_CRYSTAL_USED, 1)
            is ReforgeCrystalItem -> player.awardStat(CustomStats.REFORGE_CRYSTAL_USED, 1)
            is CorruptionCrystalItem -> player.awardStat(CustomStats.CORRUPTION_CRYSTAL_USED, 1)
            else -> log.error("Crystal stats handling not implemented for ${event.crystal}")
        }
    }

    @EventSubscriber
    fun on(event: ScarredOneDespawnEvent) {
        if (event.accepted) {
            event.player.awardStat(CustomStats.SCARRED_ONE_ACCEPTED, 1)
        } else {
            event.player.awardStat(CustomStats.SCARRED_ONE_DECLINED, 1)
        }
    }

    @EventSubscriber
    fun on(event: DungeonGeneratingEvent) {
        event.player.awardStat(CustomStats.DUNGEONS_OPENED, 1)

        val aspectCount = event.affectingAspects.values.sum()
        event.player.awardStat(CustomStats.ASPECTS_USED, aspectCount)
    }

    @EventSubscriber
    fun on(event: TotemEncounterActivatedEvent) {
        event.player.awardStat(CustomStats.TOTEM_ENCOUNTERS_ACTIVATED, 1)
    }
}