package de.fuballer.mcendgame.main.component.item.custom.aspect.item.grove

import de.fuballer.mcendgame.main.component.dungeon.enemy.boss.DungeonBossDeathEffectsCommand
import de.fuballer.mcendgame.main.component.dungeon.type.DungeonType
import de.fuballer.mcendgame.main.component.item.custom.aspect.AspectItems
import de.fuballer.mcendgame.main.component.item.custom.crystal.CrystalItems
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
object AspectOfTheGroveService {
    @EventSubscriber(sync = true)
    fun dropAspectOfTheGrove(event: DungeonBossDeathEvent) {
        val serverLevel = event.world as? ServerLevel ?: return

        val dungeonLevel = serverLevel.getDungeonLevel()
        if (dungeonLevel < AspectOfTheGrove.MIN_DROP_LEVEL) return
        if (serverLevel.getDungeonAspects().contains(AspectItems.ASPECT_OF_THE_GROVE)) return

        val dropProbability = AspectOfTheGrove.getDropProbability(dungeonLevel)
        if (Random.nextDouble() > dropProbability) return

        val stack = AspectItems.ASPECT_OF_THE_GROVE.defaultInstance
        event.bossEntity.spawnAtLocation(serverLevel, stack)
    }

    @CommandHandler
    fun onSelectDungeonType(cmd: SelectDungeonTypeCommand) {
        if (!cmd.aspects.contains(AspectItems.ASPECT_OF_THE_GROVE)) return
        cmd.dungeonType = DungeonType.BEASTWEAVER_GROVE
    }

    @CommandHandler
    fun onGenerateLayout(cmd: DungeonGenerateCommand) {
        if (!cmd.aspects.contains(AspectItems.ASPECT_OF_THE_GROVE)) return
        cmd.dungeonLevel = max(cmd.dungeonLevel, AspectOfTheGrove.MIN_DUNGEON_LEVEL)
    }

    @CommandHandler
    fun onDungeonBossCrystalDrop(cmd: DungeonBossCrystalDropCommand) {
        if (!cmd.aspects.contains(AspectItems.ASPECT_OF_THE_GROVE)) return

        val crystalItems = cmd.crystalItems
        crystalItems.clear()

        val baseProbability = cmd.dungeonLevel / 10.0
        val finalProbability = baseProbability * cmd.lootMultiplier
        val count = finalProbability.toInt() + if (Random.nextDouble() < finalProbability % 1) 1 else 0
        repeat(count) { crystalItems.add(CrystalItems.IMITATION_CRYSTAL) }
    }

    @CommandHandler
    fun onIncreaseProgress(cmd: DungeonPlayerIncreaseProgressCommand) {
        if (!cmd.aspects.contains(AspectItems.ASPECT_OF_THE_GROVE)) return
        cmd.progressBlocked = true
    }

    @CommandHandler
    fun onDecreaseProgress(cmd: DungeonPlayerDecreaseProgressCommand) {
        if (!cmd.aspects.contains(AspectItems.ASPECT_OF_THE_GROVE)) return
        cmd.decreaseBlocked = true
    }

    @CommandHandler
    fun onDungeonBossDeathEffects(cmd: DungeonBossDeathEffectsCommand) {
        if (!cmd.level.getDungeonAspects().contains(AspectItems.ASPECT_OF_THE_GROVE)) return

        cmd.sounds.clear()
        cmd.sounds.add(AspectOfTheGrove.BOSS_DEATH_SOUND)
        cmd.particles.clear()
        cmd.particles.addAll(AspectOfTheGrove.BOSS_DEATH_PARTICLES)
    }
}