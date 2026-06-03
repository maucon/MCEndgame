package de.fuballer.mcendgame.main.component.dungeon.level

import de.fuballer.mcendgame.main.component.dungeon.completion.DungeonCompletedEvent
import de.fuballer.mcendgame.main.messaging.dungeon.ClientDungeonLevelUpdateEvent
import de.fuballer.mcendgame.main.messaging.dungeon.DungeonPlayerDeathEvent
import de.fuballer.mcendgame.main.messaging.dungeon.DungeonPlayerIncreaseProgressCommand
import de.fuballer.mcendgame.main.util.extension.mixin.PlayerEntityMixinExtension.getDungeonLevel
import de.fuballer.mcendgame.main.util.extension.mixin.PlayerEntityMixinExtension.setDungeonLevel
import de.fuballer.mcendgame.main.util.extension.mixin.WorldMixinExtension.getDungeonAspects
import de.fuballer.mcendgame.main.util.extension.mixin.WorldMixinExtension.getDungeonLevel
import de.fuballer.mcendgame.main.util.extension.mixin.WorldMixinExtension.isTrainingDungeon
import de.maucon.mauconframework.command.CommandGateway
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.event.EventSubscriber
import net.minecraft.server.world.ServerWorld
import kotlin.math.max

@Injectable
class DungeonLevelService {
    @EventSubscriber(sync = true)
    fun on(event: DungeonPlayerDeathEvent) {
        if (event.isClient) return

        val player = event.player
        if ((player.entityWorld as ServerWorld).isTrainingDungeon()) return
        val playerDungeonLevel = player.getDungeonLevel()

        playerDungeonLevel.level = max(playerDungeonLevel.level - 1, 1)
        playerDungeonLevel.levelProgress = 0

        player.setDungeonLevel(playerDungeonLevel)
        player.sendMessage(DungeonLevelSettings.getRegressMessage(playerDungeonLevel.level, playerDungeonLevel.levelProgress), false)
    }

    @EventSubscriber(sync = true)
    fun on(event: DungeonCompletedEvent) {
        val dungeonWorld = event.dungeonWorld
        val aspects = dungeonWorld.getDungeonAspects()
        val dungeonLevel = dungeonWorld.getDungeonLevel()

        val dungeonPlayerIncreaseProgressCommand = DungeonPlayerIncreaseProgressCommand(aspects)
        val cmd = CommandGateway.apply(dungeonPlayerIncreaseProgressCommand)

        event.players.forEach { player ->
            val playerDungeonLevel = player.getDungeonLevel()

            if (playerDungeonLevel.locked) {
                player.sendMessage(DungeonLevelSettings.LOCKED_MESSAGE, false)
                return@forEach
            }

            if (playerDungeonLevel.level > dungeonLevel) {
                player.sendMessage(DungeonLevelSettings.NO_PROGRESS_MESSAGE, false)
                return@forEach
            }

            val helpLevelProgress = (playerDungeonLevel.levelProgress + cmd.progressGranted)
            playerDungeonLevel.level += helpLevelProgress / DungeonLevelSettings.LEVEL_INCREASE_THRESHOLD
            playerDungeonLevel.levelProgress = helpLevelProgress % DungeonLevelSettings.LEVEL_INCREASE_THRESHOLD
            playerDungeonLevel.highestReached = max(playerDungeonLevel.highestReached, playerDungeonLevel.level)

            player.setDungeonLevel(playerDungeonLevel)
            player.sendMessage(DungeonLevelSettings.getProgressMessage(playerDungeonLevel.level, playerDungeonLevel.levelProgress), false)
        }
    }

    @EventSubscriber(sync = true)
    fun on(event: ClientDungeonLevelUpdateEvent) {
        val player = event.playerEntity
        val clientLevel = event.dungeonLevel
        val serverLevel = player.getDungeonLevel()

        val cL = clientLevel.level
        if (cL != serverLevel.level
            && cL >= 1
            && cL <= DungeonLevelSettings.getClientSetLevelLimit(serverLevel.highestReached)
        ) {
            serverLevel.level = cL
            serverLevel.levelProgress = 0
        }

        serverLevel.locked = clientLevel.locked

        player.setDungeonLevel(serverLevel)
    }
}