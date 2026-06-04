package de.fuballer.mcendgame.client.component.block

import de.fuballer.mcendgame.main.component.block.CustomBlocks
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.initializer.Initializer
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap
import net.minecraft.client.renderer.chunk.ChunkSectionLayer

@Injectable
object BlockRenderLayerInitializer {
    @Initializer
    fun init() {
        BlockRenderLayerMap.putBlock(CustomBlocks.DECAYING_COBWEB, ChunkSectionLayer.CUTOUT)
        BlockRenderLayerMap.putBlock(CustomBlocks.CRYSTAL_FORGE, ChunkSectionLayer.CUTOUT)
    }
}