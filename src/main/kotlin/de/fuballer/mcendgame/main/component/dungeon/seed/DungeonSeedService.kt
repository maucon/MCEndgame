package de.fuballer.mcendgame.main.component.dungeon.seed

import de.fuballer.mcendgame.main.component.dungeon.completion.DungeonCompletedEvent
import de.fuballer.mcendgame.main.component.dungeon.type.DungeonType
import de.fuballer.mcendgame.main.messaging.dungeon.DungeonGeneratedEvent
import de.fuballer.mcendgame.main.util.extension.mixin.PlayerEntityMixinExtension.clearDungeonSeed
import de.fuballer.mcendgame.main.util.extension.mixin.PlayerEntityMixinExtension.getDungeonSeed
import de.fuballer.mcendgame.main.util.extension.mixin.PlayerEntityMixinExtension.setDungeonSeed
import de.fuballer.mcendgame.main.util.extension.mixin.WorldMixinExtension.getOpener
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.event.EventSubscriber
import net.minecraft.entity.player.PlayerEntity
import kotlin.random.Random

@Injectable
class DungeonSeedService {
    fun rollSeed(player: PlayerEntity): PlayerDungeonSeed {
        val playerSeed = player.getDungeonSeed()
        if (playerSeed != null) return playerSeed

        val seed = Random.nextLong()
        val type = DungeonType.entries[Random.nextInt(DungeonType.entries.size)]

        val newSeed = PlayerDungeonSeed(seed, type)
        player.setDungeonSeed(newSeed)

        return newSeed
    }

    @EventSubscriber(sync = true)
    fun on(event: DungeonCompletedEvent) {
        val dungeonWorld = event.dungeonWorld
        val opener = dungeonWorld.getOpener()

        opener.clearDungeonSeed()
    }

    @EventSubscriber(sync = true)
    fun on(event: DungeonGeneratedEvent) {
        if (event.isTraining) return
        event.player.getDungeonSeed()?.hasBeenUsed = true
    }
}