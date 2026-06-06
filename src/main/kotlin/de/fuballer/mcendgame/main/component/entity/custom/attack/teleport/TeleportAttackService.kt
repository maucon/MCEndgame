package de.fuballer.mcendgame.main.component.entity.custom.attack.teleport

import de.fuballer.mcendgame.main.component.entity.custom.interfaces.TeleportAttackMob
import de.fuballer.mcendgame.main.functional.scheduler.Scheduler
import de.fuballer.mcendgame.main.util.extension.EntityExtension.centerPos
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.event.EventSubscriber
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import kotlin.math.max

private const val PREPARATION_PARTICLE_DURATION = 40 // ticks
private const val TARGET_POS_ADJUST_TRIES = 5

@Injectable
class TeleportAttackService<T>(
    private val scheduler: Scheduler,
) where T : Entity, T : TeleportAttackMob {
    @EventSubscriber(sync = true)
    fun on(event: TeleportAttackEvent<T>) {
        val attacker = event.attacker
        val target = event.target

        scheduler.delayed(max(0, event.chosePositionDelayTicks - PREPARATION_PARTICLE_DURATION)) { createPreparationParticles(attacker) }
        scheduler.delayed(event.chosePositionDelayTicks) { choseTeleportPosition(attacker, target) }

        scheduler.delayed(event.teleportDelayTicks) {
            teleport(attacker)
            createArriveParticles(attacker)
        }
    }

    private fun choseTeleportPosition(attacker: T, target: Entity) {
        val world = attacker.level()
        if (world != target.level()) return

        var targetPos = target.position()
        val adjustDirection = attacker.position().subtract(targetPos).normalize()

        targetPos = targetPos.add(adjustDirection.scale(0.5))
        repeat(TARGET_POS_ADJUST_TRIES) {
            if (isTeleportPositionSafe(world, attacker, targetPos)) {
                attacker.teleportAttackTargetPosition = targetPos
                return
            }

            targetPos = targetPos.add(adjustDirection)
        }
    }

    private fun isTeleportPositionSafe(
        world: Level,
        entity: Entity,
        pos: Vec3,
    ): Boolean {
        val box = entity.boundingBox.move(pos.subtract(entity.position()))
        return world.noCollision(box)
    }

    private fun teleport(attacker: T) {
        val pos = attacker.teleportAttackTargetPosition ?: return
        attacker.snapTo(pos.x, pos.y, pos.z)
        attacker.teleportAttackTargetPosition = null
    }

    private fun createPreparationParticles(attacker: Entity) {
        val world = attacker.level() as? ServerLevel ?: return
        val pos = attacker.centerPos()
        world.sendParticles(
            ParticleTypes.PORTAL,
            pos.x,
            pos.y,
            pos.z,
            50,
            0.1,
            0.1,
            0.1,
            1.0
        )
    }

    private fun createArriveParticles(attacker: Entity) {
        val world = attacker.level() as? ServerLevel ?: return
        val pos = attacker.centerPos()
        world.sendParticles(
            ParticleTypes.REVERSE_PORTAL,
            pos.x,
            pos.y,
            pos.z,
            50,
            0.1,
            0.1,
            0.1,
            1.0
        )
    }
}