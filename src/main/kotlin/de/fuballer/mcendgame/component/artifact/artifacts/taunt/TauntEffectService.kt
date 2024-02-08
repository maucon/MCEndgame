package de.fuballer.mcendgame.component.artifact.artifacts.taunt

import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.technical.extension.PlayerExtension.getHighestArtifactTier
import de.fuballer.mcendgame.util.EventUtil
import de.fuballer.mcendgame.util.WorldUtil
import org.bukkit.entity.Monster
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import kotlin.random.Random

@Component
class TauntEffectService : Listener {
    @EventHandler
    fun on(event: EntityDamageByEntityEvent) {
        if (WorldUtil.isNotDungeonWorld(event.entity.world)) return

        val player = EventUtil.getPlayerDamager(event) ?: return
        val tier = player.getHighestArtifactTier(TauntArtifactType) ?: return

        val (tauntProbability) = TauntArtifactType.getValues(tier)
        if (Random.nextDouble() * 100 > tauntProbability) return

        val entity = event.entity as? Monster ?: return
        entity.target = player
    }
}