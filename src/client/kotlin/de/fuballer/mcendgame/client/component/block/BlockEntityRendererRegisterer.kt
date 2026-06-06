package de.fuballer.mcendgame.client.component.block

import de.fuballer.mcendgame.client.component.block.totem_statue.TotemStatueBlockEntityRenderer
import de.fuballer.mcendgame.main.component.block.CustomBlockEntityTypes
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.initializer.Initializer
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers

@Injectable
class BlockEntityRendererRegisterer {
    @Initializer
    fun init() {
        BlockEntityRenderers.register(CustomBlockEntityTypes.TOTEM_STATUE) { ctx -> TotemStatueBlockEntityRenderer(ctx) }
    }
}