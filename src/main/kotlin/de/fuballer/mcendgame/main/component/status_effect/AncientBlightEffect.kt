package de.fuballer.mcendgame.main.component.status_effect

import de.fuballer.mcendgame.main.component.damage.dealing.DamageDealing.dealTrueDamage
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player

private const val DAMAGE_PER_LEVEL = 1F
private const val COLOR = 1349140

class AncientBlightEffect : MobEffect(MobEffectCategory.HARMFUL, COLOR) {
    override fun applyEffectTick(world: ServerLevel, entity: LivingEntity, amplifier: Int): Boolean {
        if (entity.tickCount % 20 != 0) return true
        if (entity is Player && (entity.isCreative || entity.isSpectator)) return true
        entity.dealTrueDamage(DAMAGE_PER_LEVEL * (amplifier + 1))
        return true
    }

    override fun shouldApplyEffectTickThisTick(duration: Int, amplifier: Int) = true
}