package de.fuballer.mcendgame.main.messaging

import de.fuballer.mcendgame.main.messaging.dungeon.DungeonBossDeathEvent
import de.fuballer.mcendgame.main.messaging.dungeon.DungeonEnemyDeathEvent
import de.fuballer.mcendgame.main.messaging.dungeon.DungeonEntityDeathEvent
import de.fuballer.mcendgame.main.messaging.dungeon.DungeonPlayerDeathEvent
import de.fuballer.mcendgame.main.messaging.misc.*
import de.fuballer.mcendgame.main.messaging.server.ServerEndTickEvent
import de.fuballer.mcendgame.main.messaging.server.ServerStartedEvent
import de.fuballer.mcendgame.main.messaging.server.ServerStartingEvent
import de.fuballer.mcendgame.main.messaging.server.ServerStoppingEvent
import de.fuballer.mcendgame.main.util.extension.ItemStackExtension.isSameIgnoringDurability
import de.fuballer.mcendgame.main.util.extension.WorldExtension.isDungeonWorld
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.isDungeonBoss
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.isDungeonEnemy
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.event.EventGateway
import de.maucon.mauconframework.event.EventSubscriber
import de.maucon.mauconframework.initializer.Initializer
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityLevelChangeEvents
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.arrow.Arrow
import net.minecraft.world.entity.projectile.arrow.SpectralArrow

@Injectable
object EventMapper {
    @EventSubscriber(sync = true)
    fun onPlayerDeath(event: LivingEntityDeathEvent) {
        val player = event.entity as? Player ?: return
        val playerDeathEvent = PlayerEntityDeathEvent(event.isClient, event.world, player, event.killer)
        EventGateway.publish(playerDeathEvent)
    }

    @EventSubscriber(sync = true)
    fun onDungeonPlayerDeath(event: PlayerEntityDeathEvent) {
        val player = event.player
        if (!player.level().isDungeonWorld()) return

        val dungeonPlayerDeathEvent = DungeonPlayerDeathEvent(event.isClient, player, event.killer)
        EventGateway.publish(dungeonPlayerDeathEvent)
    }

    @EventSubscriber(sync = true)
    fun onDungeonEntityDeath(event: LivingEntityDeathEvent) {
        val entity = event.entity
        if (!entity.level().isDungeonWorld()) return

        val dungeonEntityDeathEvent = DungeonEntityDeathEvent(event.isClient, event.world, event.entity, event.killer)
        EventGateway.publish(dungeonEntityDeathEvent)
    }

    @EventSubscriber(sync = true)
    fun onDungeonEnemyDeath(event: DungeonEntityDeathEvent) {
        val enemyEntity = event.entity
        if (!enemyEntity.isDungeonEnemy()) return

        val dungeonEnemyDeathEvent = DungeonEnemyDeathEvent(event.isClient, event.world, enemyEntity, event.killer)
        EventGateway.publish(dungeonEnemyDeathEvent)
    }

    @EventSubscriber(sync = true)
    fun onDungeonBossDeath(event: DungeonEnemyDeathEvent) {
        val bossEntity = event.enemyEntity as? Mob ?: return
        if (!bossEntity.isDungeonBoss()) return

        val dungeonBossDeathEvent = DungeonBossDeathEvent(event.isClient, event.world, bossEntity, event.killer)
        EventGateway.publish(dungeonBossDeathEvent)
    }

    @Initializer
    fun onServerStarting() = ServerLifecycleEvents.SERVER_STARTING.register {
        val event = ServerStartingEvent(it)
        EventGateway.publish(event)
    }

    @Initializer
    fun onServerStarted() = ServerLifecycleEvents.SERVER_STARTED.register {
        val event = ServerStartedEvent(it)
        EventGateway.publish(event)
    }

    @Initializer
    fun onServerStopping() = ServerLifecycleEvents.SERVER_STOPPING.register {
        val event = ServerStoppingEvent(it)
        EventGateway.publish(event)
    }

    @Initializer
    fun onServerTickEnd() = ServerTickEvents.END_SERVER_TICK.register {
        val event = ServerEndTickEvent(it)
        EventGateway.publish(event)
    }

    @Initializer
    fun afterPlayerChangeWorld() = ServerEntityLevelChangeEvents.AFTER_PLAYER_CHANGE_LEVEL.register { entity, oldWorld, newWorld ->
        val event = PlayerAfterDimensionChangeEvent(entity, oldWorld, newWorld)
        EventGateway.publish(event)
    }

    @Initializer
    fun afterPlayerRespawn() = ServerPlayerEvents.AFTER_RESPAWN.register { oldPlayer, newPlayer, alive -> // end portal is alive respawn
        val event = PlayerAfterRespawnEvent(oldPlayer, newPlayer, alive)
        EventGateway.publish(event)
    }

    @Initializer
    fun onPlayerJoin() = ServerPlayConnectionEvents.JOIN.register { handler, _, _ ->
        val event = PlayerJoinEvent(handler.player)
        EventGateway.publish(event)
    }

    @Initializer
    fun onPlayerDisconnect() = ServerPlayConnectionEvents.DISCONNECT.register { handler, _ ->
        val event = PlayerDisconnectEvent(handler.player)
        EventGateway.publish(event)
    }

    @Initializer
    fun onEquipmentChange() = ServerEntityEvents.EQUIPMENT_CHANGE.register { entity, slot, oldStack, newStack ->
        if (oldStack.isSameIgnoringDurability(newStack)) return@register

        val event = EquipmentChangeEvent(entity, slot, oldStack, newStack)
        EventGateway.publish(event)
    }

    @Initializer
    fun onArrowShotByLivingEntity() = ServerEntityEvents.ENTITY_LOAD.register { entity, world ->
        if (entity !is Arrow && entity !is SpectralArrow) return@register
        val owner = entity.owner as? LivingEntity ?: return@register

        val event = EntityShotArrowEvent.of(entity, owner)
        EventGateway.publish(event)
    }

    @Initializer
    fun onItemDrop() = ServerEntityEvents.ENTITY_LOAD.register { entity, world ->
        if (entity !is ItemEntity) return@register

        val event = ItemDropEvent.of(entity)
        EventGateway.publish(event)
    }

    @EventSubscriber(sync = true)
    fun onDungeonItemDrop(event: ItemDropEvent) {
        if (!event.world.isDungeonWorld()) return

        val dungeonItemDropEvent = DungeonItemDropEvent.of(event)
        EventGateway.publish(dungeonItemDropEvent)
    }

    @Initializer
    fun onLivingEntityEndTick() = ServerTickEvents.END_LEVEL_TICK.register { world ->
        if (world.gameTime % 5 != 0L) return@register
        val event = ServerLivingEntitiesEveryFiveTicksEvent(world.allEntities.filterIsInstance<LivingEntity>(), world)
        EventGateway.publish(event)
    }
}