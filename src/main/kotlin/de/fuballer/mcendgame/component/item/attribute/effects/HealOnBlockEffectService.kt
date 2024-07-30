package de.fuballer.mcendgame.component.item.attribute.effects

import de.fuballer.mcendgame.component.damage.DamageCalculationEvent
import de.fuballer.mcendgame.component.item.attribute.CustomAttributeTypes
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.extension.AttributeRollExtension.getFirstAsDouble
import de.fuballer.mcendgame.util.extension.LivingEntityExtension.heal
import de.fuballer.mcendgame.util.extension.PlayerExtension.getHealOnBlockActivation
import de.fuballer.mcendgame.util.extension.PlayerExtension.setHealOnBlockActivation
import org.bukkit.Color
import org.bukkit.Particle
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener

private const val COOLDOWN = 5000 // should equal attribute type text

@Component
class HealOnBlockEffectService : Listener {
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun on(event: DamageCalculationEvent) {
        val player = event.damaged as? Player ?: return
        if (!event.isDamageBlocked) return

        val healOnBlockAttributes = event.damagedAttributes[CustomAttributeTypes.HEAL_ON_BLOCK] ?: return

        if (isHealOnBlockOnCooldown(player)) return
        player.setHealOnBlockActivation(System.currentTimeMillis())

        val healOnBlockAttribute = healOnBlockAttributes.sumOf { it.attributeRolls.getFirstAsDouble() }
        val maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value
        val amount = maxHealth * healOnBlockAttribute

        player.heal(amount)
        spawnParticles(player)
    }

    private fun isHealOnBlockOnCooldown(player: Player): Boolean {
        val lastActivation = player.getHealOnBlockActivation() ?: return false
        return lastActivation + COOLDOWN > System.currentTimeMillis()
    }

    private fun spawnParticles(player: Player) {
        val location = player.location
        val dustOptions = Particle.DustOptions(Color.fromRGB(50, 255, 50), 1.0f)

        player.world.spawnParticle(
            Particle.DUST,
            location.x, location.y + 1, location.z,
            15, 0.2, 0.2, 0.2, 0.01, dustOptions
        )
    }
}