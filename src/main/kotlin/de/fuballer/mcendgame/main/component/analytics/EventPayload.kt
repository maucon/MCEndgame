package de.fuballer.mcendgame.main.component.analytics

import com.google.gson.JsonElement

data class EventPayload(
    val eventType: String,
    val modVersion: String,
    val payload: Any
)

enum class EventType(
    val type: String,
) {
    DUNGEON_JOIN("dungeon_join"),
    PLAYER_DUNGEON_DEATH("player_dungeon_death")
}

data class DungeonJoinPayload(
    val gamemode: String,
    val dungeonLevel: Int,
    val armor: List<PayloadItem?>,
    val hotbar: List<PayloadItem?>,
    val offhand: PayloadItem?
)

data class DungeonPlayerDeathPayload(
    val dungeonLevel: Int,
    val playerArmor: List<PayloadItem?>,
    val playerMainhand: PayloadItem?,
    val playerOffhand: PayloadItem?,
    val playerEffects: Map<String, Int>,
    val killerEntity: String,
    val killerArmor: List<PayloadItem?>,
    val killerMainhand: PayloadItem?,
    val killerOffhand: PayloadItem?,
    val killerEffects: Map<String, Int>,
)

data class PayloadItem(
    val id: String,
    val enchantments: Map<String, Int>,
    val customAttributes: JsonElement,
)