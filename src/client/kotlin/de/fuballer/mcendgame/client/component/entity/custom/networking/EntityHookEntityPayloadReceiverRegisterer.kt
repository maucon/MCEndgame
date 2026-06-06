package de.fuballer.mcendgame.client.component.entity.custom.networking

import de.fuballer.mcendgame.main.component.entity.custom.interfaces.HookAttackMob
import de.fuballer.mcendgame.main.component.entity.custom.networking.EntityHookEntityPayload
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.initializer.Initializer
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.client.Minecraft

@Injectable
class EntityHookEntityPayloadReceiverRegisterer {
    @Initializer
    fun register() {
        ClientPlayNetworking.registerGlobalReceiver(EntityHookEntityPayload.ID) { payload, _ ->
            val world = Minecraft.getInstance().level ?: return@registerGlobalReceiver
            val hooker = world.getEntity(payload.hookerId) ?: return@registerGlobalReceiver
            if (hooker !is HookAttackMob) return@registerGlobalReceiver

            if (payload.remove) {
                hooker.removeHookedEntity(payload.hookedId)
            } else {
                hooker.addHookedEntity(payload.hookedId)
            }
        }
    }
}