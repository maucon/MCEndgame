package de.fuballer.mcendgame.util

import org.bukkit.entity.Arrow
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent

object EventUtil {
    fun getPlayerDamager(event: EntityDamageByEntityEvent): Player? {
        if (event.damager is Player) {
            return event.damager as Player
        }
        if (event.damager is Arrow && (event.damager as Arrow).shooter is Player) {
            return (event.damager as Arrow).shooter as Player
        }
        return null
    }
}