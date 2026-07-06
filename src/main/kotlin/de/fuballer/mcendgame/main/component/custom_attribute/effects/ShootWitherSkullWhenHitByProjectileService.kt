package de.fuballer.mcendgame.main.component.custom_attribute.effects

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.asDoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.getAllCustomAttributes
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.configuration.RuntimeConfig
import de.fuballer.mcendgame.main.messaging.misc.LivingEntityDamagedEvent
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.event.EventSubscriber
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.entity.projectile.WitherSkullEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.Vec3d
import kotlin.random.Random

@Injectable
class ShootWitherSkullWhenHitByProjectileService {
    @EventSubscriber(sync = true)
    fun on(event: LivingEntityDamagedEvent) {
        val damageSource = event.damageSource
        val projectile = damageSource.source ?: return
        if (projectile !is ProjectileEntity) return

        val damaged = event.damaged
        val attributes = damaged.getAllCustomAttributes()[CustomAttributeTypes.SHOOT_WITHER_SKULL_WHEN_HIT_BY_PROJECTILE] ?: return

        val attacker = damageSource.attacker

        attributes.forEach {
            if (Random.nextDouble() > it.rolls[0].asDoubleRoll().getValue()) return@forEach
            shootWitherSkullAt(damaged, attacker)
        }
    }

    private fun shootWitherSkullAt(
        shooter: LivingEntity,
        target: Entity?,
    ) {
        val world = shooter.entityWorld as? ServerWorld ?: return

        val skull = WitherSkullEntity(world, shooter, Vec3d.ZERO)
        skull.setPosition(shooter.eyePos)
        val direction = if (target != null && target !is ProjectileEntity) {
            target.pos.add(0.0, target.height / 2.0, 0.0).subtract(skull.pos).normalize()
        } else {
            shooter.rotationVector
        }.normalize()
        skull.velocity = direction

        RuntimeConfig.SERVER.execute { world.spawnEntity(skull) }
    }
}