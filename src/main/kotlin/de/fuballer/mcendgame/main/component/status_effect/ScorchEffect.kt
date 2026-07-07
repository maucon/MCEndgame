package de.fuballer.mcendgame.main.component.status_effect

import de.fuballer.mcendgame.main.component.damage.dealing.DamageDealingExtension.dealGenericAttackDamage
import de.fuballer.mcendgame.main.util.extension.EntityExtension.isEnemy
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectCategory
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import kotlin.random.Random

class ScorchEffect : StatusEffect(StatusEffectCategory.BENEFICIAL, 15754270) {
    override fun applyUpdateEffect(entity: LivingEntity, amplifier: Int): Boolean {
        if (entity.age % 40 != 0) return true

        val world = entity.world as? ServerWorld ?: return true
        val enemies = world.getOtherEntities(entity, entity.boundingBox.expand(5.0))?.filter { it is LivingEntity && it.isEnemy(entity) } ?: return true
        if (enemies.isEmpty()) return true

        for (enemy in enemies) {
            enemy.fireTicks = 60
            enemy.dealGenericAttackDamage(2f, entity)

            spawnParticles(world, enemy, 3, 0.2)
        }
        spawnParticles(world, entity, 8, 0.5)
        playSound(world, entity)

        return true
    }

    private fun spawnParticles(world: ServerWorld, entity: Entity, count: Int, speed: Double) {
        world.spawnParticles(ParticleTypes.FLAME, entity.x, entity.y + entity.height / 2, entity.z, count, 0.0, 0.0, 0.0, speed)
    }

    private fun playSound(world: ServerWorld, entity: Entity) {
        world.playSound(entity, entity.blockPos, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.NEUTRAL, 0.5F, 1F + (Random.nextFloat() - 0.5F) / 10F)
    }

    override fun canApplyUpdateEffect(duration: Int, amplifier: Int) = true
}