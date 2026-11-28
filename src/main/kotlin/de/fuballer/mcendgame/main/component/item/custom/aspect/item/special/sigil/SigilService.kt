package de.fuballer.mcendgame.main.component.item.custom.aspect.item.special.sigil

import de.fuballer.mcendgame.main.component.dungeon.enemy.boss.DungeonBossDeathEffectsCommand
import de.fuballer.mcendgame.main.component.item.custom.aspect.AspectItems
import de.fuballer.mcendgame.main.component.item.custom.aspect.item.special.key.KeyItem
import de.fuballer.mcendgame.main.messaging.dungeon.DungeonBossCrystalDropCommand
import de.fuballer.mcendgame.main.messaging.dungeon.DungeonBossDeathEvent
import de.fuballer.mcendgame.main.messaging.dungeon.DungeonGenerateCommand
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

        if (serverLevel.getDungeonAspects().keys.any { it is KeyItem }) return

        AspectItems.SIGILS.forEach { sigil ->
            if (sigil !is SigilItem) return@forEach

            if (dungeonLevel < sigil.minDropLevel) return@forEach

            val dropProbability = sigil.getDropProbability(dungeonLevel)
            if (Random.nextDouble() > dropProbability) return@forEach

            val stack = sigil.defaultInstance
            event.bossEntity.spawnAtLocation(serverLevel, stack)
        }
    }

    @CommandHandler
    fun onGenerateLayout(cmd: DungeonGenerateCommand) {
        cmd.aspects.keys.forEach { aspect ->
            val sigil = aspect as? SigilItem ?: return@forEach
            cmd.dungeonLevel = max(cmd.dungeonLevel, sigil.minBossLevel)
            return
        }
    }

    @CommandHandler
    fun onDungeonBossCrystalDrop(cmd: DungeonBossCrystalDropCommand) {
        cmd.aspects.keys.forEach { aspect ->
            val sigil = aspect as? SigilItem ?: return@forEach
            val crystalItems = cmd.crystalItems
            crystalItems.clear()
            crystalItems.addAll(sigil.getCrystalItems(cmd.dungeonLevel, cmd.lootMultiplier, cmd.finalBoss))
            return
        }
    }

    @CommandHandler
    fun onDungeonBossDeathEffects(cmd: DungeonBossDeathEffectsCommand) {
        cmd.level.getDungeonAspects().keys.forEach { aspect ->
            val sigil = aspect as? SigilItem ?: return@forEach

            cmd.particles.clear()
            cmd.particles.addAll(sigil.bossDeathParticles)
            cmd.sounds.clear()
            cmd.sounds.addAll(sigil.bossDeathSounds)
            return
        }
    }
}