package de.fuballer.mcendgame.test.client

import net.fabricmc.fabric.api.client.gametest.v1.FabricClientGameTest
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext
import net.minecraft.world.phys.Vec3

@Suppress("UnstableApiUsage")
class MCEndgameClientGameTest : FabricClientGameTest {
    override fun runTest(context: ClientGameTestContext) {
        // TODO disable mcendgame telemetry
        context.worldBuilder().create().use { singleplayer ->
            singleplayer.clientLevel.waitForChunksRender()
            val pos = singleplayer.server.computeOnServer<Vec3, RuntimeException> { server ->
                val player = server.playerList.players.first()
                player.position()
            }

            singleplayer.server.runCommand(
                "summon mcendgame:arachne ${pos.x} ${pos.y} ${pos.z + 10}"
            )
            context.waitTicks(5)

            context.takeScreenshot("arachne")
        }
    }
}