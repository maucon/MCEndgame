package de.fuballer.mcendgame.component.artifact.artifacts.effect_steal

import de.fuballer.mcendgame.event.DungeonEntityDeathEvent
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.technical.extension.EntityExtension.isEnemy
import de.fuballer.mcendgame.technical.extension.PlayerExtension.getHighestArtifactTier
import de.fuballer.mcendgame.util.WorldUtil
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.potion.PotionEffect
import kotlin.math.min
import kotlin.random.Random

@Component
class EffectStealEffectService : Listener {
    @EventHandler
    fun on(event: DungeonEntityDeathEvent) {
        val entity = event.entity

        if (entity is Player) return
        if (!entity.isEnemy()) return

        if (WorldUtil.isNotDungeonWorld(entity.world)) return

        val player = entity.killer ?: return
        val tier = player.getHighestArtifactTier(EffectStealArtifactType) ?: return

        val (chance, duration, maxAmplifier) = EffectStealArtifactType.getValues(tier)
        if (Random.nextDouble() > chance) return

        val activeEffects = entity.activePotionEffects
        if (activeEffects.isEmpty()) return

        player.playSound(player.location, Sound.BLOCK_AMETHYST_CLUSTER_BREAK, 1f, 1f)

        val effect = activeEffects.random()

        val realMaxAmplifier = maxAmplifier.toInt() + 1
        val amplifier = min(effect.amplifier, realMaxAmplifier)
        val tickDuration = duration.toInt() * 20

        val modifiedEffect = PotionEffect(effect.type, tickDuration, amplifier, true)
        player.addPotionEffect(modifiedEffect)
    }
}