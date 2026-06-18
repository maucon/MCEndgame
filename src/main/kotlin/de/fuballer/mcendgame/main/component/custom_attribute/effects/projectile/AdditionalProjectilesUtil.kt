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
        entityFactory: (ProjectileIndex) -> Projectile,
        shoot: (Projectile, Vec3, ProjectileIndex) -> Unit,
    ) {
        val additionalProjectileCount = shooter.getAdditionalProjectileCount()
        val spread: Float = AdditionalProjectilesSettings.SPREAD_PRE_PROJECTILE_RAD * additionalProjectileCount
        var spreadRotation = -spread

        for (i in 0..additionalProjectileCount) {
            val index = ProjectileIndex(i, additionalProjectileCount + 1)
            val projectile = entityFactory(index)

            spawnPos?.let { projectile.setPos(it) }
            projectile.owner = shooter

            val spreadVelocity = direction.yRot(spreadRotation)
            shoot(projectile, spreadVelocity, index)
            spreadRotation += 2 * AdditionalProjectilesSettings.SPREAD_PRE_PROJECTILE_RAD
        }
    }

    data class ProjectileIndex(
        val index: Int,
        val totalProjectiles: Int,
    ) {
        fun isMain() = index == totalProjectiles / 2
    }
}