package de.fuballer.mcendgame.component.item.attribute.effects

import de.fuballer.mcendgame.component.damage.DamageCalculationEvent
import de.fuballer.mcendgame.component.item.attribute.CustomAttributeTypes
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.extension.EventExtension.cancel
import de.fuballer.mcendgame.util.extension.LivingEntityExtension.getCustomAttributes
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPotionEffectEvent
import org.bukkit.potion.PotionEffectType

private val CANCELLED_ACTIONS = listOf(
    EntityPotionEffectEvent.Action.ADDED,
    EntityPotionEffectEvent.Action.CHANGED,
)

private val NEGATIVE_EFFECTS = listOf(
    PotionEffectType.WEAKNESS,
    PotionEffectType.SLOWNESS,
    PotionEffectType.MINING_FATIGUE,
    PotionEffectType.BLINDNESS,
    PotionEffectType.DARKNESS,
    PotionEffectType.NAUSEA,
    PotionEffectType.WITHER,
    PotionEffectType.POISON,
    PotionEffectType.HUNGER,
    PotionEffectType.UNLUCK,
)

@Component
class NegativeEffectImmunityEffectService : Listener {
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun on(event: DamageCalculationEvent) {
        if (!event.damagedAttributes.containsKey(CustomAttributeTypes.NEGATIVE_EFFECT_IMMUNITY)) return

        event.onHitPotionEffects.clear()
    }

    @EventHandler
    fun on(event: EntityPotionEffectEvent) {
        if (!CANCELLED_ACTIONS.contains(event.action)) return
        if (!NEGATIVE_EFFECTS.contains(event.modifiedType)) return

        val entity = event.entity as? LivingEntity ?: return
        val entityCustomAttributes = entity.getCustomAttributes()
        if (!entityCustomAttributes.containsKey(CustomAttributeTypes.NEGATIVE_EFFECT_IMMUNITY)) return

        event.cancel()
    }
}