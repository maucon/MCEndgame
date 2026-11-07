package de.fuballer.mcendgame.main.component.item.custom.aspect.item.sigil

import de.fuballer.mcendgame.main.component.dungeon.enemy.boss.DungeonBossDeathEffectsCommand
import de.fuballer.mcendgame.main.component.item.custom.aspect.AspectItems
import de.fuballer.mcendgame.main.messaging.dungeon.*
import de.fuballer.mcendgame.main.util.extension.mixin.WorldMixinExtension.getDungeonAspects
import de.fuballer.mcendgame.main.util.extension.mixin.WorldMixinExtension.getDungeonLevel
import de.maucon.mauconframework.command.CommandHandler
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.event.EventSubscriber
import net.minecraft.server.level.ServerLevel
import kotlin.math.max
import kotlin.random.Random

@Injectable
object SigilService {
    @EventSubscriber(sync = true)
    fun dropSigil(event: DungeonBossDeathEvent) {
        val serverLevel = event.world as? ServerLevel ?: return
        val dungeonLevel = serverLevel.getDungeonLevel()

        AspectItems.SIGILS.forEach { sigil ->
            if (sigil !is SigilItem) return@forEach
            if (serverLevel.getDungeonAspects().contains(sigil)) return@forEach

            if (dungeonLevel < sigil.minDropLevel) return@forEach

            val dropProbability = sigil.getDropProbability(dungeonLevel)
            if (Random.nextDouble() > dropProbability) return@forEach

            val stack = sigil.defaultInstance
            event.bossEntity.spawnAtLocation(serverLevel, stack)
        }
    }

    @CommandHandler
    fun onSelectDungeonType(cmd: SelectDungeonTypeCommand) {
        AspectItems.SIGILS.forEach { sigil ->
            if (sigil !is SigilItem) return@forEach
            if (!cmd.aspects.contains(sigil)) return@forEach
            cmd.dungeonType = sigil.dungeonType
            return
        }
    }

    @CommandHandler
    fun onGenerateLayout(cmd: DungeonGenerateCommand) {
        AspectItems.SIGILS.forEach { sigil ->
            if (sigil !is SigilItem) return@forEach
            if (!cmd.aspects.contains(sigil)) return@forEach
            cmd.dungeonLevel = max(cmd.dungeonLevel, sigil.minBossLevel)
            return
        }
    }

    @CommandHandler
    fun onDungeonBossCrystalDrop(cmd: DungeonBossCrystalDropCommand) {
        AspectItems.SIGILS.forEach { sigil ->
            if (sigil !is SigilItem) return@forEach
            if (!cmd.aspects.contains(sigil)) return@forEach

            val crystalItems = cmd.crystalItems
            crystalItems.clear()
            crystalItems.addAll(sigil.getCrystalItems(cmd.dungeonLevel, cmd.lootMultiplier, cmd.finalBoss))
            return
        }
    }

    @CommandHandler
    fun onIncreaseProgress(cmd: DungeonPlayerIncreaseProgressCommand) {
        AspectItems.SIGILS.forEach { sigil ->
            if (sigil !is SigilItem) return@forEach
            if (!cmd.aspects.contains(sigil)) return@forEach
            cmd.progressBlocked = true
            return
        }
    }

    @CommandHandler
    fun onDecreaseProgress(cmd: DungeonPlayerDecreaseProgressCommand) {
        AspectItems.SIGILS.forEach { sigil ->
            if (sigil !is SigilItem) return@forEach
            if (!cmd.aspects.contains(sigil)) return@forEach
            cmd.decreaseBlocked = true
            return
        }
    }

    @CommandHandler
    fun onDungeonBossDeathEffects(cmd: DungeonBossDeathEffectsCommand) {
        val aspects = cmd.level.getDungeonAspects()
        AspectItems.SIGILS.forEach { sigil ->
            if (sigil !is SigilItem) return@forEach
            if (!aspects.contains(sigil)) return@forEach

            cmd.particles.clear()
            cmd.particles.addAll(sigil.bossDeathParticles)
            cmd.sounds.clear()
            cmd.sounds.addAll(sigil.bossDeathSounds)
            return
        }
    }

    @CommandHandler
    fun onGenerateDungeonEnemies(cmd: DungeonGenerateEnemiesCommand) {
        AspectItems.SIGILS.forEach { sigil ->
            if (sigil !is SigilItem) return@forEach
            if (!cmd.aspects.contains(sigil)) return@forEach
            cmd.specialEnemiesDisabled = true
            return
        }
    }
}