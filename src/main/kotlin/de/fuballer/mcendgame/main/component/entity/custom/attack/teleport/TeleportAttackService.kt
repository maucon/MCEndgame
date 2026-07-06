package de.fuballer.mcendgame.main.component.entity.custom.attack.teleport

import de.fuballer.mcendgame.main.component.entity.custom.interfaces.TeleportAttackMob
import de.fuballer.mcendgame.main.functional.scheduler.Scheduler
import de.fuballer.mcendgame.main.util.extension.EntityExtension.centerPos
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.event.EventSubscriber
import net.minecraft.entity.Entity
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
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
        val world = attacker.entityWorld
        if (world != target.entityWorld) return

        var targetPos = target.pos
        val adjustDirection = attacker.pos.subtract(targetPos).normalize()

        targetPos = targetPos.add(adjustDirection.multiply(0.5))
        repeat(TARGET_POS_ADJUST_TRIES) {
            if (isTeleportPositionSafe(world, attacker, targetPos)) {
                attacker.teleportAttackTargetPosition = targetPos
                return
            }

            targetPos = targetPos.add(adjustDirection)
        }
    }

    private fun isTeleportPositionSafe(
        world: World,
        entity: Entity,
        pos: Vec3d,
    ): Boolean {
        val box = entity.boundingBox.offset(pos.subtract(entity.pos))
        return world.isSpaceEmpty(box)
    }

    private fun teleport(attacker: T) {
        val pos = attacker.teleportAttackTargetPosition ?: return
        attacker.refreshPositionAfterTeleport(pos.x, pos.y, pos.z)
        attacker.teleportAttackTargetPosition = null
    }

    private fun createPreparationParticles(attacker: Entity) {
        val world = attacker.entityWorld as? ServerWorld ?: return
        val pos = attacker.centerPos()
        world.spawnParticles(
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
        val world = attacker.entityWorld as? ServerWorld ?: return
        val pos = attacker.centerPos()
        world.spawnParticles(
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