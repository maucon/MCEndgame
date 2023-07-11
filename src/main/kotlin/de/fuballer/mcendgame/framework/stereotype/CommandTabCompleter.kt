package de.fuballer.mcendgame.framework.stereotype

import org.bukkit.command.TabCompleter

interface CommandTabCompleter : TabCompleter, Injectable {
    fun getCommand(): String
}
