package de.fuballer.mcendgame.client.mixin.model_submit;

import com.mojang.blaze3d.vertex.PoseStack;
import de.fuballer.mcendgame.client.accessor.ModelSubmitAccessor;
import de.fuballer.mcendgame.client.accessor.SubmitNodeCollectionAccessor;
import de.fuballer.mcendgame.client.component.entity.custom.entities.beastweaver.BeastweaverGradientData;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.SubmitNodeCollection;
import net.minecraft.client.renderer.SubmitNodeStorage;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SubmitNodeCollection.class)
public class SubmitNodeCollectionMixin implements SubmitNodeCollectionAccessor {
    @Shadow
    private boolean wasUsed;

    @Shadow
    @Final
    private ModelFeatureRenderer.Storage modelSubmits;

    @Override
    public <S> void mcendgame$submitBeastweaverGradientModel(
            Model<? super S> model,
            S state,
            PoseStack poseStack,
            RenderType renderType,
            int lightCoords,
            int overlayCoords,
            int tintedColor,
            @Nullable TextureAtlasSprite sprite,
            int outlineColor,
            ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay,
            BeastweaverGradientData gradientData
    ) {
        wasUsed = true;
        SubmitNodeStorage.ModelSubmit<S> modelSubmit = new SubmitNodeStorage.ModelSubmit<>(
                poseStack.last().copy(), model, state, lightCoords, overlayCoords, tintedColor, sprite, outlineColor, crumblingOverlay
        );

        ((ModelSubmitAccessor) (Object) modelSubmit).mcendgame$setBeastweaverGradientData(gradientData);

        modelSubmits.add(renderType, modelSubmit);
    }
}
