package de.fuballer.mcendgame.component.artifact.artifacts.slow_when_hit

import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.technical.extension.EntityExtension.isEnemy
import de.fuballer.mcendgame.technical.extension.PlayerExtension.getHighestArtifactTier
import de.fuballer.mcendgame.util.WorldUtil
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

@Component
class SlowWhenHitEffectService : Listener {
    @EventHandler
    fun on(event: EntityDamageByEntityEvent) {
        if (WorldUtil.isNotDungeonWorld(event.entity.world)) return

        val player = event.entity as? Player ?: return
        val tier = player.getHighestArtifactTier(SlowWhenHitArtifactType) ?: return

        val (amplifier, duration) = SlowWhenHitArtifactType.getValues(tier)
        val realAmplifier = amplifier.toInt() - 1
        val realDuration = (duration * 20).toInt()

        val slowEffect = PotionEffect(PotionEffectType.SLOW, realDuration, realAmplifier, true)

        event.entity.getNearbyEntities(4.0, 4.0, 4.0)
            .filter { !it.isEnemy() }
            .filterIsInstance<LivingEntity>()
            .forEach { it.addPotionEffect(slowEffect) }
    }
}