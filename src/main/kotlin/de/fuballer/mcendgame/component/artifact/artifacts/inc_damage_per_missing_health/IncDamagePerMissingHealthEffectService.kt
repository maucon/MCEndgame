package de.fuballer.mcendgame.component.artifact.artifacts.inc_damage_per_missing_health

import de.fuballer.mcendgame.component.damage.DamageCalculationEvent
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.extension.PlayerExtension.getHighestArtifactTier
import de.fuballer.mcendgame.util.extension.WorldExtension.isDungeonWorld
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

@Component
class IncDamagePerMissingHealthEffectService : Listener {
    @EventHandler(ignoreCancelled = true)
    fun on(event: DamageCalculationEvent) {
        val player = event.damager as? Player ?: return
        if (!player.world.isDungeonWorld()) return

        val tier = player.getHighestArtifactTier(IncDamagePerMissingHealthArtifactType) ?: return

        val (incDmgPerHealth) = IncDamagePerMissingHealthArtifactType.getValues(tier)

        val missingHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value - player.health
        val increasedDamage = incDmgPerHealth * (missingHealth / 2).toInt()

        event.increasedDamage.add(increasedDamage)
    }
}