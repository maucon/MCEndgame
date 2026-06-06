package de.fuballer.mcendgame.main.component.custom_attribute.effects.dodge

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.getAllCustomAttributes
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.functional.scheduler.Scheduler
import de.fuballer.mcendgame.main.messaging.misc.LivingEntityDodgedEvent
import de.fuballer.mcendgame.main.mixin.projectile.ProjectileAccessor
import de.fuballer.mcendgame.main.mixin.trident.ThrownTridentAccessor
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.event.EventSubscriber
import net.minecraft.core.registries.Registries
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.Avatar
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.entity.projectile.arrow.AbstractArrow
import net.minecraft.world.entity.projectile.arrow.ThrownTrident
import net.minecraft.world.item.enchantment.EnchantmentHelper
import net.minecraft.world.item.enchantment.Enchantments
import kotlin.random.Random

@Injectable
class DodgedProjectileReflectService(
    val scheduler: Scheduler,
) {
    @EventSubscriber(sync = true)
    fun on(event: LivingEntityDodgedEvent) {
        val projectile = event.source as? Projectile ?: return

        val entity = event.entity
        if (!entity.getAllCustomAttributes().contains(CustomAttributeTypes.DODGED_PROJECTILE_REFLECT)) return

        val attacker = event.attacker
        scheduler.delayed(1) {
            if (!projectile.isAlive) return@delayed
            val world = projectile.level() as? ServerLevel ?: return@delayed

            val rawDirection = if (attacker == null) entity.lookAngle else attacker.eyePosition.subtract(projectile.position())
            if (rawDirection.lengthSqr() < 1.0E-6) return@delayed
            val newDirection = rawDirection.normalize()

            val newVelocity = newDirection.scale(1.6)
            val newYaw = (Math.toDegrees(kotlin.math.atan2(newVelocity.z, newVelocity.x)) - 90.0).toFloat()
            projectile.setYRot(newYaw)
            projectile.yRotO = newYaw
            projectile.setDeltaMovement(newVelocity)
            projectile.needsSync = true

            var loyalty = false
            if (projectile is ThrownTrident) {
                val stack = projectile.pickupItemStackOrigin

                world.registryAccess()
                    .lookupOrThrow(Registries.ENCHANTMENT)
                    .get(Enchantments.LOYALTY.identifier()).ifPresent {
                        loyalty = EnchantmentHelper.getItemEnchantmentLevel(it, stack) > 0
                    }

                (projectile as ThrownTridentAccessor).`mcendgame$setDealtDamage`(false)
            }

            if (!loyalty) projectile.owner = entity
            (projectile as ProjectileAccessor).`mcendgame$setLeftOwner`(false)

            if (projectile is AbstractArrow) {
                if (attacker != null && attacker !is Avatar)
                    projectile.pickup = AbstractArrow.Pickup.CREATIVE_ONLY
                else {
                    projectile.pickup = AbstractArrow.Pickup.ALLOWED
                }
            }

            playSound(entity, world)
        }
    }

    private fun playSound(
        entity: Entity,
        world: ServerLevel,
    ) {
        val pos = entity.position()
        val soundCategory = if (entity is Avatar) SoundSource.PLAYERS else SoundSource.HOSTILE
        world.playSound(
            null,
            pos.x,
            pos.y,
            pos.z,
            SoundEvents.BREEZE_WIND_CHARGE_BURST,
            soundCategory,
            0.5F,
            0.9F + 0.2F * Random.nextFloat(),
        )
    }
}