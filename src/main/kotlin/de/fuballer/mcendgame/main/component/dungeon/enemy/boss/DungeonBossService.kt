package de.fuballer.mcendgame.main.component.dungeon.enemy.boss

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.addCustomAttribute
import de.fuballer.mcendgame.main.messaging.dungeon.DungeonBossDeathEvent
import de.fuballer.mcendgame.main.messaging.dungeon.DungeonFinalBossDeathEvent
import de.fuballer.mcendgame.main.messaging.misc.LivingEntityDamagedEvent
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.isDungeonBoss
import de.fuballer.mcendgame.main.util.extension.mixin.WorldMixinExtension.getBossesKilled
import de.fuballer.mcendgame.main.util.extension.mixin.WorldMixinExtension.getTotalBossCount
import de.fuballer.mcendgame.main.util.extension.mixin.WorldMixinExtension.increaseBossesKilled
import de.maucon.mauconframework.command.CommandGateway
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.event.EventGateway
import de.maucon.mauconframework.event.EventSubscriber
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.player.Player

@Injectable
object DungeonBossService {
    @EventSubscriber(sync = true)
    fun on(event: DungeonBossDeathEvent) {
        val world = event.world as? ServerLevel ?: return
        world.increaseBossesKilled()

        playBossDeathEffects(world, event.bossEntity)

        val finalBoss = world.getBossesKilled() == world.getTotalBossCount()
        sendBossKilledMessage(world, finalBoss)

        if (!finalBoss) return
        val finalBossKilledEvent = DungeonFinalBossDeathEvent.of(event)
        EventGateway.publish(finalBossKilledEvent)
    }

    private fun playBossDeathEffects(
        level: ServerLevel,
        boss: Mob,
    ) {
        val command = DungeonBossDeathEffectsCommand(
            level,
            boss,
            DungeonBossSettings.DEFAULT_DEATH_SOUNDS.toMutableList(),
            DungeonBossSettings.DEFAULT_DEATH_PARTICLES.toMutableList(),
        )
        val cmd = CommandGateway.apply(command)

        cmd.sounds.forEach { it.apply(level, boss) }
        cmd.particles.forEach { it.apply(level, boss) }
    }

    private fun sendBossKilledMessage(
        level: ServerLevel,
        finalBoss: Boolean,
    ) {
        level.players().forEach { it.sendSystemMessage(DungeonBossSettings.getBossKilledMessage(finalBoss)) }
    }

    @EventSubscriber(sync = true)
    fun on(event: LivingEntityDamagedEvent) {
        if (!event.damaged.isDungeonBoss()) return
        val mobEntity = event.damaged as? Mob ?: return
        if (!mobEntity.isNoAi) return

        val player = event.damageSource.entity as? Player
        activateBoss(mobEntity, player)
    }

    fun activateBoss(
        boss: Mob,
        activatedBy: Player? = null,
    ) {
        boss.isNoAi = false
        enhanceBoss(boss)

        if (activatedBy == null) return
        if (activatedBy.isCreative || activatedBy.isSpectator) return
        boss.target = activatedBy
    }

    fun enhanceBoss(boss: Mob) {
        val world = boss.level() as? ServerLevel ?: return
        val killedBosses = world.getBossesKilled()
        if (killedBosses == 0) return

        DungeonBossSettings.getAttributePerKilledBoss(killedBosses).forEach { boss.addCustomAttribute(it) }
    }
}