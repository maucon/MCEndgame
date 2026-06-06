package de.fuballer.mcendgame.main.mixin.server_player_entity;

import de.fuballer.mcendgame.main.messaging.misc.GetRespawnCommand;
import de.maucon.mauconframework.command.CommandGateway;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayer.class)
public class ServerPlayerRespawnMixin {
    @Inject(
            method = "getRespawnConfig",
            at = @At("HEAD"),
            cancellable = true
    )
    void getRespawn(CallbackInfoReturnable<ServerPlayer.RespawnConfig> cir) {
        var player = (ServerPlayer) (Object) this;
        var command = new GetRespawnCommand(player);
        var cmd = CommandGateway.INSTANCE.apply(command);

        var respawn = cmd.getRespawn();
        if (respawn == null) return;
        cir.setReturnValue(respawn);
    }
}
