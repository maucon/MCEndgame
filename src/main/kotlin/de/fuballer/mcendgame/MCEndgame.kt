package de.fuballer.mcendgame

import de.fuballer.mcendgame.component.artifact.ArtifactListener
import de.fuballer.mcendgame.component.artifact.ArtifactService
import de.fuballer.mcendgame.component.artifact.command.ArtifactCommand
import de.fuballer.mcendgame.component.artifact.command.ArtifactTabCompleter
import de.fuballer.mcendgame.component.artifact.command.give_artifact.GiveArtifactCommand
import de.fuballer.mcendgame.component.artifact.command.give_artifact.GiveArtifactTabCompleter
import de.fuballer.mcendgame.component.artifact.db.ArtifactRepository
import de.fuballer.mcendgame.component.artifact.effects.ArtifactEffectsListener
import de.fuballer.mcendgame.component.artifact.effects.ArtifactEffectsService
import de.fuballer.mcendgame.component.build_calculation.BuildCalculationCommand
import de.fuballer.mcendgame.component.build_calculation.BuildCalculationTabCompleter
import de.fuballer.mcendgame.component.corruption.CorruptionListener
import de.fuballer.mcendgame.component.corruption.CorruptionService
import de.fuballer.mcendgame.component.dungeon.antibug.DungeonAntiBugListener
import de.fuballer.mcendgame.component.dungeon.antibug.DungeonAntiBugService
import de.fuballer.mcendgame.component.dungeon.armor_durability.ArmorDurabilityListener
import de.fuballer.mcendgame.component.dungeon.armor_durability.ArmorDurabilityService
import de.fuballer.mcendgame.component.dungeon.boss.DungeonBossListener
import de.fuballer.mcendgame.component.dungeon.boss.DungeonBossService
import de.fuballer.mcendgame.component.dungeon.boss.db.DungeonBossRepository
import de.fuballer.mcendgame.component.dungeon.creeper_explode.CreeperExplodeListener
import de.fuballer.mcendgame.component.dungeon.creeper_explode.CreeperExplodeService
import de.fuballer.mcendgame.component.dungeon.enemy.EnemyGenerationListener
import de.fuballer.mcendgame.component.dungeon.enemy.EnemyGenerationService
import de.fuballer.mcendgame.component.dungeon.enemy.custom_entity.MinionRepository
import de.fuballer.mcendgame.component.dungeon.enemy.custom_entity.necromancer.NecromancerListener
import de.fuballer.mcendgame.component.dungeon.enemy.custom_entity.necromancer.NecromancerService
import de.fuballer.mcendgame.component.dungeon.enemy.custom_entity.summoner.SummonerService
import de.fuballer.mcendgame.component.dungeon.enemy.loot.EnemyLootListener
import de.fuballer.mcendgame.component.dungeon.enemy.loot.EnemyLootService
import de.fuballer.mcendgame.component.dungeon.enemy.targeting.EnemyTargetingListener
import de.fuballer.mcendgame.component.dungeon.enemy.targeting.EnemyTargetingService
import de.fuballer.mcendgame.component.dungeon.generation.DungeonGenerationService
import de.fuballer.mcendgame.component.dungeon.killingstreak.KillStreakListener
import de.fuballer.mcendgame.component.dungeon.killingstreak.KillStreakService
import de.fuballer.mcendgame.component.dungeon.killingstreak.db.KillStreakRepository
import de.fuballer.mcendgame.component.dungeon.leave.DungeonLeaveListener
import de.fuballer.mcendgame.component.dungeon.leave.DungeonLeaveService
import de.fuballer.mcendgame.component.dungeon.leave.db.DungeonLeaveRepository
import de.fuballer.mcendgame.component.dungeon.looting.LootingListener
import de.fuballer.mcendgame.component.dungeon.looting.LootingService
import de.fuballer.mcendgame.component.dungeon.progress.PlayerDungeonProgressListener
import de.fuballer.mcendgame.component.dungeon.progress.PlayerDungeonProgressService
import de.fuballer.mcendgame.component.dungeon.progress.command.DungeonProgressCommand
import de.fuballer.mcendgame.component.dungeon.progress.command.DungeonProgressTabCompleter
import de.fuballer.mcendgame.component.dungeon.progress.db.PlayerDungeonProgressRepository
import de.fuballer.mcendgame.component.dungeon.world.WorldGenerationListener
import de.fuballer.mcendgame.component.dungeon.world.WorldManageService
import de.fuballer.mcendgame.component.dungeon.world.db.WorldManageRepository
import de.fuballer.mcendgame.component.filter.FilterListener
import de.fuballer.mcendgame.component.filter.FilterService
import de.fuballer.mcendgame.component.filter.command.FilterCommand
import de.fuballer.mcendgame.component.filter.command.FilterTabCompleter
import de.fuballer.mcendgame.component.filter.db.FilterRepository
import de.fuballer.mcendgame.component.killer.KillerListener
import de.fuballer.mcendgame.component.killer.KillerService
import de.fuballer.mcendgame.component.killer.command.KillerCommand
import de.fuballer.mcendgame.component.killer.command.KillerTabCompleter
import de.fuballer.mcendgame.component.killer.db.KillerRepository
import de.fuballer.mcendgame.component.mapdevice.MapDeviceListener
import de.fuballer.mcendgame.component.mapdevice.MapDeviceService
import de.fuballer.mcendgame.component.mapdevice.db.MapDeviceRepository
import de.fuballer.mcendgame.component.recipe.RecipeListener
import de.fuballer.mcendgame.component.recipe.RecipeService
import de.fuballer.mcendgame.component.recipe.db.RecipeRepository
import de.fuballer.mcendgame.component.remaining.RemainingListener
import de.fuballer.mcendgame.component.remaining.RemainingService
import de.fuballer.mcendgame.component.remaining.command.RemainingCommand
import de.fuballer.mcendgame.component.remaining.command.RemainingTabCompleter
import de.fuballer.mcendgame.component.remaining.db.RemainingRepository
import de.fuballer.mcendgame.component.statistics.StatisticsListener
import de.fuballer.mcendgame.component.statistics.StatisticsService
import de.fuballer.mcendgame.component.statistics.command.StatisticsCommand
import de.fuballer.mcendgame.component.statistics.command.StatisticsTabCompleter
import de.fuballer.mcendgame.component.statistics.db.StatisticsRepository
import de.fuballer.mcendgame.component.statitem.StatItemListener
import de.fuballer.mcendgame.component.statitem.StatItemService
import de.fuballer.mcendgame.component.statitem.command.StatItemCommand
import de.fuballer.mcendgame.component.statitem.command.StatItemTabCompleter
import de.fuballer.mcendgame.framework.DependencyInjector
import de.fuballer.mcendgame.framework.stereotype.*
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class MCEndgame : JavaPlugin() {
    private val repositories: List<Repository<*, *>>
    private val services: List<Service>
    private val listener: List<EventListener>
    private val commandHandler: List<CommandHandler>
    private val tabCompleter: List<CommandTabCompleter>

    private val injectableClassObjects = mutableSetOf<Class<*>>(
        ArtifactRepository::class.java,
        RecipeRepository::class.java,
        StatisticsRepository::class.java,
        FilterRepository::class.java,
        PlayerDungeonProgressRepository::class.java,
        KillerRepository::class.java,
        RemainingRepository::class.java,
        DungeonBossRepository::class.java,
        KillStreakRepository::class.java,
        DungeonLeaveRepository::class.java,
        WorldManageRepository::class.java,
        MapDeviceRepository::class.java,
        MinionRepository::class.java,

        StatItemService::class.java,
        RemainingService::class.java,
        WorldManageService::class.java,
        PlayerDungeonProgressService::class.java,
        StatisticsService::class.java,
        KillStreakService::class.java,
        RecipeService::class.java,
        ArtifactService::class.java,
        ArtifactEffectsService::class.java,
        DungeonLeaveService::class.java,
        DungeonAntiBugService::class.java,
        EnemyTargetingService::class.java,
        ArmorDurabilityService::class.java,
        DungeonBossService::class.java,
        DungeonGenerationService::class.java,
        EnemyGenerationService::class.java,
        MapDeviceService::class.java,
        CorruptionService::class.java,
        KillerService::class.java,
        FilterService::class.java,
        LootingService::class.java,
        CreeperExplodeService::class.java,
        SummonerService::class.java,
        NecromancerService::class.java,
        EnemyLootService::class.java,

        MapDeviceListener::class.java,
        RecipeListener::class.java,
        CorruptionListener::class.java,
        StatItemListener::class.java,
        DungeonBossListener::class.java,
        EnemyGenerationListener::class.java,
        DungeonLeaveListener::class.java,
        DungeonAntiBugListener::class.java,
        EnemyTargetingListener::class.java,
        ArmorDurabilityListener::class.java,
        KillerListener::class.java,
        FilterListener::class.java,
        LootingListener::class.java,
        PlayerDungeonProgressListener::class.java,
        KillStreakListener::class.java,
        RemainingListener::class.java,
        CreeperExplodeListener::class.java,
        ArtifactListener::class.java,
        ArtifactEffectsListener::class.java,
        StatisticsListener::class.java,
        WorldGenerationListener::class.java,
        NecromancerListener::class.java,
        EnemyLootListener::class.java,

        BuildCalculationCommand::class.java,
        KillerCommand::class.java,
        RemainingCommand::class.java,
        FilterCommand::class.java,
        ArtifactCommand::class.java,
        GiveArtifactCommand::class.java,
        DungeonProgressCommand::class.java,
        StatItemCommand::class.java,
        StatisticsCommand::class.java,

        BuildCalculationTabCompleter::class.java,
        ArtifactTabCompleter::class.java,
        GiveArtifactTabCompleter::class.java,
        DungeonProgressTabCompleter::class.java,
        FilterTabCompleter::class.java,
        KillerTabCompleter::class.java,
        RemainingTabCompleter::class.java,
        StatisticsTabCompleter::class.java,
        StatItemTabCompleter::class.java
    )

    companion object {
        var DATA_FOLDER: File = File("")
        lateinit var WORLD_CONTAINER: File
        lateinit var PLUGIN: Plugin
    }

    init {
        val injectedObjects = DependencyInjector.instantiateClasses(injectableClassObjects)

        repositories = injectedObjects.filterIsInstance<Repository<*, *>>()
        services = injectedObjects.filterIsInstance<Service>()
        listener = injectedObjects.filterIsInstance<EventListener>()
        commandHandler = injectedObjects.filterIsInstance<CommandHandler>()
        tabCompleter = injectedObjects.filterIsInstance<CommandTabCompleter>()
    }

    override fun onEnable() {
        Bukkit.getLogger().info("Enabling MC-Endgame")

        DATA_FOLDER = this.dataFolder
        WORLD_CONTAINER = server.worldContainer
        PLUGIN = Bukkit.getPluginManager().getPlugin("MC-Endgame")!!

        listener.forEach { Bukkit.getPluginManager().registerEvents(it, this) }

        repositories.forEach { it.load() }
        services.forEach { it.initialize(this) }

        commandHandler.forEach { getCommand(it.getCommand())!!.setExecutor(it) }
        tabCompleter.forEach { getCommand(it.getCommand())!!.tabCompleter = it }

        Bukkit.getLogger().info("Enabled MC-Endgame")
    }

    override fun onDisable() {
        Bukkit.getLogger().info("Disabling MC-Endgame")

        services.forEach { it.terminate() }
        repositories.forEach { it.flush() }

        Bukkit.getLogger().info("Disabled MC-Endgame")
    }
}
