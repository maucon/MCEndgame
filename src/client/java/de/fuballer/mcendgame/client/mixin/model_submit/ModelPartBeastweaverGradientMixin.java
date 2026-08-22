package de.fuballer.mcendgame.client.mixin.model_submit;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import de.fuballer.mcendgame.client.accessor.ModelPartBeastweaverGradientAccessor;
import de.fuballer.mcendgame.client.accessor.ModelPartCubeBeastweaverGradientAccessor;
import de.fuballer.mcendgame.client.component.entity.custom.entities.beastweaver.BeastweaverGradientData;
import net.minecraft.client.model.geom.ModelPart;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;
import java.util.Map;

@Mixin(ModelPart.class)
public abstract class ModelPartBeastweaverGradientMixin implements ModelPartBeastweaverGradientAccessor {
    @Shadow
    public boolean visible;

    @Shadow
    @Final
    private List<ModelPart.Cube> cubes;

    @Shadow
    @Final
    private Map<String, ModelPart> children;

    @Shadow
    public abstract void translateAndRotate(PoseStack poseStack);

    @Shadow
    public boolean skipDraw;

    @Override
    public void mcendgame$renderWithGradient(
            final PoseStack poseStack,
            final VertexConsumer buffer,
            final int lightCoords,
            final int overlayCoords,
            final int color,
            final BeastweaverGradientData gradientData
    ) {
        if (!visible) return;
        if (cubes.isEmpty() && children.isEmpty()) return;

        poseStack.pushPose();
        translateAndRotate(poseStack);
        if (!skipDraw) {
            compileWithGradient(poseStack.last(), buffer, lightCoords, overlayCoords, color, gradientData);
        }

        for (ModelPart child : children.values()) {
            var accessor = (ModelPartBeastweaverGradientAccessor) (Object) child;
            accessor.mcendgame$renderWithGradient(poseStack, buffer, lightCoords, overlayCoords, color, gradientData);
        }

        poseStack.popPose();
    }

    @Unique
    private void compileWithGradient(
            final PoseStack.Pose pose,
            final VertexConsumer builder,
            final int lightCoords,
            final int overlayCoords,
            final int color,
            final BeastweaverGradientData gradientData
    ) {
        for (ModelPart.Cube cube : cubes) {
            var accessor = (ModelPartCubeBeastweaverGradientAccessor) cube;
            accessor.mcendgame$compileWithGradient(pose, builder, lightCoords, overlayCoords, color, gradientData);
        }
    }
}
