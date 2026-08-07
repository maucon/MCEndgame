package de.fuballer.mcendgame.test.client

import net.fabricmc.fabric.api.client.gametest.v1.FabricClientGameTest
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.EntitySpawnReason
import org.slf4j.LoggerFactory

@Suppress("UnstableApiUsage")
class MCEndgameClientGameTest : FabricClientGameTest {
    companion object {
        private val LOGGER = LoggerFactory.getLogger(this::class.java.simpleName)
    }

    override fun runTest(context: ClientGameTestContext) {
        context.worldBuilder().create().use { singleplayer ->
            singleplayer.clientLevel.waitForChunksRender()

            val level = singleplayer.server.computeOnServer<ServerLevel, RuntimeException> { server ->
                server.overworld()
            }

            for (entityType in BuiltInRegistries.ENTITY_TYPE) {
                val id = BuiltInRegistries.ENTITY_TYPE.getKey(entityType)
                if (id == Identifier.withDefaultNamespace("player")) continue

                LOGGER.info("Spawning {}", id)
                entityType.create(level, EntitySpawnReason.MOB_SUMMONED)
            }
            context.waitTicks(5)

            context.takeScreenshot("arachne")
        }
    }
}