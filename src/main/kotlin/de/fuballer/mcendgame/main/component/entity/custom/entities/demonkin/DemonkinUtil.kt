package de.fuballer.mcendgame.main.component.entity.custom.entities.demonkin

import de.fuballer.mcendgame.main.component.entity.custom.interfaces.PairedMob
import de.fuballer.mcendgame.main.component.particle.DemonkinEmpowerParticleEffect
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.LivingEntity

object DemonkinUtil {
    fun <T> createEmpowerParticles(entity: T) where T : PairedMob, T : LivingEntity {
        val partner = entity.getPartner() ?: return
        if (!partner.isDeadOrDying) return

        val level = entity.getLevel() as? ServerLevel ?: return

        val distance = partner.distanceTo(entity)
        val travelTime = (distance * 5 + 3 * entity.random.nextDouble()).toInt()

        level.sendParticles(
            DemonkinEmpowerParticleEffect(entity.uuid, travelTime),
            partner.x,
            partner.y + partner.bbHeight / 2.0,
            partner.z,
            1,
            0.0,
            0.0,
            0.0,
            1.0,
        )
    }

    fun <T> playEmpowerSounds(entity: T) where T : PairedMob, T : LivingEntity {
        val partner = entity.getPartner() ?: return
        if (!partner.isDeadOrDying) return

        val level = entity.getLevel() as? ServerLevel ?: return

        level.playSound(null, partner, SoundEvents.BLAZE_BURN, SoundSource.HOSTILE, 0.45f + 0.2f * entity.random.nextFloat(), 0.8f + 0.4f * entity.random.nextFloat())
    }
}