package de.fuballer.mcendgame.main.component.custom_attribute.effects

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.asDoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.getAllCustomAttributes
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.damage.dealing.DamageDealingExtension.dealElementalSpellDamage
import de.fuballer.mcendgame.main.messaging.misc.LivingEntityDamagedEvent
import de.fuballer.mcendgame.main.util.extension.EntityExtension.centerPos
import de.fuballer.mcendgame.main.util.extension.EntityExtension.isEnemy
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.event.EventSubscriber
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.LivingEntity

@Injectable
class ExplodeWhenTakingDamageService {
    @EventSubscriber(sync = true)
    fun on(event: LivingEntityDamagedEvent) {
        if (event.amount <= 0) return

        val damaged = event.damaged
        val serverWorld = damaged.level() as? ServerLevel ?: return
        val attributes = damaged.getAllCustomAttributes()[CustomAttributeTypes.EXPLODE_WHEN_TAKING_DAMAGE] ?: return

        if (damaged.hurtTime != damaged.hurtDuration) return

        val damagePercentage = attributes.sumOf { it.rolls[0].asDoubleRoll().getValue() }
        explode(serverWorld, damaged, damagePercentage)
    }

    private fun explode(
        world: ServerLevel,
        damaged: LivingEntity,
        damagePercentage: Double,
    ) {
        createParticles(world, damaged)
        playSound(world, damaged)

        getNearbyEnemies(world, damaged)
            .forEach { target -> target.dealElementalSpellDamage(damagePercentage, damaged) }
    }

    private fun getNearbyEnemies(
        world: ServerLevel,
        damaged: LivingEntity,
    ) = world.getEntities(damaged, damaged.boundingBox.inflate(5.0)) { damaged.isEnemy(it) }

    private fun createParticles(
        world: ServerLevel,
        damaged: LivingEntity,
    ) {
        val pos = damaged.centerPos()
        world.sendParticles(
            ParticleTypes.EXPLOSION,
            pos.x,
            pos.y,
            pos.z,
            8,
            2.0,
            2.0,
            2.0,
            1.0,
        )
    }

    private fun playSound(
        world: ServerLevel,
        damaged: LivingEntity,
    ) {
        val pos = damaged.centerPos()
        world.playSound(
            null,
            pos.x,
            pos.y,
            pos.z,
            SoundEvents.GENERIC_EXPLODE,
            SoundSource.PLAYERS,
            0.35F,
            1.0F,
        )
    }
}