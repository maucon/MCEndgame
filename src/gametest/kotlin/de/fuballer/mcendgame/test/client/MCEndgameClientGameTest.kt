package de.fuballer.mcendgame.test.client

import net.fabricmc.fabric.api.client.gametest.v1.FabricClientGameTest
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import net.minecraft.world.phys.Vec3
import org.slf4j.LoggerFactory

@Suppress("UnstableApiUsage")
class MCEndgameClientGameTest : FabricClientGameTest {
    companion object {
        private val LOG = LoggerFactory.getLogger(this::class.java.simpleName)
    }

    override fun runTest(context: ClientGameTestContext) {
        context.worldBuilder().create().use { singleplayer ->
            singleplayer.clientLevel.waitForChunksRender()

            val pos = singleplayer.server.computeOnServer<Vec3, RuntimeException> { server ->
                val player = server.playerList.players.first()
                player.position()
            }

            for (entityType in BuiltInRegistries.ENTITY_TYPE) {
                val id = BuiltInRegistries.ENTITY_TYPE.getKey(entityType)
                if (id == Identifier.withDefaultNamespace("player")) continue

                LOG.info("Spawning {}", id)
                singleplayer.server.runCommand(
                    "summon $id ${pos.x} ${pos.y} ${pos.z + 10}"
                )
            }
            context.waitTicks(5)

            context.takeScreenshot("all_entities")
        }
    }
}