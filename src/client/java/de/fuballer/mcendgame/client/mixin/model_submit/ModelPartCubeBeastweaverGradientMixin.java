package de.fuballer.mcendgame.client.mixin.model_submit;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import de.fuballer.mcendgame.client.accessor.BufferBuilderSetVertexElementsAccessor;
import de.fuballer.mcendgame.client.accessor.ModelPartCubeBeastweaverGradientAccessor;
import de.fuballer.mcendgame.client.component.entity.custom.entities.beastweaver.BeastweaverGradientData;
import net.minecraft.client.model.geom.ModelPart;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ModelPart.Cube.class)
public class ModelPartCubeBeastweaverGradientMixin implements ModelPartCubeBeastweaverGradientAccessor {
    @Shadow
    @Final
    public ModelPart.Polygon[] polygons;

    @Override
    public void mcendgame$compileWithGradient(
            PoseStack.Pose pose,
            VertexConsumer builder,
            int lightCoords,
            int overlayCoords,
            int color,
            BeastweaverGradientData gradientData
    ) {
        Matrix4f matrix = pose.pose();
        Vector3f scratchVector = new Vector3f();

        for (ModelPart.Polygon polygon : polygons) {
            Vector3f normal = pose.transformNormal(polygon.normal(), scratchVector);
            float nx = normal.x();
            float ny = normal.y();
            float nz = normal.z();

            for (ModelPart.Vertex vertex : polygon.vertices()) {
                float x = vertex.worldX();
                float y = vertex.worldY();
                float z = vertex.worldZ();
                Vector3f pos = matrix.transformPosition(x, y, z, scratchVector);

                var accessor = (BufferBuilderSetVertexElementsAccessor) builder;
                accessor.mcendgame$addVertex(
                        pos.x(),
                        pos.y(),
                        pos.z(),
                        color,
                        vertex.u(),
                        vertex.v(),
                        overlayCoords,
                        lightCoords,
                        nx,
                        ny,
                        nz,
                        gradientData.getGradOriginX(),
                        gradientData.getGradOriginY(),
                        gradientData.getGradOriginZ(),
                        gradientData.getGradStart(),
                        gradientData.getGradEnd()
                );
            }
        }
    }
}
