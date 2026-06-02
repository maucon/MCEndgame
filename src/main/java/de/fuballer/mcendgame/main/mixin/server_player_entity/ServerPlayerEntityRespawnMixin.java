package de.fuballer.mcendgame.main.mixin.server_player_entity;

import de.fuballer.mcendgame.main.messaging.misc.GetRespawnCommand;
import de.maucon.mauconframework.command.CommandGateway;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityRespawnMixin {
    @Inject(
            method = "getRespawn",
            at = @At("HEAD"),
            cancellable = true
    )
    void getRespawn(CallbackInfoReturnable<ServerPlayerEntity.Respawn> cir) {
        var player = (ServerPlayerEntity) (Object) this;
        var command = new GetRespawnCommand(player);
        var cmd = CommandGateway.INSTANCE.apply(command);

        var respawn = cmd.getRespawn();
        if (respawn == null) return;
        cir.setReturnValue(respawn);
    }
}
