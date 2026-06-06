package de.fuballer.mcendgame.main.component.custom_attribute.effects

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.asDoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.getAllCustomAttributes
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.configuration.RuntimeConfig
import de.fuballer.mcendgame.main.messaging.misc.LivingEntityDamagedEvent
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.event.EventSubscriber
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.entity.projectile.hurtingprojectile.WitherSkull
import net.minecraft.world.phys.Vec3
import kotlin.random.Random

@Injectable
class ShootWitherSkullWhenHitByProjectileService {
    @EventSubscriber(sync = true)
    fun on(event: LivingEntityDamagedEvent) {
        val damageSource = event.damageSource
        val projectile = damageSource.directEntity ?: return
        if (projectile !is Projectile) return

        val damaged = event.damaged
        val attributes = damaged.getAllCustomAttributes()[CustomAttributeTypes.SHOOT_WITHER_SKULL_WHEN_HIT_BY_PROJECTILE] ?: return

        val attacker = damageSource.entity

        attributes.forEach {
            if (Random.nextDouble() > it.rolls[0].asDoubleRoll().getValue()) return@forEach
            shootWitherSkullAt(damaged, attacker)
        }
    }

    private fun shootWitherSkullAt(
        shooter: LivingEntity,
        target: Entity?,
    ) {
        val world = shooter.level() as? ServerLevel ?: return

        val skull = WitherSkull(world, shooter, Vec3.ZERO)
        skull.setPos(shooter.eyePosition)
        val direction = if (target != null && target !is Projectile) {
            target.position().add(0.0, target.bbHeight / 2.0, 0.0).subtract(skull.position()).normalize()
        } else {
            shooter.lookAngle
        }.normalize()
        skull.setDeltaMovement(direction)

        RuntimeConfig.SERVER.execute { world.addFreshEntity(skull) }
    }
}