package de.fuballer.mcendgame.component.killer

import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.EntityType
import java.util.*

object KillerSettings {
    const val COMMAND_NAME = "killer"

    val INVENTORY_TITLE = ChatColor.BLACK.toString() + "Killer"
    val NO_KILLER_FOUND_MESSAGE = ChatColor.RED.toString() + "No entity found."

    val POTION_EFFECT_ITEM_NAME = ChatColor.BLUE.toString() + "Effect"
    val DEFAULT_SPAWN_EGG = Material.EGG
    val ENTITY_SPAWN_EGGS: MutableMap<EntityType, Material> = EnumMap(EntityType::class.java)
    val BABY_LORE = listOf(ChatColor.BLUE.toString() + "Baby")

    init {
        ENTITY_SPAWN_EGGS[EntityType.ZOMBIE] = Material.ZOMBIE_SPAWN_EGG
        ENTITY_SPAWN_EGGS[EntityType.HUSK] = Material.HUSK_SPAWN_EGG
        ENTITY_SPAWN_EGGS[EntityType.WITHER_SKELETON] = Material.WITHER_SKELETON_SPAWN_EGG
        ENTITY_SPAWN_EGGS[EntityType.SKELETON] = Material.SKELETON_SPAWN_EGG
        ENTITY_SPAWN_EGGS[EntityType.STRAY] = Material.STRAY_SPAWN_EGG
        ENTITY_SPAWN_EGGS[EntityType.CREEPER] = Material.CREEPER_SPAWN_EGG
        ENTITY_SPAWN_EGGS[EntityType.SPIDER] = Material.SPIDER_SPAWN_EGG
        ENTITY_SPAWN_EGGS[EntityType.CAVE_SPIDER] = Material.CAVE_SPIDER_SPAWN_EGG
        ENTITY_SPAWN_EGGS[EntityType.WITCH] = Material.WITCH_SPAWN_EGG
        ENTITY_SPAWN_EGGS[EntityType.BLAZE] = Material.BLAZE_SPAWN_EGG
        ENTITY_SPAWN_EGGS[EntityType.RAVAGER] = Material.RAVAGER_SPAWN_EGG
        ENTITY_SPAWN_EGGS[EntityType.EVOKER] = Material.EVOKER_SPAWN_EGG
        ENTITY_SPAWN_EGGS[EntityType.VEX] = Material.VEX_SPAWN_EGG
        ENTITY_SPAWN_EGGS[EntityType.PLAYER] = Material.VILLAGER_SPAWN_EGG
    }
}
