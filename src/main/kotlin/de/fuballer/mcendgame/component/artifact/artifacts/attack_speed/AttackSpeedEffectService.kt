package de.fuballer.mcendgame.component.artifact.artifacts.attack_speed

import de.fuballer.mcendgame.event.PlayerDungeonJoinEvent
import de.fuballer.mcendgame.event.PlayerDungeonLeaveEvent
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.extension.PlayerExtension.getHighestArtifactTier
import org.bukkit.attribute.Attribute
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

@Component
class AttackSpeedEffectService : Listener {
    @EventHandler
    fun on(event: PlayerDungeonJoinEvent) {
        val player = event.player
        val tier = player.getHighestArtifactTier(AttackSpeedArtifactType) ?: return

        val (addedAttackSpeed) = AttackSpeedArtifactType.getValues(tier)
        val realAttackSpeed = 4.0 + addedAttackSpeed

        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED)?.baseValue = realAttackSpeed
    }

    @EventHandler
    fun on(event: PlayerDungeonLeaveEvent) {
        val player = event.player

        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED)?.baseValue = 4.0
    }
}