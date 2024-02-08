package de.fuballer.mcendgame.component.artifact.artifacts.inc_damage_per_missing_health

import de.fuballer.mcendgame.event.DamageCalculationEvent
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.ArtifactUtil
import de.fuballer.mcendgame.util.WorldUtil
import org.bukkit.attribute.Attribute
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

@Component
class IncDamagePerMissingHealthEffectService : Listener {
    @EventHandler
    fun on(event: DamageCalculationEvent) {
        if (WorldUtil.isNotDungeonWorld(event.player.world)) return

        val tier = ArtifactUtil.getHighestTier(event.player, IncDamagePerMissingHealthArtifactType) ?: return

        val (incDmgPerHealth) = IncDamagePerMissingHealthArtifactType.getValues(tier)
        val realIncDmgPerHealth = incDmgPerHealth / 100

        val missingHealth = event.player.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value - event.player.health
        val increasedDamage = realIncDmgPerHealth * (missingHealth / 2).toInt()

        event.increasedDamage.add(increasedDamage)
    }
}