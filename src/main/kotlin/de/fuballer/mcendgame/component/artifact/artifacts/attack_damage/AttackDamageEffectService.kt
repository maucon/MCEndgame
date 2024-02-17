package de.fuballer.mcendgame.component.artifact.artifacts.attack_damage

import de.fuballer.mcendgame.event.PlayerDungeonJoinEvent
import de.fuballer.mcendgame.event.PlayerDungeonLeaveEvent
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.extension.PlayerExtension.getHighestArtifactTier
import de.fuballer.mcendgame.util.extension.WorldExtension.isDungeonWorld
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

@Component
class AttackDamageEffectService : Listener {
    @EventHandler
    fun on(event: PlayerJoinEvent) {
        if (event.player.world.isDungeonWorld()) {
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
        val tier = player.getHighestArtifactTier(AttackDamageArtifactType) ?: return

        val (addedAttackDamage) = AttackDamageArtifactType.getValues(tier)
        val realAttackDamage = 1.0 + addedAttackDamage

        player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.baseValue = realAttackDamage
    }

    private fun processLeave(player: Player) {
        player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.baseValue = 1.0
    }
}