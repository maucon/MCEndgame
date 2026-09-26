package de.fuballer.mcendgame.main.component.status_effect

import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.entity.LivingEntity

private const val REGEN_INTERVAL = 20
private const val HEAL = 1f
private const val ADDITIONAL_HEAL_PER_AMPLIFIER = 0.5f

private const val BASE_MORE_DAMAGE_TAKEN = -0.1
private const val MORE_DAMAGE_TAKEN_PER_AMPLIFIER = -0.05

class VerdantEchoEffect : MobEffect(MobEffectCategory.BENEFICIAL, 1349140) {
    companion object {
        fun getCustomAttributes(amplifier: Int) = listOf(
            CustomAttribute(
                CustomAttributeTypes.MORE_DAMAGE_TAKEN,
                0,
                DoubleRoll(DoubleBounds(BASE_MORE_DAMAGE_TAKEN + (amplifier * MORE_DAMAGE_TAKEN_PER_AMPLIFIER))),
            )
        )
    }

    override fun applyEffectTick(world: ServerLevel, entity: LivingEntity, amplifier: Int): Boolean {
        if (entity.health >= entity.maxHealth) return true
        val heal = HEAL + amplifier * ADDITIONAL_HEAL_PER_AMPLIFIER
        entity.heal(heal)
        return true
    }

    override fun shouldApplyEffectTickThisTick(duration: Int, amplifier: Int) = duration % REGEN_INTERVAL == 0
}