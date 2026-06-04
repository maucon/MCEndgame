package de.fuballer.mcendgame.client.mixin.phasing;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.world.entity.player.Player;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ScreenEffectRenderer.class)
public class ScreenEffectRendererBlockPhasingMixin {
    @ModifyExpressionValue(
            method = "renderScreenEffect",
            at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/player/Player;noPhysics:Z", opcode = Opcodes.GETFIELD)
    )
    private static boolean doNotRenderInWallOverlay(
            boolean original,
            @Local Player playerEntity
    ) {
        if (CustomAttributesExtensions.INSTANCE.hasBlockPhasing(playerEntity)) return true;
        return original;
    }
}
