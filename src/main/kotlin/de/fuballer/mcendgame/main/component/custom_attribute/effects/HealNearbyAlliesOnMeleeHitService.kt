package de.fuballer.mcendgame.main.component.custom_attribute.effects

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.getAllCustomAttributes
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.getHealingFactor
import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.data.IntRoll
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.tags.CustomTags
import de.fuballer.mcendgame.main.messaging.misc.LivingEntityDamagedEvent
import de.fuballer.mcendgame.main.util.extension.EntityExtension.isAlly
import de.fuballer.mcendgame.main.util.extension.mixin.PlayerEntityMixinExtension.getAttackCooldownMultiplier
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.event.EventSubscriber
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player

@Injectable
class HealNearbyAlliesOnMeleeHitService {
    @EventSubscriber(sync = true)
    fun on(event: LivingEntityDamagedEvent) {
        if (!event.damageSource.`is`(CustomTags.MELEE_ATTACK)) return

        val attacker = event.damageSource.entity as? LivingEntity ?: return

        val attributes = attacker.getAllCustomAttributes()[CustomAttributeTypes.HEAL_NEARBY_ALLIES_ON_MELEE_HIT] ?: return
        val attributesValues = attributes.groupBy { (it.rolls[0] as IntRoll).getValue() }
            .mapValues { (_, values) -> values.sumOf { (it.rolls[1] as DoubleRoll).getValue() } }

        var healFactor = attacker.getHealingFactor()
        healFactor *= (attacker as? Player)?.getAttackCooldownMultiplier() ?: 1F

        attributesValues.forEach { range, baseHeal ->
            val allies = attacker.level().getEntitiesOfClass(LivingEntity::class.java, attacker.boundingBox.inflate(range.toDouble())) { attacker.isAlly(it) }
            val heal = (baseHeal * healFactor).toFloat()
            allies.forEach { it.heal(heal) }
        }
    }
}