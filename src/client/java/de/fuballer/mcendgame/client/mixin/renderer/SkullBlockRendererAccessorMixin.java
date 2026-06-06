package de.fuballer.mcendgame.client.mixin.renderer;

import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.SkullBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(SkullBlockRenderer.class)
public interface SkullBlockRendererAccessorMixin {
    @Accessor("SKIN_BY_TYPE")
    static Map<SkullBlock.Type, Identifier> getTextures() {
        throw new UnsupportedOperationException();
    }
}
