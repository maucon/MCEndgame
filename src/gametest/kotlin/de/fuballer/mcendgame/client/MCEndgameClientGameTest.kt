package de.fuballer.mcendgame.client

import net.fabricmc.fabric.api.client.gametest.v1.FabricClientGameTest
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext

@Suppress("UnstableApiUsage")
class MCEndgameClientGameTest : FabricClientGameTest {
    override fun runTest(context: ClientGameTestContext) {
        context.worldBuilder().create().use { singleplayer ->
            singleplayer.clientLevel.waitForChunksRender()
            context.takeScreenshot("example-mod-singleplayer-test")
        }
    }
}