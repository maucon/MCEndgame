package de.fuballer.mcendgame.component.artifact.effect

import de.fuballer.mcendgame.domain.artifact.ArtifactType
import de.fuballer.mcendgame.domain.technical.persistent_data.TypeKeys
import de.fuballer.mcendgame.event.DungeonEntityDeathEvent
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.ArtifactUtil
import de.fuballer.mcendgame.util.PersistentDataUtil
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
        if (WorldUtil.isNotDungeonWorld(entity.world)) return
        if (!PersistentDataUtil.getBooleanValue(entity, TypeKeys.IS_ENEMY)) return

        val player = entity.killer ?: return

        val tier = ArtifactUtil.getHighestTier(player, ArtifactType.EFFECT_STEAL) ?: return

        val (chance, duration, maxAmplifier) = ArtifactType.EFFECT_STEAL.values[tier]!!
        if (Random.nextDouble() > chance / 100.0) return

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