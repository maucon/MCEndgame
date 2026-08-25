package de.fuballer.mcendgame.main.runtime_worlds;

import com.mojang.logging.LogUtils;
import de.fuballer.mcendgame.main.mixin.runtime_worlds.MinecraftServerAccess;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLevelEvents;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;

final class RuntimeLevelManager {
    private final Logger LOG = LogUtils.getLogger();

    private final MinecraftServer server;
    private final MinecraftServerAccess serverAccess;

    RuntimeLevelManager(MinecraftServer server) {
        this.server = server;
        this.serverAccess = (MinecraftServerAccess) server;
    }

    RuntimeLevel add(ResourceKey<Level> levelKey, RuntimeLevelConfig config) {
        LevelStem options = config.createDimensionOptions(this.server);

        MappedRegistry<LevelStem> dimensionsRegistry = getDimensionsRegistry(this.server);
        try (var _ = RemoveFromRegistry.thaw(dimensionsRegistry)) {
            var key = ResourceKey.create(Registries.LEVEL_STEM, levelKey.identifier());
            if (!dimensionsRegistry.containsKey(key)) {
                dimensionsRegistry.register(key, options, RegistrationInfo.BUILT_IN);
            }
        }

        RuntimeLevel level = new RuntimeLevel(this.server, levelKey, config, options);

        this.serverAccess.getLevels().put(level.dimension(), level);
        ServerLevelEvents.LOAD.invoker().onLevelLoad(this.server, level);

        // tick the level to ensure it is ready for use right away
        level.tick(() -> true);

        return level;
    }

    void delete(ServerLevel level) {
        ResourceKey<Level> dimensionKey = level.dimension();

        if (this.serverAccess.getLevels().remove(dimensionKey, level)) {
            ServerLevelEvents.UNLOAD.invoker().onLevelUnload(this.server, level);

            MappedRegistry<LevelStem> dimensionsRegistry = getDimensionsRegistry(this.server);
            this.unregister(dimensionKey, dimensionsRegistry);

            try {
                level.close();
            } catch (IOException e) {
                LOG.error("Exception closing the level", e);
            }

            LevelStorageSource.LevelStorageAccess session = this.serverAccess.getStorageSource();
            File levelDirectory = session.getDimensionPath(dimensionKey).toFile();
            if (levelDirectory.exists()) {
                try {
                    FileUtils.deleteDirectory(levelDirectory);
                } catch (IOException e) {
                    LOG.warn("Failed to delete level directory", e);
                    try {
                        FileUtils.forceDeleteOnExit(levelDirectory);
                    } catch (IOException ignored) {
                    }
                }
            }
        }
    }

    private void unregister(ResourceKey<Level> dimensionKey, MappedRegistry<LevelStem> dimensionsRegistry) {
        RemoveFromRegistry.remove(dimensionsRegistry, dimensionKey.identifier());
    }

    private static MappedRegistry<LevelStem> getDimensionsRegistry(MinecraftServer server) {
        RegistryAccess registryManager = server.registries().compositeAccess();
        return (MappedRegistry<LevelStem>) registryManager.lookupOrThrow(Registries.LEVEL_STEM);
    }
}
