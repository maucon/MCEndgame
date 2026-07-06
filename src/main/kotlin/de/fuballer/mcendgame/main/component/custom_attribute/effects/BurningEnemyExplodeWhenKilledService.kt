package de.fuballer.mcendgame.main.component.custom_attribute.effects

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.asDoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.getAllCustomAttributes
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.damage.dealing.DamageDealingExtension.dealElementalSpellDamage
import de.fuballer.mcendgame.main.messaging.misc.LivingEntityDeathEvent
import de.fuballer.mcendgame.main.util.extension.EntityExtension.centerPos
import de.fuballer.mcendgame.main.util.extension.EntityExtension.isValidSecondaryTarget
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.event.EventSubscriber
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.Vec3d

@Injectable
class BurningEnemyExplodeWhenKilledService {
    @EventSubscriber(sync = true)
    fun on(event: LivingEntityDeathEvent) {
        val serverWorld = event.world as? ServerWorld ?: return

        val entity = event.entity
        if (!entity.isOnFire) return

        val killer = event.killer ?: return
        val attributes = killer.getAllCustomAttributes()[CustomAttributeTypes.BURNING_ENEMIES_EXPLODE_WHEN_KILLED] ?: return
        val sum = attributes.sumOf { it.rolls[0].asDoubleRoll().getValue() }
        if (sum <= 0) return

        explode(serverWorld, entity, killer, sum)
    }

    private fun explode(
        world: ServerWorld,
        killed: LivingEntity,
        killer: LivingEntity,
        damagePercentage: Double,
    ) {
        createParticles(world, killed.centerPos())
        playSound(world, killed)

        getNearbyTargets(world, killed, killer)
            .forEach { target -> target.dealElementalSpellDamage(damagePercentage, killer) }
    }

    private fun getNearbyTargets(
        world: ServerWorld,
        killed: LivingEntity,
        killer: LivingEntity,
    ) = world.getOtherEntities(killed, killed.boundingBox.expand(5.0)) { it.isValidSecondaryTarget(killed, killer) }

    private fun createParticles(
        world: ServerWorld,
        pos: Vec3d,
    ) {
        world.spawnParticles(
            ParticleTypes.FLAME,
            pos.x,
            pos.y,
            pos.z,
            30,
            0.5,
            0.5,
            0.5,
            1.0
        )
    }

    private fun playSound(
        world: ServerWorld,
        source: Entity,
    ) {
        world.playSoundFromEntity(
            source,
            SoundEvents.ENTITY_FIREWORK_ROCKET_BLAST,
            SoundCategory.PLAYERS,
            0.5F,
            1.0F
        )
    }
}