package de.fuballer.mcendgame.client.component.datagen

import de.fuballer.mcendgame.main.component.block.CustomBlocks
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootSubProvider
import net.minecraft.core.HolderLookup
import java.util.concurrent.CompletableFuture

class CustomLootTableProvider(
    packOutput: FabricPackOutput,
    registryLookup: CompletableFuture<HolderLookup.Provider>,
) : FabricBlockLootSubProvider(packOutput, registryLookup) {
    override fun generate() {
        dropSelf(CustomBlocks.DUNGEON_DEVICE)
        dropSelf(CustomBlocks.CRYSTAL_FORGE)
        dropSelf(CustomBlocks.TOTEM_STATUE)
    }
}