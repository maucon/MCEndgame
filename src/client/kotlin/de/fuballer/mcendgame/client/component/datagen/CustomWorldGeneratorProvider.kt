package de.fuballer.mcendgame.client.component.datagen

import de.fuballer.mcendgame.main.MCEndgame
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.RegistryWrapper
import java.util.concurrent.CompletableFuture

class CustomWorldGeneratorProvider(
    output: FabricDataOutput,
    registriesFuture: CompletableFuture<RegistryWrapper.WrapperLookup>
) : FabricDynamicRegistryProvider(output, registriesFuture) {
    override fun getName() = "${MCEndgame.MOD_ID} World Generation Provider"

    override fun configure(
        registries: RegistryWrapper.WrapperLookup,
        entries: Entries
    ) {
        entries.addAll(registries.getOrThrow(RegistryKeys.BIOME))
    }
}