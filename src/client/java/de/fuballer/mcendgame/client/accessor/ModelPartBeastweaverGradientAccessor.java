package de.fuballer.mcendgame.client.accessor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import de.fuballer.mcendgame.client.component.entity.custom.entities.beastweaver.BeastweaverGradientData;

public interface ModelPartBeastweaverGradientAccessor {
    void mcendgame$renderWithGradient(
            final PoseStack poseStack,
            final VertexConsumer buffer,
            final int lightCoords,
            final int overlayCoords,
            final int color,
            final BeastweaverGradientData gradientData
    );
}
