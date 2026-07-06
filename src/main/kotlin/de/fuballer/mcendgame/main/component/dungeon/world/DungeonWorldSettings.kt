package de.fuballer.mcendgame.main.component.dungeon.world

import de.fuballer.mcendgame.main.component.dimension.CustomDimensions
import de.fuballer.mcendgame.main.configuration.RuntimeConfig
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import net.minecraft.registry.RegistryKey
import net.minecraft.world.Difficulty
import net.minecraft.world.GameRules
import net.minecraft.world.biome.Biome
import xyz.nucleoid.fantasy.RuntimeWorldConfig
import xyz.nucleoid.fantasy.util.VoidChunkGenerator
import java.util.*

object DungeonWorldSettings {
    const val EMPTY_WORLD_CHECK_PERIOD = 10 * 60 * 20 // ticks
    const val MAX_EMPTY_TIME = 8 * 60L // seconds

    fun getWorldConfig(
        biome: RegistryKey<Biome>,
    ): RuntimeWorldConfig = RuntimeWorldConfig()
        .setDimensionType(CustomDimensions.DUNGEON)
        .setDifficulty(Difficulty.HARD)
        .setGenerator(VoidChunkGenerator(RuntimeConfig.SERVER, biome))
        .setTimeOfDay(18000L)
        .setGameRule(GameRules.KEEP_INVENTORY, true)
        .setGameRule(GameRules.DO_MOB_GRIEFING, false)
        .setGameRule(GameRules.DO_MOB_SPAWNING, false)
        .setGameRule(GameRules.DO_DAYLIGHT_CYCLE, false)
        .setGameRule(GameRules.DO_WEATHER_CYCLE, false)
        .setGameRule(GameRules.DO_FIRE_TICK, false)
        .setGameRule(GameRules.RANDOM_TICK_SPEED, 0)
        .setGameRule(GameRules.DO_TILE_DROPS, false)
        .setGameRule(GameRules.BLOCK_EXPLOSION_DROP_DECAY, false)
        .setGameRule(GameRules.DO_PATROL_SPAWNING, false)
        .setGameRule(GameRules.TNT_EXPLOSION_DROP_DECAY, false)
        .setGameRule(GameRules.DO_TRADER_SPAWNING, false)
        .setGameRule(GameRules.GLOBAL_SOUND_EVENTS, false)
        .setGameRule(GameRules.REDUCED_DEBUG_INFO, true)
        .setGameRule(GameRules.DISABLE_RAIDS, true)
        .setGameRule(GameRules.SPECTATORS_GENERATE_CHUNKS, false)
        .setGameRule(GameRules.DO_WARDEN_SPAWNING, false)
        .setGameRule(GameRules.DO_VINES_SPREAD, false)
        .setGameRule(GameRules.UNIVERSAL_ANGER, false)

    const val DUNGEON_WORLD_PREFIX = "dungeon-world"
    fun generateIdentifier() = IdentifierUtil.default("$DUNGEON_WORLD_PREFIX-${UUID.randomUUID()}")
}