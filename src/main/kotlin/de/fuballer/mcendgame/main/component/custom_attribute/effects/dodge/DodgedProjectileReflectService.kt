package de.fuballer.mcendgame.main.component.custom_attribute.effects.dodge

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.getAllCustomAttributes
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.functional.scheduler.Scheduler
import de.fuballer.mcendgame.main.messaging.misc.LivingEntityDodgedEvent
import de.fuballer.mcendgame.main.mixin.projectile.ProjectileEntityAccessor
import de.fuballer.mcendgame.main.mixin.trident.TridentEntityAccessor
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.event.EventSubscriber
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.enchantment.Enchantments
import net.minecraft.entity.Entity
import net.minecraft.entity.PlayerLikeEntity
import net.minecraft.entity.projectile.PersistentProjectileEntity
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.entity.projectile.TridentEntity
import net.minecraft.registry.RegistryKeys
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import kotlin.random.Random

@Injectable
class DodgedProjectileReflectService(
    val scheduler: Scheduler,
) {
    @EventSubscriber(sync = true)
    fun on(event: LivingEntityDodgedEvent) {
        val projectile = event.source as? ProjectileEntity ?: return

        val entity = event.entity
        if (!entity.getAllCustomAttributes().contains(CustomAttributeTypes.DODGED_PROJECTILE_REFLECT)) return

        val attacker = event.attacker
        scheduler.delayed(1) {
            if (!projectile.isAlive) return@delayed
            val world = projectile.entityWorld as? ServerWorld ?: return@delayed

            val rawDirection = if (attacker == null) entity.rotationVector else attacker.eyePos.subtract(projectile.entityPos)
            if (rawDirection.lengthSquared() < 1.0E-6) return@delayed
            val newDirection = rawDirection.normalize()

            val newVelocity = newDirection.multiply(1.6)
            val newYaw = (Math.toDegrees(kotlin.math.atan2(newVelocity.z, newVelocity.x)) - 90.0).toFloat()
            projectile.yaw = newYaw
            projectile.lastYaw = newYaw
            projectile.velocity = newVelocity
            projectile.velocityDirty = true

            var loyalty = false
            if (projectile is TridentEntity) {
                val stack = projectile.itemStack

                world.registryManager
                    .getOrThrow(RegistryKeys.ENCHANTMENT)
                    .getEntry(Enchantments.LOYALTY.value).ifPresent {
                        loyalty = EnchantmentHelper.getLevel(it, stack) > 0
                    }

                (projectile as TridentEntityAccessor).`mcendgame$setDealtDamage`(false)
            }

            if (!loyalty) projectile.owner = entity
            (projectile as ProjectileEntityAccessor).`mcendgame$setLeftOwner`(false)

            if (projectile is PersistentProjectileEntity) {
                if (attacker != null && attacker !is PlayerLikeEntity)
                    projectile.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY
                else {
                    projectile.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED
                }
            }

            playSound(entity, world)
        }
    }

    private fun playSound(
        entity: Entity,
        world: ServerWorld,
    ) {
        val pos = entity.entityPos
        val soundCategory = if (entity is PlayerLikeEntity) SoundCategory.PLAYERS else SoundCategory.HOSTILE
        world.playSound(
            null,
            pos.x,
            pos.y,
            pos.z,
            SoundEvents.ENTITY_BREEZE_WIND_BURST,
            soundCategory,
            0.5F,
            0.9F + 0.2F * Random.nextFloat(),
        )
    }
}