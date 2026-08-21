package de.fuballer.mcendgame.main.component.status_effect

import de.fuballer.mcendgame.main.component.damage.dealing.DamageDealingExtension.dealGenericAttackDamage
import de.fuballer.mcendgame.main.util.extension.EntityExtension.isEnemy
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import kotlin.random.Random

class ScorchEffect : MobEffect(MobEffectCategory.BENEFICIAL, 15754270) {
    override fun applyEffectTick(world: ServerLevel, entity: LivingEntity, amplifier: Int): Boolean {
        if (entity.tickCount % 40 != 0) return true

        val enemies = world.getEntities(entity, entity.boundingBox.inflate(5.0))
            .filter { it is LivingEntity && it.isEnemy(entity) }

        if (enemies.isEmpty()) return true

        for (enemy in enemies) {
            if (!enemy.dealGenericAttackDamage(2f, entity)) continue
            enemy.remainingFireTicks = 60

            spawnParticles(world, enemy, 3, 0.2)
        }
        spawnParticles(world, entity, 8, 0.5)
        playSound(world, entity)

        return true
    }

    private fun spawnParticles(world: ServerLevel, entity: Entity, count: Int, speed: Double) {
        world.sendParticles(ParticleTypes.FLAME, entity.x, entity.y + entity.bbHeight / 2, entity.z, count, 0.0, 0.0, 0.0, speed)
    }

    private fun playSound(world: ServerLevel, entity: Entity) {
        world.playSound(entity, entity.blockPosition(), SoundEvents.FIRECHARGE_USE, SoundSource.NEUTRAL, 0.5F, 1F + (Random.nextFloat() - 0.5F) / 10F)
    }

    override fun shouldApplyEffectTickThisTick(duration: Int, amplifier: Int) = true
}