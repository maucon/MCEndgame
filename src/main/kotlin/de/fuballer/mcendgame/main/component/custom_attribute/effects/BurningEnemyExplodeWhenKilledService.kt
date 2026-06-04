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
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.phys.Vec3

@Injectable
class BurningEnemyExplodeWhenKilledService {
    @EventSubscriber(sync = true)
    fun on(event: LivingEntityDeathEvent) {
        val serverWorld = event.world as? ServerLevel ?: return

        val entity = event.entity
        if (!entity.isOnFire) return

        val killer = event.killer ?: return
        val attributes = killer.getAllCustomAttributes()[CustomAttributeTypes.BURNING_ENEMIES_EXPLODE_WHEN_KILLED] ?: return
        val sum = attributes.sumOf { it.rolls[0].asDoubleRoll().getValue() }
        if (sum <= 0) return

        explode(serverWorld, entity, killer, sum)
    }

    private fun explode(
        world: ServerLevel,
        killed: LivingEntity,
        killer: LivingEntity,
        damagePercentage: Double,
    ) {
        createParticles(world, killed.centerPos())
        playSound(world, killed, killed.centerPos())

        getNearbyTargets(world, killed, killer)
            .forEach { target -> target.dealElementalSpellDamage(damagePercentage, killer) }
    }

    private fun getNearbyTargets(
        world: ServerLevel,
        killed: LivingEntity,
        killer: LivingEntity,
    ) = world.getEntities(killed, killed.boundingBox.inflate(5.0)) { it.isValidSecondaryTarget(killed, killer) }

    private fun createParticles(
        world: ServerLevel,
        pos: Vec3,
    ) {
        world.sendParticles(
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
        world: ServerLevel,
        source: Entity,
        pos: Vec3,
    ) {
        world.playSound(
            source,
            pos.x,
            pos.y,
            pos.z,
            SoundEvents.FIREWORK_ROCKET_BLAST,
            SoundSource.PLAYERS,
            0.5F,
            1.0F
        )
    }
}