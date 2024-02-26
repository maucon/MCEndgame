package de.fuballer.mcendgame.component.attribute.effects

import de.fuballer.mcendgame.component.attribute.AttributeType
import de.fuballer.mcendgame.component.damage.DamageCalculationEvent
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.extension.LivingEntityExtension.getCustomAttributes
import de.fuballer.mcendgame.util.extension.PlayerExtension.getHealOnBlockActivation
import de.fuballer.mcendgame.util.extension.PlayerExtension.setHealOnBlockActivation
import org.bukkit.Color
import org.bukkit.Particle
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import kotlin.math.min

@Component
class HealOnBlockEffectService : Listener {
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun on(event: DamageCalculationEvent) {
        val player = event.damaged as? Player ?: return
        if (!event.damageBlocked) return

        val damagedCustomAttributes = event.damaged.getCustomAttributes()
        val healOnBlockAttributes = damagedCustomAttributes[AttributeType.HEAL_ON_BLOCK] ?: return

        val healOnBlockAttribute = healOnBlockAttributes.sum()

        if (isHealOnBlockOnCooldown(player)) return
        player.setHealOnBlockActivation(System.currentTimeMillis())

        spawnParticles(player)

        val maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value
        val newHealth = min(player.health + healOnBlockAttribute, maxHealth)
        player.health = newHealth
    }

    private fun isHealOnBlockOnCooldown(player: Player): Boolean {
        val lastActivation = player.getHealOnBlockActivation() ?: return false
        return lastActivation + 7000 > System.currentTimeMillis()
    }

    private fun spawnParticles(player: Player) {
        val location = player.location
        val dustOptions = Particle.DustOptions(Color.fromRGB(50, 255, 50), 1.0f)

        player.world.spawnParticle(
            Particle.REDSTONE,
            location.x, location.y + 1, location.z,
            15, 0.2, 0.2, 0.2, 0.01, dustOptions
        )
    }
}