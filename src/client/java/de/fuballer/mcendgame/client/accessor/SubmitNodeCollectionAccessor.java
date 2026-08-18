package de.fuballer.mcendgame.client.accessor;

import com.mojang.blaze3d.vertex.PoseStack;
import de.fuballer.mcendgame.client.component.entity.custom.entities.beastweaver.BeastweaverGradientData;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.jspecify.annotations.Nullable;

public interface SubmitNodeCollectionAccessor {
    <S> void mcendgame$submitBeastweaverGradientModel(
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
    );
}
