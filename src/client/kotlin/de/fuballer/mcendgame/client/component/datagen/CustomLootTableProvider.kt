package de.fuballer.mcendgame.client.component.datagen

import de.fuballer.mcendgame.main.component.block.CustomBlocks
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider
import net.minecraft.core.HolderLookup
import java.util.concurrent.CompletableFuture

class CustomLootTableProvider(
    dataOutput: FabricDataOutput,
    registryLookup: CompletableFuture<HolderLookup.Provider>,
) : FabricBlockLootTableProvider(dataOutput, registryLookup) {
    override fun generate() {
        dropSelf(CustomBlocks.DUNGEON_DEVICE)
        dropSelf(CustomBlocks.CRYSTAL_FORGE)
        dropSelf(CustomBlocks.TOTEM_STATUE)
    }
}