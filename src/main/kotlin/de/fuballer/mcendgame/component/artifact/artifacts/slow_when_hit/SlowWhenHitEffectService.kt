package de.fuballer.mcendgame.component.artifact.artifacts.slow_when_hit

import de.fuballer.mcendgame.component.damage.DamageCalculationEvent
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.technical.extension.EntityExtension.isEnemy
import de.fuballer.mcendgame.technical.extension.PlayerExtension.getHighestArtifactTier
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

@Component
class SlowWhenHitEffectService : Listener {
    @EventHandler(ignoreCancelled = true)
    fun on(event: DamageCalculationEvent) {
        if (!event.isDungeonWorld) return

        val player = event.damager as? Player ?: return
        val tier = player.getHighestArtifactTier(SlowWhenHitArtifactType) ?: return

        val (amplifier, duration) = SlowWhenHitArtifactType.getValues(tier)
        val realAmplifier = amplifier.toInt() - 1
        val realDuration = (duration * 20).toInt()

        val slowEffect = PotionEffect(PotionEffectType.SLOW, realDuration, realAmplifier, true)

        event.damaged.getNearbyEntities(4.0, 4.0, 4.0)
            .filter { !it.isEnemy() }
            .filterIsInstance<LivingEntity>()
            .forEach { it.addPotionEffect(slowEffect) }
    }
}