package de.fuballer.mcendgame.main.component.analytics

import com.google.gson.Gson
import de.fuballer.mcendgame.main.MCEndgame
import de.fuballer.mcendgame.main.component.config.UserConfig
import de.fuballer.mcendgame.main.messaging.dungeon.DungeonPlayerDeathEvent
import de.fuballer.mcendgame.main.messaging.misc.PlayerAfterDimensionChangeEvent
import de.fuballer.mcendgame.main.util.extension.WorldExtension.isDungeonWorld
import de.fuballer.mcendgame.main.util.extension.mixin.WorldMixinExtension.getDungeonLevel
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.di.annotation.Logging
import de.maucon.mauconframework.event.EventSubscriber
import de.maucon.mauconframework.initializer.Initializer
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.server.level.ServerLevel
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

        val player = event.player

        sendAnalytics(
            eventType = EventType.DUNGEON_JOIN,
            payload = DungeonJoinPayload(
                gamemode = player.gameMode()?.name ?: AnalyticsUtil.UNKNOWN,
                dungeonLevel = event.newWorld.getDungeonLevel(),
                armor = AnalyticsUtil.getArmorItems(player),
                hotbar = AnalyticsUtil.getHotbarItems(player),
                offhand = AnalyticsUtil.getOffhandItems(player)
            )
        )
    }

    @EventSubscriber
    fun on(event: DungeonPlayerDeathEvent) {
        if (event.isClient) return
        val killer = event.killer ?: return

        val player = event.player
        val world = player.level() as? ServerLevel ?: return

        sendAnalytics(
            eventType = EventType.PLAYER_DUNGEON_DEATH,
            payload = DungeonPlayerDeathPayload(
                dungeonLevel = world.getDungeonLevel(),
                playerArmor = AnalyticsUtil.getArmorItems(player),
                playerMainhand = AnalyticsUtil.getMainhandItems(player),
                playerOffhand = AnalyticsUtil.getOffhandItems(player),
                playerEffects = AnalyticsUtil.getActiveEffects(player),
                killerEntity = BuiltInRegistries.ENTITY_TYPE.getKey(killer.type).toString(),
                killerArmor = AnalyticsUtil.getArmorItems(killer),
                killerMainhand = AnalyticsUtil.getMainhandItems(killer),
                killerOffhand = AnalyticsUtil.getOffhandItems(killer),
                killerEffects = AnalyticsUtil.getActiveEffects(killer),
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