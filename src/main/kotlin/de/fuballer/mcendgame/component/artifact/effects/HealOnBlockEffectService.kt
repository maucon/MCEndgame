package de.fuballer.mcendgame.component.artifact.effects

import de.fuballer.mcendgame.domain.ArtifactType
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.technical.extension.PlayerExtension.getHealOnBlockArtifactActivation
import de.fuballer.mcendgame.technical.extension.PlayerExtension.setHealOnBlockArtifactActivation
import de.fuballer.mcendgame.util.ArtifactUtil
import de.fuballer.mcendgame.util.WorldUtil
import org.bukkit.Color
import org.bukkit.Particle
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import kotlin.math.min
import kotlin.random.Random

@Component
class HealOnBlockEffectService : Listener {
    @EventHandler
    fun on(event: EntityDamageByEntityEvent) {
        if (WorldUtil.isNotDungeonWorld(event.entity.world)) return

        val player = event.entity as? Player ?: return
        if (event.getDamage(EntityDamageEvent.DamageModifier.BLOCKING) == 0.0) return

        val tier = ArtifactUtil.getHighestTier(player, ArtifactType.HEAL_ON_BLOCK) ?: return

        val (blockChance, health, cooldown) = ArtifactType.HEAL_ON_BLOCK.values[tier]!!
        val realBlockChance = blockChance / 100.0
        if (Random.nextDouble() > realBlockChance) return

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