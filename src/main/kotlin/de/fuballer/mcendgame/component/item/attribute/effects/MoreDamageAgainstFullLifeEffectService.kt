package de.fuballer.mcendgame.component.item.attribute.effects

import de.fuballer.mcendgame.component.damage.DamageCalculationEvent
import de.fuballer.mcendgame.component.item.attribute.CustomAttributeTypes
import de.fuballer.mcendgame.framework.annotation.Service
import de.fuballer.mcendgame.util.extension.AttributeRollExtension.getFirstAsDouble
import org.bukkit.Color
import org.bukkit.Particle
import org.bukkit.attribute.Attribute
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener

@Service
class MoreDamageAgainstFullLifeEffectService : Listener {
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun on(event: DamageCalculationEvent) {
        val moreDamageAttributes = event.damagerAttributes[CustomAttributeTypes.MORE_DAMAGE_AGAINST_FULL_LIFE] ?: return

        val maxHealth = event.damaged.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value
        val currentHealth = event.damaged.health
        if (maxHealth > currentHealth + 0.1) return

        moreDamageAttributes.forEach {
            val moreDamage = it.attributeRolls.getFirstAsDouble()
            event.moreDamage.add(moreDamage)
        }

        spawnParticles(event.damaged)
    }

    private fun spawnParticles(entity: LivingEntity) {
        val location = entity.location
        val dustOptions = Particle.DustOptions(Color.fromRGB(255, 50, 50), 1.0f)

        entity.world.spawnParticle(
            Particle.DUST,
            location.x, location.y + 1.3, location.z,
            25, 0.2, 0.3, 0.2, 0.01, dustOptions
        )
    }
}