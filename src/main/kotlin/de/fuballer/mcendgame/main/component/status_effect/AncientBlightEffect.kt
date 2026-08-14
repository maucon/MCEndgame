package de.fuballer.mcendgame.main.component.status_effect

import net.minecraft.server.level.ServerLevel
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.entity.LivingEntity
import kotlin.math.max

class AncientBlightEffect : MobEffect(MobEffectCategory.HARMFUL, 1349140) {
    override fun applyEffectTick(world: ServerLevel, entity: LivingEntity, amplifier: Int): Boolean {
        if (entity.tickCount % 20 != 0) return true
        entity.health = max(entity.health - 1F * (amplifier + 1), 0F) // TODO instead use damage system and deal true damage
        return true
    }

    override fun shouldApplyEffectTickThisTick(duration: Int, amplifier: Int) = true
}