package de.fuballer.mcendgame.component.killer

import org.bukkit.ChatColor
import org.bukkit.Material

object KillerSettings {
    const val COMMAND_NAME = "killer"

    const val INVENTORY_TITLE = "Killer"
    val NO_KILLER_FOUND_MESSAGE = ChatColor.RED.toString() + "No entity found."

    val POTION_EFFECT_ITEM_NAME = ChatColor.BLUE.toString() + "Effect"
    val DEFAULT_SPAWN_EGG = Material.ALLAY_SPAWN_EGG
    val BABY_LORE = listOf(ChatColor.BLUE.toString() + "Baby")
}
