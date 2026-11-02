package de.fuballer.mcendgame.main.mixin.runtime_worlds;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import de.fuballer.mcendgame.main.accessor.ServerLevelAccessor;
import de.fuballer.mcendgame.main.runtime_worlds.RuntimeLevel;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.clock.ServerClockManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.BooleanSupplier;

@Mixin(value = ServerLevel.class)
public abstract class ServerLevelMixin implements ServerLevelAccessor {
    @Unique
    private static final int TICK_TIMEOUT = 20 * 15;

    @Unique
    private boolean mcendgame$tickWhenEmpty = true;
    @Unique
    private int mcendgame$tickTimeout;

    @Shadow
    public abstract List<ServerPlayer> players();

    @Shadow
    public abstract ServerChunkCache getChunkSource();

    @Override
    public void mcendgame$setTickWhenEmpty(boolean tickWhenEmpty) {
        this.mcendgame$tickWhenEmpty = tickWhenEmpty;
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void tick(BooleanSupplier haveTime, CallbackInfo ci) {
        boolean shouldTick = this.mcendgame$tickWhenEmpty || this.isLevelNotEmpty();
        if (shouldTick) {
            this.mcendgame$tickTimeout = TICK_TIMEOUT;
        } else if (this.mcendgame$tickTimeout > 0) {
            this.mcendgame$tickTimeout--;
        } else {
            ci.cancel();
        }
    }

    @Override
    public boolean mcendgame$shouldTick() {
        boolean shouldTick = this.mcendgame$tickWhenEmpty || this.isLevelNotEmpty();
        return shouldTick || this.mcendgame$tickTimeout > 0;
    }

    @Unique
    private boolean isLevelNotEmpty() {
        return !this.players().isEmpty() || this.getChunkSource().getLoadedChunksCount() > 0;
    }

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;clockManager()Lnet/minecraft/world/clock/ServerClockManager;"))
    private ServerClockManager onTickMoveToTimeMarkerWakeUp(MinecraftServer instance, Operation<ServerClockManager> original) {
        if ((Object) this instanceof RuntimeLevel level) {
            return level.clockManager();
        } else {
            return original.call(instance);
        }
    }
}
