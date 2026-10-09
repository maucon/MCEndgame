package de.fuballer.mcendgame.main.runtime_worlds;

import net.minecraft.core.Holder;
import net.minecraft.network.protocol.game.ClientboundSetTimePacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Util;
import net.minecraft.world.clock.ClockNetworkState;
import net.minecraft.world.clock.PackedClockStates;
import net.minecraft.world.clock.ServerClockManager;
import net.minecraft.world.clock.WorldClock;
import org.jspecify.annotations.NonNull;

import java.util.Map;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public class RuntimeClockManager extends ServerClockManager {
    protected final BooleanSupplier advanceTime;
    protected MinecraftServer server;

    public RuntimeClockManager(PackedClockStates packedClockStates, BooleanSupplier advanceTime) {
        super(packedClockStates);
        this.advanceTime = advanceTime;
    }

    @Override
    public void init(@NonNull MinecraftServer server) {
        super.init(server);
        this.server = server;
    }

    @Override
    public void tick() {
        if (this.advanceTime.getAsBoolean()) {
            ((ServerClockManagerExtension) this).mcendgame$getClocks().values().forEach(ServerClockManager.ServerClockInstance::tick);
            this.setDirty();
        }
    }

    @Override
    protected void modifyClock(final Holder<WorldClock> clock, final Consumer<? super ServerClockManager.ServerClockInstance> action) {
        ServerClockManager.ServerClockInstance instance = this.getInstance(clock);
        action.accept(instance);
        Map<Holder<WorldClock>, ClockNetworkState> updates = Map.of(clock, this.packNetworkState(instance, this.server));
        this.setDirty();

        var packet = new ClientboundSetTimePacket(this.getGameTime(), updates);

        for (ServerLevel level : this.server.getAllLevels()) {
            if (level.clockManager() == this) {
                for (var player : level.players()) {
                    player.connection.send(packet);
                }

                level.environmentAttributes().invalidateTickCache();
            }
        }
    }

    @Override
    public @NonNull ClientboundSetTimePacket createFullSyncPacket() {
        return new ClientboundSetTimePacket(this.getGameTime(), Util.mapValues(((ServerClockManagerExtension) this).mcendgame$getClocks(), (clock) -> this.packNetworkState(clock, this.server)));
    }

    protected ClockNetworkState packNetworkState(ServerClockManager.ServerClockInstance instance, final MinecraftServer server) {
        var packed = instance.packNetworkState(server);
        if (this.advanceTime.getAsBoolean()) return packed;
        return new ClockNetworkState(packed.totalTicks(), packed.partialTick(), 0.0F);
    }

    public void tickFromLevel(RuntimeLevel level) {
        this.tick();
    }
}
