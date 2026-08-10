package de.fuballer.mcendgame.client.component.datagen

import de.fuballer.mcendgame.main.component.biome.CustomBiomes
import de.fuballer.mcendgame.main.component.dimension.CustomDimensions
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import java.util.concurrent.CompletableFuture

class CustomRegistryProvider(
    packOutput: FabricPackOutput,
    registryLookup: CompletableFuture<HolderLookup.Provider>,
) : FabricDynamicRegistryProvider(packOutput, registryLookup) {
    override fun configure(
        registries: HolderLookup.Provider,
        entries: Entries
    ) {
        registries.lookupOrThrow(Registries.DIMENSION_TYPE)
            .get(CustomDimensions.DUNGEON)
            .ifPresent(entries::add)

        val biomes = registries.lookupOrThrow(Registries.BIOME)
        biomes.get(CustomBiomes.DESERT_DUNGEON)
            .ifPresent(entries::add)
        biomes.get(CustomBiomes.BEASTWEAVER_GROVE_DUNGEON)
            .ifPresent(entries::add)
    }

    override fun getName() = "MCEndgameRegistryProvider"
}