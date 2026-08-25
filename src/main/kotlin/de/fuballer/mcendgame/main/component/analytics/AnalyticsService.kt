package de.fuballer.mcendgame.main.component.analytics

import com.google.gson.Gson
import de.fuballer.mcendgame.main.MCEndgame
import de.fuballer.mcendgame.main.component.config.UserConfig
import de.fuballer.mcendgame.main.component.entity.custom.entities.scarred_one.ScarredOneDespawnEvent
import de.fuballer.mcendgame.main.messaging.dungeon.DungeonBossDeathEvent
import de.fuballer.mcendgame.main.messaging.dungeon.DungeonEnemyDeathEvent
import de.fuballer.mcendgame.main.messaging.dungeon.DungeonPlayerDeathEvent
import de.fuballer.mcendgame.main.messaging.misc.PlayerAfterDimensionChangeEvent
import de.fuballer.mcendgame.main.messaging.totem_encounter.TotemEncounterActivatedEvent
import de.fuballer.mcendgame.main.util.extension.WorldExtension.isDungeonWorld
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.isElite
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.isLootGoblin
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.di.annotation.Logging
import de.maucon.mauconframework.event.EventSubscriber
import de.maucon.mauconframework.initializer.Initializer
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.LivingEntity
import org.slf4j.Logger
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration
import java.util.*

private val GSON = Gson()
private val HTTP = HttpClient.newHttpClient()
private const val ENDPOINT = "https://mcendgame-analytics.maucon.workers.dev/"

@Injectable
class AnalyticsService(
    @Logging private val log: Logger,
    private val userConfig: UserConfig,
) {
    private lateinit var modVersion: String
    private val sessionId = UUID.randomUUID().toString()

    @Initializer
    fun init(fabricLoader: FabricLoader) {
        modVersion = fabricLoader.getModContainer(MCEndgame.MOD_ID)
            .map { it.metadata.version.friendlyString }
            .orElse(AnalyticsUtil.UNKNOWN)!!

        log.info(
            "Analytics for MCEndgame are ${if (userConfig.sendAnalytics) "enabled" else "disabled"}. " +
                    "You can change this in '${UserConfig.FILE}'." +
                    " Find out more at https://github.com/maucon/MCEndgame/wiki/Analytics"
        )
    }

    @EventSubscriber
    fun on(event: PlayerAfterDimensionChangeEvent) {
        if (event.oldWorld.isDungeonWorld() || !event.newWorld.isDungeonWorld()) return

        sendAnalytics(
            eventType = EventType.DUNGEON_JOIN,
            payload = DungeonJoinPayload(
                dungeon = AnalyticsUtil.getDungeonData(event.newWorld),
                player = AnalyticsUtil.getPlayerLoadoutData(event.player),
            )
        )
    }

    @EventSubscriber
    fun on(event: DungeonPlayerDeathEvent) {
        if (event.isClient) return

        val player = event.player
        val level = player.level() as? ServerLevel ?: return

        sendAnalytics(
            eventType = EventType.PLAYER_DUNGEON_DEATH,
            payload = DungeonPlayerDeathPayload(
                dungeon = AnalyticsUtil.getDungeonData(level),
                player = AnalyticsUtil.getPlayerLoadoutData(event.player),
                killer = event.killer?.let { AnalyticsUtil.getEntityLoadoutData(it) },
            )
        )
    }

    @EventSubscriber
    fun on(event: DungeonBossDeathEvent) {
        if (event.isClient) return

        val level = event.world as? ServerLevel ?: return

        sendAnalytics(
            eventType = EventType.DUNGEON_BOSS_KILLED,
            payload = DungeonBossKilledPayload(
                dungeon = AnalyticsUtil.getDungeonData(level),
                boss = BuiltInRegistries.ENTITY_TYPE.getKey(event.bossEntity.type).toString(),
                players = level.players().map { AnalyticsUtil.getPlayerLoadoutData(it) },
            )
        )
    }

    @EventSubscriber
    fun on(event: DungeonEnemyDeathEvent) {
        if (event.isClient) return

        val level = event.world as? ServerLevel ?: return

        val entity = event.enemyEntity
        if (entity.isLootGoblin()) sendLootGoblinKilledAnalytics(level, entity)
        if (entity.isElite()) sendEliteKilledAnalytics(level, entity)
    }

    private fun sendLootGoblinKilledAnalytics(level: ServerLevel, goblin: LivingEntity) {
        sendAnalytics(
            eventType = EventType.LOOT_GOBLIN_KILLED,
            payload = SpecialEnemyKilledPayload(
                dungeon = AnalyticsUtil.getDungeonData(level),
                enemyLoadout = AnalyticsUtil.getEntityLoadoutData(goblin)
            )
        )
    }

    private fun sendEliteKilledAnalytics(level: ServerLevel, goblin: LivingEntity) {
        sendAnalytics(
            eventType = EventType.ELITE_KILLED,
            payload = SpecialEnemyKilledPayload(
                dungeon = AnalyticsUtil.getDungeonData(level),
                enemyLoadout = AnalyticsUtil.getEntityLoadoutData(goblin)
            )
        )
    }

    @EventSubscriber
    fun on(event: TotemEncounterActivatedEvent) {
        val level = event.player.level() as? ServerLevel ?: return
        sendAnalytics(
            eventType = EventType.TOTEM_ENCOUNTER_STARTED,
            payload = AnalyticsUtil.getDungeonData(level),
        )
    }

    @EventSubscriber
    fun on(event: ScarredOneDespawnEvent) {
        val level = event.player.level()

        val scarredOne = event.entity
        val positive = scarredOne.positiveEffects
        val negative = scarredOne.negativeEffects

        sendAnalytics(
            eventType = EventType.SCARRED_ONE_SELECTED,
            payload = ScarredOneSelectedPayload(
                dungeon = AnalyticsUtil.getDungeonData(level),
                scarredOne = ScarredOnePayload(
                    accepted = event.accepted,
                    positiveEffects = AnalyticsUtil.getScarredOneEffectsJson(positive),
                    negativeEffects = AnalyticsUtil.getScarredOneEffectsJson(negative),
                ),
            )
        )
    }

    private fun sendAnalytics(eventType: EventType, payload: Any) {
        if (!userConfig.sendAnalytics) return

        val httpPayload = EventPayload(eventType.type, modVersion, payload)

        val request = HttpRequest.newBuilder()
            .uri(URI.create("$ENDPOINT?session=$sessionId"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(GSON.toJson(httpPayload)))
            .timeout(Duration.ofSeconds(5))
            .build()

        HTTP.sendAsync(request, HttpResponse.BodyHandlers.discarding())
            .exceptionally { null }
    }
}