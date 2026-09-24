package de.fuballer.mcendgame.main.component.custom_attribute.effects.heal

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.getAllCustomAttributes
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.getHealingFactor
import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.tags.CustomTags
import de.fuballer.mcendgame.main.messaging.misc.LivingEntityDamagedEvent
import de.fuballer.mcendgame.main.util.extension.mixin.PlayerEntityMixinExtension.getAttackCooldownMultiplier
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.event.EventSubscriber
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player

@Injectable
class HealOnMeleeHitService {
    @EventSubscriber(sync = true)
    fun on(event: LivingEntityDamagedEvent) {
        if (!event.damageSource.`is`(CustomTags.MELEE_ATTACK)) return
        val attacker = event.damageSource.entity as? LivingEntity ?: return

        val attributes = attacker.getAllCustomAttributes()[CustomAttributeTypes.HEAL_ON_MELEE_HIT] ?: return
        val baseHeal = attributes.sumOf { (it.rolls[0] as DoubleRoll).getValue() }

        var healFactor = attacker.getHealingFactor()
        healFactor *= (attacker as? Player)?.getAttackCooldownMultiplier() ?: 1F

        val heal = (baseHeal * healFactor).toFloat()
        attacker.heal(heal)
    }
}