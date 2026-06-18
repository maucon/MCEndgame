package de.fuballer.mcendgame.main.component.custom_attribute.effects.projectile

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.getAdditionalProjectileCount
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.phys.Vec3

object AdditionalProjectilesUtil {
    fun shootProjectile(
        shooter: LivingEntity,
        spawnPos: Vec3?,
        direction: Vec3,
        entityFactory: () -> Projectile,
        shoot: (Projectile, Vec3) -> Unit,
    ) {
        val additionalProjectileCount = shooter.getAdditionalProjectileCount()
        val spread: Float = AdditionalProjectilesSettings.SPREAD_PRE_PROJECTILE_RAD * additionalProjectileCount
        var spreadRotation = -spread

        repeat(1 + additionalProjectileCount) {
            val projectile = entityFactory()
            spawnPos?.let { projectile.setPos(it) }
            projectile.owner = shooter

            val spreadVelocity = direction.yRot(spreadRotation)
            shoot(projectile, spreadVelocity)
            spreadRotation += 2 * AdditionalProjectilesSettings.SPREAD_PRE_PROJECTILE_RAD
        }
    }
}