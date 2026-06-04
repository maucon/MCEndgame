package de.fuballer.mcendgame.main.component.custom_attribute.effects

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.asDoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.asIntRoll
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.getAllCustomAttributes
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.tags.CustomTags
import de.fuballer.mcendgame.main.messaging.misc.LivingEntityDamagedEvent
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.addTemporaryAttributeModifier
import de.fuballer.mcendgame.main.util.extension.mixin.PlayerEntityMixinExtension.getAttackCooldownMultiplier
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil.defaultJava
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.event.EventSubscriber
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.player.Player
import java.util.*

@Injectable
class StackingMoreAttackSpeedOnHitService {
    private val attributeModifierIdentifierBase = "stacking_more_attack_speed_on_melee_hit_"

    @EventSubscriber(sync = true)
    fun on(event: LivingEntityDamagedEvent) {
        if (!event.damageSource.`is`(CustomTags.MELEE_ATTACK)) return

        val attacker = event.damageSource.entity as? LivingEntity ?: return
        val attackCooldownMultiplier = (attacker as? Player)?.getAttackCooldownMultiplier() ?: 1F

        val attributes = attacker.getAllCustomAttributes()[CustomAttributeTypes.STACKING_MORE_ATTACK_SPEED_ON_MELEE_HIT] ?: return
        attributes.forEach { attribute ->
            val moreAttackSpeed = attribute.rolls[0].asDoubleRoll().getValue() * attackCooldownMultiplier
            val duration = attribute.rolls[1].asIntRoll().getValue() * 20
            val identifier = defaultJava(attributeModifierIdentifierBase + attribute.id + "_" + UUID.randomUUID())

            attacker.addTemporaryAttributeModifier(
                Attributes.ATTACK_SPEED,
                identifier,
                duration,
                moreAttackSpeed,
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
            )
        }
    }
}