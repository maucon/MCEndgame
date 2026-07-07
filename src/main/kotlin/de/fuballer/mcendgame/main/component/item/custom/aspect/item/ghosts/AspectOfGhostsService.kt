package de.fuballer.mcendgame.main.component.item.custom.aspect.item.ghosts

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.addCustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.item.custom.UniqueAttributesItemInterface
import de.fuballer.mcendgame.main.component.item.custom.armor.CustomArmorItems
import de.fuballer.mcendgame.main.component.item.custom.aspect.AspectItems
import de.fuballer.mcendgame.main.configuration.RuntimeConfig
import de.fuballer.mcendgame.main.messaging.dungeon.DungeonEnemiesGeneratedCommand
import de.fuballer.mcendgame.main.messaging.dungeon.DungeonEnemyDeathEvent
import de.fuballer.mcendgame.main.messaging.dungeon.DungeonFinalBossDeathEvent
import de.fuballer.mcendgame.main.messaging.dungeon.DungeonGenerateCommand
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.dropsAspectOfGhosts
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.setDropsAspectOfGhosts
import de.fuballer.mcendgame.main.util.extension.mixin.WorldMixinExtension.getDungeonAspects
import de.fuballer.mcendgame.main.util.extension.mixin.WorldMixinExtension.getDungeonLevel
import de.maucon.mauconframework.command.CommandHandler
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.event.EventSubscriber
import net.minecraft.item.ItemStack
import net.minecraft.server.world.ServerWorld
import kotlin.math.pow
import kotlin.random.Random

@Injectable
object AspectOfGhostsService {
    private val GHOSTLY_APPEARANCE_ATTRIBUTE = CustomAttribute(CustomAttributeTypes.GHOSTLY_APPEARANCE)

    @CommandHandler
    fun on(command: DungeonEnemiesGeneratedCommand) {
        if (command.world.getDungeonLevel() < AspectOfGhosts.RANDOM_GHOST_MIN_DUNGEON_LEVEL) return
        if (command.aspects.contains(AspectItems.ASPECT_OF_GHOSTS)) return

        val enemyCount = command.enemies.count()
        val generateGhost = Random.nextDouble() > (1.0 - AspectOfGhosts.RANDOM_GHOST_PROBABILITY).pow(enemyCount)
        if (!generateGhost) return

        val enemy = command.enemies.random()
        enemy.setDropsAspectOfGhosts()
        enemy.addCustomAttribute(GHOSTLY_APPEARANCE_ATTRIBUTE)
    }

    @EventSubscriber(sync = true)
    fun on(event: DungeonEnemyDeathEvent) {
        val serverWorld = event.world as? ServerWorld ?: return
        if (!event.enemyEntity.dropsAspectOfGhosts()) return

        val stack = AspectItems.ASPECT_OF_GHOSTS.defaultStack
        RuntimeConfig.SERVER.execute { event.enemyEntity.dropStack(stack) }
    }

    @CommandHandler
    fun onGhostDungeonGenerated(command: DungeonEnemiesGeneratedCommand) {
        if (!command.aspects.contains(AspectItems.ASPECT_OF_GHOSTS)) return
        command.enemies.forEach { it.addCustomAttribute(GHOSTLY_APPEARANCE_ATTRIBUTE) }
    }

    @CommandHandler
    fun onGenerateLayout(cmd: DungeonGenerateCommand) {
        if (!cmd.aspects.contains(AspectItems.ASPECT_OF_GHOSTS)) return
        cmd.dungeonLevel = AspectOfGhosts.FORCED_DUNGEON_LEVEL
    }

    @EventSubscriber(sync = true)
    fun onDungeonBossDeath(event: DungeonFinalBossDeathEvent) {
        val serverWorld = event.world as? ServerWorld ?: return
        if (!serverWorld.getDungeonAspects().contains(AspectItems.ASPECT_OF_GHOSTS)) return

        val item = CustomArmorItems.GEISTERGALOSCHEN
        val stack = if (item is UniqueAttributesItemInterface) item.getRolledStack(item) else ItemStack(item)
        RuntimeConfig.SERVER.execute { event.bossEntity.dropStack(stack) }
    }
}