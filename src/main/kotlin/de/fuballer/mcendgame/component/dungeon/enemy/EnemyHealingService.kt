package de.fuballer.mcendgame.component.dungeon.enemy

import de.fuballer.mcendgame.framework.annotation.Service
import de.fuballer.mcendgame.util.extension.WorldExtension.isDungeonWorld
import org.bukkit.attribute.Attribute
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPotionEffectEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

private val POTION_EFFECT = PotionEffect(PotionEffectType.LUCK, 1, 0, false, false)

@Service
class EnemyHealingService : Listener {
    @EventHandler
    fun on(event: EntityPotionEffectEvent) {
        if (event.cause != EntityPotionEffectEvent.Cause.EXPIRATION) return
        val effect = event.oldEffect ?: return
        if (effect.type != POTION_EFFECT.type) return

        val entity = event.entity as? LivingEntity ?: return
        if (!entity.world.isDungeonWorld()) return

        entity.health = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value
    }

    companion object {
        /**
         * Heals the given entity to full health after a one tick delay
         */
        fun LivingEntity.healOnLoad() {
            this.addPotionEffect(POTION_EFFECT)
        }
    }
}