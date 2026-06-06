package de.fuballer.mcendgame.main.mixin.server_player_entity;

import de.fuballer.mcendgame.main.messaging.misc.PlayerBeforeDimensionChangeEvent;
import de.maucon.mauconframework.event.EventGateway;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.portal.TeleportTransition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayer.class)
public class ServerPlayerTeleportCommandMixin {
    @Inject(
            method = "teleport(Lnet/minecraft/world/level/portal/TeleportTransition;)Lnet/minecraft/server/level/ServerPlayer;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;getLevelData()Lnet/minecraft/world/level/storage/LevelData;")
    )
    void teleportCrossDimension(TeleportTransition transition, CallbackInfoReturnable<ServerPlayer> cir) {
        var entity = (ServerPlayer) (Object) this;

        var event = new PlayerBeforeDimensionChangeEvent(entity, entity.level(), transition);
        EventGateway.INSTANCE.publish(event);
    }
}
