package de.fuballer.mcendgame.client.mixin.model_submit;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import de.fuballer.mcendgame.client.accessor.ModelSubmitAccessor;
import de.fuballer.mcendgame.client.component.entity.custom.entities.beastweaver.BeastweaverGradientModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.OutlineBufferSource;
import net.minecraft.client.renderer.SubmitNodeStorage;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModelFeatureRenderer.class)
public class ModelFeatureRendererBeastweaverGradientMixin {
    @Shadow
    @Final
    private PoseStack poseStack;

    @Inject(
            method = "renderModel",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/model/Model;renderToBuffer(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;III)V",
                    ordinal = 0
            ),
            cancellable = true
    )
    <S> void renderWithBeastweaverGradient(
            SubmitNodeStorage.ModelSubmit<S> submit,
            RenderType renderType,
            VertexConsumer buffer,
            OutlineBufferSource outlineBufferSource,
            MultiBufferSource.BufferSource crumblingBufferSource,
            CallbackInfo ci,
            @Local(name = "model") Model<? super S> model,
            @Local(name = "wrappedBuffer") VertexConsumer wrappedBuffer
    ) {
        if (!(model instanceof BeastweaverGradientModel beastweaverGradientModel)) return;

        var accessor = (ModelSubmitAccessor) (Object) submit;
        beastweaverGradientModel.renderToBufferWithGradient(
                poseStack,
                wrappedBuffer,
                submit.lightCoords(),
                submit.overlayCoords(),
                submit.tintedColor(),
                accessor.mcendgame$getBeastweaverGradientData()
        );
        ci.cancel();
    }
}
