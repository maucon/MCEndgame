package de.fuballer.mcendgame.main.component.status_effect

import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes

class ResilienceEffect : MobEffect(MobEffectCategory.BENEFICIAL, 1349140) {
    companion object {
        val ATTRIBUTE_IDENTIFIER = IdentifierUtil.default("effect.resilience")

        fun getCustomAttributes(amplifier: Int) = listOf(
            CustomAttribute(
                CustomAttributeTypes.MORE_DAMAGE_TAKEN,
                0,
                DoubleRoll(DoubleBounds((amplifier + 1) * -0.02)),
            ),
        )
    }

    init {
        addAttributeModifier(Attributes.SCALE, ATTRIBUTE_IDENTIFIER, 0.015, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
    }

    override fun applyEffectTick(world: ServerLevel, entity: LivingEntity, amplifier: Int): Boolean {
        if (entity.tickCount % 20 != 0) return true

        if (entity.health < entity.maxHealth) {
            entity.heal(0.1F * (amplifier + 1))
        }

        return true
    }

    override fun shouldApplyEffectTickThisTick(duration: Int, amplifier: Int) = true
}