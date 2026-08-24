package de.fuballer.mcendgame.main.component.dungeon.enemy

import de.fuballer.mcendgame.main.messaging.dungeon.DungeonGeneratedEvent
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.isDungeonBoss
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.isDungeonEnemy
import de.fuballer.mcendgame.main.util.extension.mixin.WorldMixinExtension.addCustomAttribute
import de.fuballer.mcendgame.main.util.extension.mixin.WorldMixinExtension.getDungeonLevel
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.event.EventSubscriber

@Injectable
class EnemyLevelScalingService {
    @EventSubscriber
    fun on(event: DungeonGeneratedEvent) {
        val dungeonWorld = event.dungeonWorld
        val level = dungeonWorld.getDungeonLevel()

        EnemyLevelScalingSettings.getEnemyLevelAttributes(level).forEach { attribute ->
            dungeonWorld.addCustomAttribute(attribute) { it.isDungeonEnemy() }
        }
        EnemyLevelScalingSettings.getBossLevelAttributes(level).forEach { attribute ->
            dungeonWorld.addCustomAttribute(attribute) { it.isDungeonBoss() }
        }
    }
}