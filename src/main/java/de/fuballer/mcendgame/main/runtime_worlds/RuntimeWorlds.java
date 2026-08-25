package de.fuballer.mcendgame.main.runtime_worlds;

import com.google.common.base.Preconditions;
import de.fuballer.mcendgame.main.configuration.RuntimeConfig;
import de.fuballer.mcendgame.main.mixin.runtime_worlds.MinecraftServerAccess;
import de.fuballer.mcendgame.main.util.extension.BlockPosExtension;
import de.fuballer.mcendgame.main.util.extension.mixin.WorldMixinExtension;
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class RuntimeWorlds {
    public static final String DUNGEON_WORLD_PREFIX = "dungeon-world";

    private static RuntimeWorlds instance;

    private final MinecraftServer server;
    private final MinecraftServerAccess serverAccess;

    private final RuntimeLevelManager levelManager;

    static {
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            RuntimeWorlds runtimeWorlds = create(server);
            runtimeWorlds.onServerStopping();
        });
    }

    private RuntimeWorlds(MinecraftServer server) {
        this.server = server;
        this.serverAccess = (MinecraftServerAccess) server;

        this.levelManager = new RuntimeLevelManager(server);
    }

    public static RuntimeWorlds create(MinecraftServer server) {
        Preconditions.checkState(server.isSameThread(), "cannot create levels from off-thread!");

        if (instance == null || instance.server != server) {
            instance = new RuntimeWorlds(server);
        }

        return instance;
    }

    public RuntimeLevelHandle openTemporaryDungeon(RuntimeLevelConfig config) {
        var key = this.generateIdentifier();
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

    private Identifier generateIdentifier() {
        return IdentifierUtil.INSTANCE.defaultJava(DUNGEON_WORLD_PREFIX + "-" + UUID.randomUUID());
    }

    private void kickPlayers(ServerLevel level) {
        if (level.players().isEmpty()) {
            return;
        }

        List<ServerPlayer> players = new ArrayList<>(level.players());
        var exitPos = WorldMixinExtension.INSTANCE.getDungeonExitPos(level);
        var targetWorld = RuntimeConfig.SERVER.getLevel(exitPos.dimension());

        if (targetWorld == null) {
            ServerLevel spawnLevel = this.server.findRespawnDimension();
            LevelData.RespawnData spawnPoint = this.server.getRespawnData();

            for (ServerPlayer player : players) {
                Vec3 pos = Vec3.atBottomCenterOf(player.adjustSpawnLocation(spawnLevel, spawnPoint.pos()));
                player.teleport(new TeleportTransition(spawnLevel, pos, Vec3.ZERO, spawnPoint.yaw(), spawnPoint.pitch(), TeleportTransition.DO_NOTHING));
            }
            return;
        }

        Vec3 exitVec = BlockPosExtension.INSTANCE.toVec3d(exitPos.pos()).add(0.5, 1.0, 0.5);
        TeleportTransition target = new TeleportTransition(
                targetWorld,
                exitVec,
                Vec3.ZERO,
                0.0F,
                0.0F,
                TeleportTransition.DO_NOTHING
        );

        for (ServerPlayer player : players) {
            player.teleport(target);
        }
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
                temporaryLevels.add(runtimeLevel);
            }
        }
        return temporaryLevels;
    }
}
