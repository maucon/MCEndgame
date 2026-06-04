package de.fuballer.mcendgame.main.component.stats

import de.maucon.mauconframework.di.annotation.Injectable
import net.minecraft.stats.StatFormatter

@Injectable
object CustomStats {
    // Dungeons
    val DUNGEONS_OPENED = CustomStatsRegistry.register("dungeons_opened", StatFormatter.DEFAULT)
    val DUNGEONS_JOINED = CustomStatsRegistry.register("dungeons_joined", StatFormatter.DEFAULT)
    val DUNGEONS_COMPLETED = CustomStatsRegistry.register("dungeons_completed", StatFormatter.DEFAULT)
    val DUNGEON_DEATHS = CustomStatsRegistry.register("dungeon_deaths", StatFormatter.DEFAULT)
    val HIGHEST_COMPLETED_LEVEL = CustomStatsRegistry.register("highest_completed_level", StatFormatter.DEFAULT)

    // Kills
    val ENEMIES_KILLED = CustomStatsRegistry.register("enemies_killed", StatFormatter.DEFAULT)
    val BOSSES_KILLED = CustomStatsRegistry.register("bosses_killed", StatFormatter.DEFAULT)
    val LOOT_GOBLINS_KILLED = CustomStatsRegistry.register("loot_goblins_killed", StatFormatter.DEFAULT)
    val ELITES_KILLED = CustomStatsRegistry.register("elites_killed", StatFormatter.DEFAULT)

    // Combat
    val DUNGEON_DAMAGE_DEALT = CustomStatsRegistry.register("dungeon_damage_dealt", StatFormatter.DIVIDE_BY_TEN)
    val DUNGEON_DAMAGE_TAKEN = CustomStatsRegistry.register("dungeon_damage_taken", StatFormatter.DIVIDE_BY_TEN)

    // Crystal Forge
    val CRYSTAL_FORGE_USED = CustomStatsRegistry.register("crystal_forge_used", StatFormatter.DEFAULT)
    val CALIBRATION_CRYSTAL_USED = CustomStatsRegistry.register("calibration_crystal_used", StatFormatter.DEFAULT)
    val SACRIFICE_CRYSTAL_USED = CustomStatsRegistry.register("sacrifice_crystal_used", StatFormatter.DEFAULT)
    val PERMUTATION_CRYSTAL_USED = CustomStatsRegistry.register("permutation_crystal_used", StatFormatter.DEFAULT)
    val REFORGE_CRYSTAL_USED = CustomStatsRegistry.register("reforge_crystal_used", StatFormatter.DEFAULT)
    val CORRUPTION_CRYSTAL_USED = CustomStatsRegistry.register("corruption_crystal_used", StatFormatter.DEFAULT)

    // Scarred One
    val SCARRED_ONE_ACCEPTED = CustomStatsRegistry.register("scarred_one_accepted", StatFormatter.DEFAULT)
    val SCARRED_ONE_DECLINED = CustomStatsRegistry.register("scarred_one_declined", StatFormatter.DEFAULT)

    // Misc
    val PORTALS_USED = CustomStatsRegistry.register("portals_used", StatFormatter.DEFAULT)
    val ASPECTS_USED = CustomStatsRegistry.register("aspects_used", StatFormatter.DEFAULT)
    val TOTEM_ENCOUNTERS_ACTIVATED = CustomStatsRegistry.register("totem_encounters_activated", StatFormatter.DEFAULT)
}