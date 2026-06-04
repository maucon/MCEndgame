package de.fuballer.mcendgame.client.component.block.totem_statue

import de.fuballer.mcendgame.client.component.block.totem_statue.TotemStatueBlockEntityModel.TotemStatueModelState
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import net.minecraft.client.model.Model
import net.minecraft.client.model.geom.ModelLayerLocation
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.model.geom.PartPose
import net.minecraft.client.model.geom.builders.CubeDeformation
import net.minecraft.client.model.geom.builders.CubeListBuilder
import net.minecraft.client.model.geom.builders.LayerDefinition
import net.minecraft.client.model.geom.builders.MeshDefinition
import net.minecraft.client.renderer.rendertype.RenderTypes

class TotemStatueBlockEntityModel(
    root: ModelPart,
) : Model<TotemStatueModelState>(root, RenderTypes::entityTranslucent) {
    companion object {
        val MODEL_LAYER = ModelLayerLocation(IdentifierUtil.default("totem_statue"), "main")

        fun getTexturedModelData(): LayerDefinition {
            val modelData = MeshDefinition()
            val modelPartData = modelData.root
            modelPartData.addOrReplaceChild(
                "bb_main", CubeListBuilder.create().texOffs(4, 31).addBox(-3.0F, -8.0F, -3.0F, 6.0F, 6.0F, 6.0F, CubeDeformation(0.0F))
                    .texOffs(0, 0).addBox(-4.0F, -15.0F, -4.0F, 8.0F, 7.0F, 8.0F, CubeDeformation(0.0F))
                    .texOffs(12, 16).addBox(-1.0F, -11.0F, -5.0F, 2.0F, 4.0F, 2.0F, CubeDeformation(0.0F))
                    .texOffs(8, 44).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 2.0F, 4.0F, CubeDeformation(0.0F))
                    .texOffs(18, 20).addBox(3.0F, -8.0F, -1.0F, 4.0F, 2.0F, 3.0F, CubeDeformation(0.0F))
                    .texOffs(19, 26).addBox(3.0F, -6.0F, -1.0F, 3.0F, 1.0F, 3.0F, CubeDeformation(0.0F))
                    .texOffs(0, 20).addBox(-7.0F, -8.0F, -1.0F, 4.0F, 2.0F, 3.0F, CubeDeformation(0.0F))
                    .texOffs(1, 26).addBox(-6.0F, -6.0F, -1.0F, 3.0F, 1.0F, 3.0F, CubeDeformation(0.0F)),
                PartPose.ZERO
            )

            return LayerDefinition.create(modelData, 32, 64)
        }
    }

    class TotemStatueModelState()
}