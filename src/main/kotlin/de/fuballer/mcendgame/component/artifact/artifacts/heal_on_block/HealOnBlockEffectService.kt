package de.fuballer.mcendgame.component.artifact.artifacts.heal_on_block

import de.fuballer.mcendgame.component.damage.DamageCalculationEvent
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.technical.extension.PlayerExtension.getHealOnBlockArtifactActivation
import de.fuballer.mcendgame.technical.extension.PlayerExtension.getHighestArtifactTier
import de.fuballer.mcendgame.technical.extension.PlayerExtension.setHealOnBlockArtifactActivation
import org.bukkit.Color
import org.bukkit.Particle
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import kotlin.math.min
import kotlin.random.Random

@Component
class HealOnBlockEffectService : Listener {
    @EventHandler(ignoreCancelled = true)
    fun on(event: DamageCalculationEvent) {
        if (!event.isDungeonWorld) return

        val player = event.damaged as? Player ?: return
        if (!event.damageBlocked) return

        val tier = player.getHighestArtifactTier(HealOnBlockArtifactType) ?: return

        val (chance, health, cooldown) = HealOnBlockArtifactType.getValues(tier)
        if (Random.nextDouble() > chance) return

        if (isHealOnBlockOnCooldown(player, cooldown)) return
        player.setHealOnBlockArtifactActivation(System.currentTimeMillis())

        spawnParticles(player)

        val maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value
        val newHealth = min(player.health + health, maxHealth)
        player.health = newHealth
    }

    private fun isHealOnBlockOnCooldown(player: Player, cooldown: Double): Boolean {
        val lastActivation = player.getHealOnBlockArtifactActivation() ?: return false
        val cdMS = (cooldown * 1000).toLong()
        return lastActivation + cdMS > System.currentTimeMillis()
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