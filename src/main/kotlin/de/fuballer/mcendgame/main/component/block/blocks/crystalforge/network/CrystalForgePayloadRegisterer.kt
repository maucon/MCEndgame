package de.fuballer.mcendgame.main.component.block.blocks.crystalforge.network

import de.fuballer.mcendgame.main.component.block.blocks.crystalforge.CrystalForgeScreenHandler
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.initializer.Initializer
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking

@Injectable
object CrystalForgePayloadRegisterer {
    @Initializer
    fun register() {
        PayloadTypeRegistry.playC2S().register(CrystalForgePayload.ID, CrystalForgePayload.CODEC)

        ServerPlayNetworking.registerGlobalReceiver(CrystalForgePayload.ID) { _, context ->
            val player = context.player()
            val screenHandler = player.containerMenu as? CrystalForgeScreenHandler ?: return@registerGlobalReceiver
            screenHandler.forge()
        }
    }
}