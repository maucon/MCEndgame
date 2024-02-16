package de.fuballer.mcendgame.component.artifact.artifacts.taunt

import de.fuballer.mcendgame.component.damage.DamageCalculationEvent
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.EntityUtil
import de.fuballer.mcendgame.util.extension.PlayerExtension.getHighestArtifactTier
import org.bukkit.entity.Monster
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import kotlin.random.Random

@Component
class TauntEffectService : Listener {
    @EventHandler(ignoreCancelled = true)
    fun on(event: DamageCalculationEvent) {
        if (!event.isDungeonWorld) return

        val player = EntityUtil.getPlayerDamager(event.damager) ?: return
        val tier = player.getHighestArtifactTier(TauntArtifactType) ?: return

        val (chance) = TauntArtifactType.getValues(tier)
        if (Random.nextDouble() > chance) return

        val entity = event.damaged as? Monster ?: return
        entity.target = player
    }
}