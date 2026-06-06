package de.fuballer.mcendgame.client.component.datagen

import de.fuballer.mcendgame.main.MCEndgame
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import java.util.concurrent.CompletableFuture

class CustomWorldGeneratorProvider(
    packOutput: FabricPackOutput,
    registriesFuture: CompletableFuture<HolderLookup.Provider>
) : FabricDynamicRegistryProvider(packOutput, registriesFuture) {
    override fun getName() = "${MCEndgame.MOD_ID} World Generation Provider"

    override fun configure(
        registries: HolderLookup.Provider,
        entries: Entries
    ) {
        entries.addAll(registries.lookupOrThrow(Registries.BIOME))
    }
}