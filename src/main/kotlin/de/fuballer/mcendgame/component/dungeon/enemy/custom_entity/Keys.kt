package de.fuballer.mcendgame.component.dungeon.enemy.custom_entity

import de.fuballer.mcendgame.MCEndgame
import org.bukkit.NamespacedKey

object Keys {
    val DROP_BASE_LOOT = NamespacedKey(MCEndgame.INSTANCE, "drop-base-loot")
    val DROP_EQUIPMENT = NamespacedKey(MCEndgame.INSTANCE, "drop-equipment")
    val MAP_TIER = NamespacedKey(MCEndgame.INSTANCE, "map-tier")
    val IS_MINION = NamespacedKey(MCEndgame.INSTANCE, "is-minion")
}