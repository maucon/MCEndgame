package de.fuballer.mcendgame.main.runtime_worlds;

import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.ApiStatus;

public interface RemoveFromRegistry<T> {
    @SuppressWarnings("unchecked")
    static <T> boolean remove(MappedRegistry<T> registry, Identifier key) {
        return ((RemoveFromRegistry<T>) registry).mcendgame$remove(key);
    }

    @SuppressWarnings("unchecked")
    static <T> boolean remove(MappedRegistry<T> registry, T value) {
        return ((RemoveFromRegistry<T>) registry).mcendgame$remove(value);
    }

    @SuppressWarnings("unchecked")
    static <T> RegistryRemoval thaw(Registry<T> registry) {
        RemoveFromRegistry<T> registry1 = ((RemoveFromRegistry<T>) registry);
        boolean priorStateOfMatter = registry1.mcendgame$isFrozen();
        registry1.mcendgame$setFrozen(false);
        return () -> registry1.mcendgame$setFrozen(priorStateOfMatter);
    }

    boolean mcendgame$remove(T value);

    boolean mcendgame$remove(Identifier key);

    void mcendgame$setFrozen(boolean value);

    boolean mcendgame$isFrozen();

    @ApiStatus.NonExtendable
    @FunctionalInterface
    interface RegistryRemoval extends AutoCloseable {
        @Override
        void close();
    }
}
