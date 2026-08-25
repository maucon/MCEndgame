package de.fuballer.mcendgame.main.mixin.runtime_worlds;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import de.fuballer.mcendgame.main.runtime_worlds.util.SafeIterator;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.clock.ServerClockManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gamerules.GameRules;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
    @Shadow
    @Deprecated
    public abstract GameRules getGlobalGameRules();

    @Shadow
    @Final
    private Map<ResourceKey<Level>, ServerLevel> levels;

    @Shadow
    @Final
    private ServerClockManager clockManager;

    @Redirect(method = "tickChildren", at = @At(value = "INVOKE", target = "Ljava/lang/Iterable;iterator()Ljava/util/Iterator;", ordinal = 0), require = 0)
    private Iterator<ServerLevel> mcendgame$copyBeforeTicking(Iterable<ServerLevel> instance) {
        return new SafeIterator<>((Collection<ServerLevel>) instance);
    }

    @WrapOperation(method = {"onGameRuleChanged"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/server/players/PlayerList;broadcastAll(Lnet/minecraft/network/protocol/Packet;)V"))
    private void forceGameRunTimeSynchronization(PlayerList instance, Packet<?> packet, Operation<Void> original) {
        for (ServerLevel level : this.levels.values()) {
            if (level.clockManager() == this.clockManager) {
                instance.broadcastAll(packet, level.dimension());
            }
        }
    }
}
