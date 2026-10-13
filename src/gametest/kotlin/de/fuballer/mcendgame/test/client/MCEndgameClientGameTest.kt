package de.fuballer.mcendgame.test.client

import de.fuballer.mcendgame.client.component.render.CustomRenderLayers
import de.fuballer.mcendgame.client.component.render.CustomRenderPipelines
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
        verifyCustomRenderPipelines()

        LOG.info("Creating singleplayer world")
        context.worldBuilder().create().use { singleplayer ->
            LOG.info("Waiting for chunks to render")
            singleplayer.connection.waitForChunksRender()
            LOG.info("Chunks rendered")

            val pos = singleplayer.server.computeOnServer<Vec3, RuntimeException> { server ->
                val player = server.playerList.players.first()
                player.position()
            }

            val entityTypes = BuiltInRegistries.ENTITY_TYPE
                .filter { BuiltInRegistries.ENTITY_TYPE.getKey(it) != Identifier.withDefaultNamespace("player") }
            LOG.info("Summoning {} entity types", entityTypes.size)

            for (entityType in entityTypes) {
                val id = BuiltInRegistries.ENTITY_TYPE.getKey(entityType)

                LOG.info("Spawning {}", id)
                singleplayer.server.runCommand(
                    "summon $id ${pos.x} ${pos.y} ${pos.z + 10}"
                )
            }
            context.waitTicks(5)

            LOG.info("Taking screenshot")
            context.takeScreenshot("all_entities")
            LOG.info("Screenshot taken")
        }
    }

    private fun verifyCustomRenderPipelines() {
        val pipelines = linkedMapOf(
            "link" to CustomRenderPipelines.LINK_PIPELINE,
            "ghostly" to CustomRenderPipelines.GHOSTLY_PIPELINE,
            "bound_abyss" to CustomRenderPipelines.BOUND_ABYSS_PIPELINE,
            "beastweaver_attack" to CustomRenderPipelines.BEASTWEAVER_ATTACK_PIPELINE,
        )

        for ((name, pipeline) in pipelines) {
            LOG.info("Custom render pipeline '{}' is registered and compiled: {}", name, pipeline)
        }

        // Also builds the RenderTypes layered on top of those pipelines.
        LOG.info("Custom render layer 'link' is registered: {}", CustomRenderLayers.LINK)
    }
}
