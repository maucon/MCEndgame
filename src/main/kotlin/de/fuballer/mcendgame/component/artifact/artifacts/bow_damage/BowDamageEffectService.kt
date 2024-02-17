package de.fuballer.mcendgame.component.artifact.artifacts.bow_damage

import de.fuballer.mcendgame.component.damage.DamageCalculationEvent
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.EntityUtil
import de.fuballer.mcendgame.util.extension.PlayerExtension.getHighestArtifactTier
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

@Component
class BowDamageEffectService : Listener {
    @EventHandler(ignoreCancelled = true)
    fun on(event: DamageCalculationEvent) {
        if (!event.isDungeonWorld) return

        val player = EntityUtil.getPlayerDamager(event.damager) ?: return
        val tier = player.getHighestArtifactTier(BowDamageArtifactType) ?: return

        val (incDmg) = BowDamageArtifactType.getValues(tier)
        event.increasedDamage.add(incDmg)
    }
}