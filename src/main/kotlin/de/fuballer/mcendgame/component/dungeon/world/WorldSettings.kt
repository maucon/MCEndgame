package de.fuballer.mcendgame.component.dungeon.world

import org.bukkit.Difficulty
import org.bukkit.GameRule
import org.bukkit.World

object WorldSettings {
    const val WORLD_PREFIX = "worlds_mcendgame/"
    const val GENERATOR_SETTINGS = "{\"layers\": [], \"biome\":\"plains\"}"

    const val MAX_WORLD_EMPTY_TIME = 10
    const val WORLD_EMPTY_TEST_PERIOD = 60 * 20L // in ticks

    val DIFFICULTY = Difficulty.HARD
    const val WORLD_TIME = 18000L

    fun updateGameRules(world: World) {
        world.setGameRule(GameRule.KEEP_INVENTORY, true)
        world.setGameRule(GameRule.MOB_GRIEFING, false)
        world.setGameRule(GameRule.DO_MOB_SPAWNING, false)
        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false)
        world.setGameRule(GameRule.DO_WEATHER_CYCLE, false)
        world.setGameRule(GameRule.DO_FIRE_TICK, false)
        world.setGameRule(GameRule.RANDOM_TICK_SPEED, 0)
        world.setGameRule(GameRule.DO_TILE_DROPS, false)
        world.setGameRule(GameRule.BLOCK_EXPLOSION_DROP_DECAY, false)
        world.setGameRule(GameRule.DO_PATROL_SPAWNING, false)
        world.setGameRule(GameRule.TNT_EXPLOSION_DROP_DECAY, false)
        world.setGameRule(GameRule.DO_TRADER_SPAWNING, false)
        world.setGameRule(GameRule.GLOBAL_SOUND_EVENTS, false)
        world.setGameRule(GameRule.REDUCED_DEBUG_INFO, true)
        world.setGameRule(GameRule.DISABLE_RAIDS, true)
        world.setGameRule(GameRule.SPECTATORS_GENERATE_CHUNKS, false)
        world.setGameRule(GameRule.DO_WARDEN_SPAWNING, false)
        world.setGameRule(GameRule.DROWNING_DAMAGE, false)
        world.setGameRule(GameRule.FREEZE_DAMAGE, false)
        world.setGameRule(GameRule.DO_VINES_SPREAD, false)
        world.setGameRule(GameRule.SPAWN_CHUNK_RADIUS, 0)
    }
}