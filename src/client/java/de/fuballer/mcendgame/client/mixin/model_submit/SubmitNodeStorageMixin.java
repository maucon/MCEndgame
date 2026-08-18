package de.fuballer.mcendgame.client.mixin.model_submit;

import com.mojang.blaze3d.vertex.PoseStack;
import de.fuballer.mcendgame.client.accessor.SubmitNodeCollectionAccessor;
import de.fuballer.mcendgame.client.accessor.SubmitNodeStorageAccessor;
import de.fuballer.mcendgame.client.component.entity.custom.entities.beastweaver.BeastweaverGradientData;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.OrderedSubmitNodeCollector;
import net.minecraft.client.renderer.SubmitNodeStorage;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({SubmitNodeStorage.class})
public abstract class SubmitNodeStorageMixin implements SubmitNodeStorageAccessor {

    @Shadow
    public abstract OrderedSubmitNodeCollector order(int par1);

    @Override
    public <S> void mcendgame$submitBeastweaverGradientModel(
            final Model<? super S> model,
            final S state,
            final PoseStack poseStack,
            final RenderType renderType,
            final int lightCoords,
            final int overlayCoords,
            final int tintedColor,
            @Nullable final TextureAtlasSprite sprite,
            final int outlineColor,
            final ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay,
            final BeastweaverGradientData gradientData
    ) {
        var collector = order(0);
        var accessor = (SubmitNodeCollectionAccessor) collector;
        accessor.mcendgame$submitBeastweaverGradientModel(
                model,
                state,
                poseStack,
                renderType,
                lightCoords,
                overlayCoords,
                tintedColor,
                sprite,
                outlineColor,
                crumblingOverlay,
                gradientData
        );
    }
}
