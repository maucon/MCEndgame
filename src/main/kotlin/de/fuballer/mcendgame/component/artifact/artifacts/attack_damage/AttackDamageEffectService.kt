package de.fuballer.mcendgame.component.artifact.artifacts.attack_damage

import de.fuballer.mcendgame.event.PlayerDungeonJoinEvent
import de.fuballer.mcendgame.event.PlayerDungeonLeaveEvent
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.extension.PlayerExtension.getHighestArtifactTier
import org.bukkit.attribute.Attribute
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

@Component
class AttackDamageEffectService : Listener {
    @EventHandler
    fun on(event: PlayerDungeonJoinEvent) {
        val player = event.player
        val tier = player.getHighestArtifactTier(AttackDamageArtifactType) ?: return

        val (addedAttackDamage) = AttackDamageArtifactType.getValues(tier)
        val realAttackDamage = 1.0 + addedAttackDamage

        player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.baseValue = realAttackDamage
    }

    @EventHandler
    fun on(event: PlayerDungeonLeaveEvent) {
        val player = event.player

        player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.baseValue = 1.0
    }
}