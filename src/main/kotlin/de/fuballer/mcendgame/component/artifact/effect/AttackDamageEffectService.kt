package de.fuballer.mcendgame.component.artifact.effect

import de.fuballer.mcendgame.domain.artifact.ArtifactType
import de.fuballer.mcendgame.event.PlayerDungeonJoinEvent
import de.fuballer.mcendgame.event.PlayerDungeonLeaveEvent
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.ArtifactUtil
import de.fuballer.mcendgame.util.WorldUtil
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

@Component
class AttackDamageEffectService : Listener {
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        if (WorldUtil.isDungeonWorld(event.player.world)) {
            processJoin(event.player)
        } else {
            processLeave(event.player)
        }
    }

    @EventHandler
    fun onPlayerDungeonJoin(event: PlayerDungeonJoinEvent) {
        val player = event.player
        processJoin(player)
    }

    @EventHandler
    fun onPlayerDungeonLeave(event: PlayerDungeonLeaveEvent) {
        val player = event.player
        processLeave(player)
    }

    private fun processJoin(player: Player) {
        val tier = ArtifactUtil.getHighestTier(player, ArtifactType.ATTACK_DAMAGE) ?: return

        val (addedAttackDamage) = ArtifactType.ATTACK_DAMAGE.values[tier]!!
        val realAttackDamage = 1.0 + addedAttackDamage

        player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.baseValue = realAttackDamage
    }

    private fun processLeave(player: Player) {
        player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.baseValue = 1.0
    }
}