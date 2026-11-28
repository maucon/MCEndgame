package de.fuballer.mcendgame.main.component.entity.custom.entities.spell_fireball

import de.fuballer.mcendgame.main.component.custom_attribute.effects.projectile.AdditionalProjectilesUtil
import de.fuballer.mcendgame.main.component.entity.custom.CustomEntities
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.phys.Vec3
import kotlin.math.pow

object SpellFireballUtil {
    fun shoot(
        shooter: LivingEntity,
        target: Entity,
        startPos: Vec3,
        travelTicks: (Double) -> Int,
        serverLevel: ServerLevel,
        scale: Float = 1F,
    ) {
        val distance = Vec3(
            target.x - startPos.x,
            target.y + target.bbHeight / 2 - startPos.y,
            target.z - startPos.z,
        )
        val velocity = getFireballVelocity(distance, travelTicks(distance.length()))

        val itemStack = ItemStack(Items.AIR)
        val projectilePower = velocity.length().toFloat()

        AdditionalProjectilesUtil.shootProjectile(
            shooter,
            startPos,
            velocity,
            {
                val entity = SpellFireballEntity(CustomEntities.SPELL_FIREBALL, serverLevel)
                entity.setScale(scale)
                return@shootProjectile entity
            },
            { projectile, spreadVelocity, _ ->
                Projectile.spawnProjectileUsingShoot(
                    projectile,
                    serverLevel,
                    itemStack,
                    spreadVelocity.x,
                    spreadVelocity.y,
                    spreadVelocity.z,
                    projectilePower,
                    1.5F,
                )
            }
        )
    }

    private fun getFireballVelocity(
        distance: Vec3,
        travelTicks: Int,
    ): Vec3 {
        val horizontalDistance = distance.horizontalDistance()

        val tickInertia = SpellFireballEntity.INERTIA.pow(travelTicks)
        val denominator = 100 * (1 - tickInertia)
        if (denominator < 1e-8) return distance.normalize()

        val horizontalVelocity = horizontalDistance / denominator
        val gravityTimes100 = SpellFireballEntity.GRAVITY * 100
        val yVelocity = (distance.y + gravityTimes100 * travelTicks) / denominator - gravityTimes100

        val horizontalVelocityVec = distance.multiply(1.0, 0.0, 1.0).normalize().multiply(horizontalVelocity, 1.0, horizontalVelocity)
        val velocityVec = Vec3(horizontalVelocityVec.x, yVelocity, horizontalVelocityVec.z)
        return velocityVec
    }
}