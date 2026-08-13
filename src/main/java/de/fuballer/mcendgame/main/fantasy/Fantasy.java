package de.fuballer.mcendgame.main.fantasy;

import com.google.common.base.Preconditions;
import de.fuballer.mcendgame.main.mixin.fantasy.MinecraftServerAccess;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class Fantasy {
    public static final Logger LOGGER = LogManager.getLogger(Fantasy.class);

    private static Fantasy instance;

    private final MinecraftServer server;
    private final MinecraftServerAccess serverAccess;

    private final RuntimeLevelManager levelManager;

    private final Set<ServerLevel> deletionQueue = new ReferenceOpenHashSet<>();
    private final Set<ServerLevel> unloadingQueue = new ReferenceOpenHashSet<>();

    static {
        ServerTickEvents.START_SERVER_TICK.register(server -> {
            Fantasy fantasy = get(server);
            fantasy.tick();
        });

        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            Fantasy fantasy = get(server);
            fantasy.onServerStopping();
        });
    }

    private Fantasy(MinecraftServer server) {
        this.server = server;
        this.serverAccess = (MinecraftServerAccess) server;

        this.levelManager = new RuntimeLevelManager(server);
    }

    /**
     * Gets the {@link Fantasy} instance for the given server instance.
     *
     * @param server the server to work with
     * @return the {@link Fantasy} instance to work with runtime dimensions
     */
    public static Fantasy get(MinecraftServer server) {
        Preconditions.checkState(server.isSameThread(), "cannot create levels from off-thread!");

        if (instance == null || instance.server != server) {
            instance = new Fantasy(server);
        }

        return instance;
    }

    private void tick() {
        Set<ServerLevel> deletionQueue = this.deletionQueue;
        if (!deletionQueue.isEmpty()) {
            deletionQueue.removeIf(this::tickDeleteLevel);
        }

        Set<ServerLevel> unloadingQueue = this.unloadingQueue;
        if (!unloadingQueue.isEmpty()) {
            unloadingQueue.removeIf(this::tickUnloadLevel);
        }
    }

    public RuntimeLevelHandle openTemporaryLevel(Identifier key, RuntimeLevelConfig config) {
        RuntimeLevel level = this.addTemporaryLevel(key, config);
        return new RuntimeLevelHandle(level);
    }

    private RuntimeLevel addTemporaryLevel(Identifier key, RuntimeLevelConfig config) {
        ResourceKey<Level> levelKey = ResourceKey.create(Registries.DIMENSION, key);

        try {
            LevelStorageSource.LevelStorageAccess session = this.serverAccess.getStorageSource();
            FileUtils.forceDeleteOnExit(session.getDimensionPath(levelKey).toFile());
        } catch (IOException ignored) {
        }

        return this.levelManager.add(levelKey, config);
    }

    public boolean tickDeleteLevel(ServerLevel level) {
        this.kickPlayers(level);
        this.levelManager.delete(level);
        return true;
    }

    public boolean tickUnloadLevel(ServerLevel level) {
        if (this.isLevelActive(level) && !level.getChunkSource().chunkMap.hasWork()) {
            this.levelManager.unload(level);
            return true;
        } else {
            this.kickPlayers(level);
            return false;
        }
    }

    private void kickPlayers(ServerLevel level) {
        if (level.players().isEmpty()) {
            return;
        }

        ServerLevel spawnLevel = this.server.findRespawnDimension();
        LevelData.RespawnData spawnPoint = this.server.getRespawnData();

        List<ServerPlayer> players = new ArrayList<>(level.players());

        for (ServerPlayer player : players) {
            Vec3 pos = Vec3.atBottomCenterOf(player.adjustSpawnLocation(spawnLevel, spawnPoint.pos()));
            TeleportTransition target = new TeleportTransition(spawnLevel, pos, Vec3.ZERO, spawnPoint.yaw(), spawnPoint.pitch(), TeleportTransition.DO_NOTHING);

            player.teleport(target);
        }
    }

    private boolean isLevelActive(ServerLevel level) {
        return level.players().isEmpty() && level.getChunkSource().getLoadedChunksCount() <= 0;
    }

    private void onServerStopping() {
        List<RuntimeLevel> temporaryLevels = this.collectTemporaryLevels();
        for (RuntimeLevel temporary : temporaryLevels) {
            this.kickPlayers(temporary);
            this.levelManager.delete(temporary);
        }
    }

    private List<RuntimeLevel> collectTemporaryLevels() {
        List<RuntimeLevel> temporaryLevels = new ArrayList<>();
        for (ServerLevel level : this.server.getAllLevels()) {
            if (level instanceof RuntimeLevel runtimeLevel) {
                if (runtimeLevel.style == RuntimeLevel.Style.TEMPORARY) {
                    temporaryLevels.add(runtimeLevel);
                }
            }
        }
        return temporaryLevels;
    }
}
