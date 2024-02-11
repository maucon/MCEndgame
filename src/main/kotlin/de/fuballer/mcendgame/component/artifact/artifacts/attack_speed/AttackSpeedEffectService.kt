package de.fuballer.mcendgame.component.artifact.artifacts.attack_speed

import de.fuballer.mcendgame.event.PlayerDungeonJoinEvent
import de.fuballer.mcendgame.event.PlayerDungeonLeaveEvent
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.technical.extension.PlayerExtension.getHighestArtifactTier
import de.fuballer.mcendgame.util.WorldUtil
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

@Component
class AttackSpeedEffectService : Listener {
    @EventHandler
    fun on(event: PlayerJoinEvent) {
        if (WorldUtil.isDungeonWorld(event.player.world)) {
            processJoin(event.player)
        } else {
            processLeave(event.player)
        }
    }

    @EventHandler
    fun on(event: PlayerDungeonJoinEvent) {
        val player = event.player
        processJoin(player)
    }

    @EventHandler
    fun on(event: PlayerDungeonLeaveEvent) {
        val player = event.player
        processLeave(player)
    }

    private fun processJoin(player: Player) {
        val tier = player.getHighestArtifactTier(AttackSpeedArtifactType) ?: return

        val (addedAttackSpeed) = AttackSpeedArtifactType.getValues(tier)
        val realAttackSpeed = 4.0 + addedAttackSpeed

        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED)?.baseValue = realAttackSpeed
    }

    private fun processLeave(player: Player) {
        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED)?.baseValue = 4.0
    }
}