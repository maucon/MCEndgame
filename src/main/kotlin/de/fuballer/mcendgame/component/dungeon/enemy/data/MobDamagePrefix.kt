package de.fuballer.mcendgame.component.dungeon.enemy.data

import org.bukkit.ChatColor

enum class MobDamagePrefix(
    val minDamage: Int,
    val prefix: String
) {
    // they must be sorted by minDamage DESC
    CELESTIAL(60, "${ChatColor.AQUA}Celestial"),
    DIVINE(50, "${ChatColor.GOLD}Divine"),
    DEMONIC(40, "${ChatColor.DARK_RED}Demonic"),
    INSANE(30, "${ChatColor.RED}Insane"),
    POWERFUL(20, "${ChatColor.BLUE}Powerful"),
    STRONG(10, "${ChatColor.GREEN}Strong");

    companion object {
        fun fromPrefixStartsWith(value: String) = values().find { it.prefix.startsWith(value) }
    }
}
