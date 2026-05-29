package de.fuballer.mcendgame.main.mixin.server_player_entity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ServerPlayerEntity.class)
public interface ServerPlayerEntityAccessor {
    @Invoker("copyInventoryAndExperienceFrom")
    void mcendgame$invokeCopyInventoryAndExperienceFrom(PlayerEntity other);
}
