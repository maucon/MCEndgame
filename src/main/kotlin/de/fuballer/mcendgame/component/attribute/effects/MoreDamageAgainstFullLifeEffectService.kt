package de.fuballer.mcendgame.component.attribute.effects

import de.fuballer.mcendgame.component.attribute.AttributeType
import de.fuballer.mcendgame.component.damage.DamageCalculationEvent
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.extension.LivingEntityExtension.getCustomAttributes
import org.bukkit.Color
import org.bukkit.Particle
import org.bukkit.attribute.Attribute
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener

@Component
class MoreDamageAgainstFullLifeEffectService : Listener {
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun on(event: DamageCalculationEvent) {
        val damagerCustomAttributes = event.damager.getCustomAttributes()
        val moreDamageAttributes = damagerCustomAttributes[AttributeType.MORE_DAMAGE_AGAINST_FULL_LIFE] ?: return

        if (event.damaged.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value > event.damaged.health + 0.1) return

        spawnParticles(event.damaged)

        moreDamageAttributes.forEach {
            event.moreDamage.add(it)
        }
    }

    private fun spawnParticles(entity: LivingEntity) {
        val location = entity.location
        val dustOptions = Particle.DustOptions(Color.fromRGB(255, 50, 50), 1.0f)

        entity.world.spawnParticle(
            Particle.REDSTONE,
            location.x, location.y + 1.3, location.z,
            25, 0.2, 0.3, 0.2, 0.01, dustOptions
        )
    }
}