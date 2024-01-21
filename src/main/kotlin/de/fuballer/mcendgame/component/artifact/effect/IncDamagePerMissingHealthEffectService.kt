package de.fuballer.mcendgame.component.artifact.effect

import de.fuballer.mcendgame.domain.artifact.ArtifactType
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.ArtifactUtil
import de.fuballer.mcendgame.util.EventUtil
import de.fuballer.mcendgame.util.WorldUtil
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Arrow
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent

@Component
class IncDamagePerMissingHealthEffectService : Listener {
    @EventHandler
    fun on(event: EntityDamageByEntityEvent) {
        if (WorldUtil.isNotDungeonWorld(event.entity.world)) return

        val player = EventUtil.getPlayerDamager(event) ?: return
        process(player, event)
    }

    private fun process(player: Player, event: EntityDamageByEntityEvent) {
        val tier = ArtifactUtil.getHighestTier(player, ArtifactType.INC_DMG_PER_MISSING_HEALTH) ?: return

        val (incDmgPerHealth) = ArtifactType.INC_DMG_PER_MISSING_HEALTH.values[tier]!!
        val realIncDmgPerHealth = incDmgPerHealth / 100

        val missingHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value - player.health
        val dmgMultiplier = 1 + realIncDmgPerHealth * (missingHealth / 2).toInt()
        val incDamage = event.damage * dmgMultiplier

        event.damage = incDamage
    }

    fun s(event: EntityDamageByEntityEvent): Player? {
        return if (event.damager is Player) {
            event.damager as Player
        } else if (event.damager is Arrow && (event.damager as Arrow).shooter is Player) {
            (event.damager as Arrow).shooter as Player
        } else null
    }
}