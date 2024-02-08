package de.fuballer.mcendgame.component.artifact.artifacts.bow_damage

import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.technical.extension.PlayerExtension.getHighestArtifactTier
import de.fuballer.mcendgame.util.WorldUtil
import org.bukkit.entity.Arrow
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent

@Component
class BowDamageEffectService : Listener {
    @EventHandler
    fun on(event: EntityDamageByEntityEvent) {
        if (WorldUtil.isNotDungeonWorld(event.entity.world)) return
        if (!(event.damager is Arrow && (event.damager as Arrow).shooter is Player)) return

        val player = (event.damager as Arrow).shooter as Player
        val tier = player.getHighestArtifactTier(BowDamageArtifactType) ?: return

        val (incDmg) = BowDamageArtifactType.getValues(tier)
        event.damage *= incDmg
    }
}