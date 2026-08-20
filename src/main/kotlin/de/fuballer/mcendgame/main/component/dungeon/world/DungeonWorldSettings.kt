package de.fuballer.mcendgame.main.component.dungeon.world

import de.fuballer.mcendgame.main.configuration.RuntimeConfig
import de.fuballer.mcendgame.main.runtime_worlds.RuntimeLevelConfig
import de.fuballer.mcendgame.main.runtime_worlds.util.VoidChunkGenerator
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.gamerules.GameRules
import java.util.*

object DungeonWorldSettings {
    const val EMPTY_WORLD_CHECK_PERIOD = 10 * 60 * 20 // ticks
    const val MAX_EMPTY_TIME = 8 * 60L // seconds

    fun getWorldConfig(
        biome: ResourceKey<Biome>,
    ): RuntimeLevelConfig = RuntimeLevelConfig()
        .setGenerator(VoidChunkGenerator(RuntimeConfig.SERVER, biome))
        .setClockTime(14700)
        .setGameRule(GameRules.KEEP_INVENTORY, true)
        .setGameRule(GameRules.MOB_GRIEFING, false)
        .setGameRule(GameRules.SPAWN_MOBS, false)
        .setGameRule(GameRules.ADVANCE_TIME, false)
        .setGameRule(GameRules.ADVANCE_WEATHER, false)
        .setGameRule(GameRules.FIRE_SPREAD_RADIUS_AROUND_PLAYER, 0)
        .setGameRule(GameRules.RANDOM_TICK_SPEED, 0)
        .setGameRule(GameRules.BLOCK_DROPS, false)
        .setGameRule(GameRules.BLOCK_EXPLOSION_DROP_DECAY, false)
        .setGameRule(GameRules.SPAWN_PATROLS, false)
        .setGameRule(GameRules.TNT_EXPLOSION_DROP_DECAY, false)
        .setGameRule(GameRules.SPAWN_WANDERING_TRADERS, false)
        .setGameRule(GameRules.GLOBAL_SOUND_EVENTS, false)
        .setGameRule(GameRules.REDUCED_DEBUG_INFO, true)
        .setGameRule(GameRules.RAIDS, true)
        .setGameRule(GameRules.SPECTATORS_GENERATE_CHUNKS, false)
        .setGameRule(GameRules.SPAWN_WARDENS, false)
        .setGameRule(GameRules.SPREAD_VINES, false)
        .setGameRule(GameRules.UNIVERSAL_ANGER, false)

    const val DUNGEON_WORLD_PREFIX = "dungeon-world"
    fun generateIdentifier() = IdentifierUtil.default("$DUNGEON_WORLD_PREFIX-${UUID.randomUUID()}")
}