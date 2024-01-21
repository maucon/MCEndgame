package de.fuballer.mcendgame.component.dungeon.tweaks.friendly_fire

import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.WorldUtil
import org.bukkit.entity.Arrow
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent

@Component
class FriendlyFireService : Listener {
    @EventHandler
    fun on(event: EntityDamageByEntityEvent) {
        if (WorldUtil.isNotDungeonWorld(event.entity.world)) return

        if (event.damager !is Player) return
        if (event.damager !is Arrow || (event.damager as Arrow).shooter !is Player) return
        if (event.entity !is Player) return

        event.isCancelled = true
    }
}