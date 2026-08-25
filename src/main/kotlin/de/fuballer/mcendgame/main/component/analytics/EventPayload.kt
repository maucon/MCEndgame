package de.fuballer.mcendgame.main.component.analytics

import com.google.gson.JsonElement

data class EventPayload(
    val eventType: String,
    val modVersion: String,
    val payload: Any,
    val schemaVersion : Int = 1,
)

enum class EventType(
    val type: String,
) {
    DUNGEON_JOIN("dungeon_join"),
    PLAYER_DUNGEON_DEATH("player_dungeon_death"),
    DUNGEON_BOSS_KILLED("dungeon_boss_killed"),
    LOOT_GOBLIN_KILLED("loot_goblin_killed"),
    ELITE_KILLED("elite_killed"),
    TOTEM_ENCOUNTER_STARTED("totem_encounter_started"),
    SCARRED_ONE_SELECTED("scarred_one_selected"),
}

data class DungeonJoinPayload(
    val dungeon: DungeonDataPayload,
    val player: PlayerLoadoutPayload,
    val schemaVersion : Int = 1,
)

data class DungeonPlayerDeathPayload(
    val dungeon: DungeonDataPayload,
    val player: PlayerLoadoutPayload,
    val killer: EntityLoadoutPayload?,
    val schemaVersion : Int = 1,
)

data class DungeonBossKilledPayload(
    val dungeon: DungeonDataPayload,
    val boss: String,
    val players: List<PlayerLoadoutPayload>,
    val schemaVersion : Int = 1,
)

data class SpecialEnemyKilledPayload(
    val dungeon: DungeonDataPayload,
    val enemyLoadout: EntityLoadoutPayload,
    val schemaVersion : Int = 1,
)

data class ScarredOneSelectedPayload(
    val dungeon: DungeonDataPayload,
    val scarredOne: ScarredOnePayload,
    val schemaVersion : Int = 1,
)

data class DungeonDataPayload(
    val seed: Long,
    val dungeonLevel: Int,
    val aspects: List<String>,
    val timeSinceCreation: Long,
    val schemaVersion : Int = 1,
)

data class PlayerLoadoutPayload(
    val armor: List<PayloadItem?>,
    val mainhand: PayloadItem?,
    val offhand: PayloadItem?,
    val hotbar: List<PayloadItem?>,
    val effects: Map<String, Int>,
    val gamemode: String,
    val schemaVersion : Int = 1,
)

data class EntityLoadoutPayload(
    val entity: String,
    val armor: List<PayloadItem?>,
    val mainhand: PayloadItem?,
    val offhand: PayloadItem?,
    val effects: Map<String, Int>,
    val schemaVersion : Int = 1,
)

data class ScarredOnePayload(
    val accepted: Boolean,
    val positiveEffects: JsonElement,
    val negativeEffects: JsonElement,
    val schemaVersion : Int = 1,
)

data class PayloadItem(
    val id: String,
    val enchantments: Map<String, Int>,
    val customAttributes: JsonElement,
    val schemaVersion : Int = 1,
)