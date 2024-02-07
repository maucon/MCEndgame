package de.fuballer.mcendgame.component.artifact.effects

import de.fuballer.mcendgame.component.artifact.ArtifactType
import de.fuballer.mcendgame.component.technical.extension.EntityExtension.isEnemy
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.ArtifactUtil
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
        val tier = ArtifactUtil.getHighestTier(player, ArtifactType.SLOW_WHEN_HIT) ?: return

        val (amplifier, duration) = ArtifactType.SLOW_WHEN_HIT.values[tier]!!
        val realAmplifier = amplifier.toInt() - 1
        val realDuration = (duration * 20).toInt()

        val slowEffect = PotionEffect(PotionEffectType.SLOW, realDuration, realAmplifier, true)

        event.entity.getNearbyEntities(4.0, 4.0, 4.0)
            .filter { !it.isEnemy() }
            .filterIsInstance<LivingEntity>()
            .forEach { it.addPotionEffect(slowEffect) }
    }
}