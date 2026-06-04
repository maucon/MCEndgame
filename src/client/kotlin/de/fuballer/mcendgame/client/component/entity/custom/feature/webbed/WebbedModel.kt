package de.fuballer.mcendgame.client.component.entity.custom.feature.webbed

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

class WebbedModel(
    modelPart: ModelPart,
) : Model<WebbedModel.WebbedData>(modelPart, RenderTypes::entityCutoutNoCull) {
    companion object {
        val WEBBED_LAYER = ModelLayerLocation(IdentifierUtil.default("webbed"), "main")

        fun getTexturedModelData(): LayerDefinition {
            val modelData = MeshDefinition()
            val modelPartData = modelData.root
            val webbed = modelPartData.addOrReplaceChild(
                "webbed",
                CubeListBuilder.create().texOffs(0, 0).addBox(-8.0f, -24.0f, -8.0f, 16.0f, 24.0f, 16.0f, CubeDeformation(0.0f))
                    .texOffs(64, 0).addBox(-8.0f, -24.0f, -8.0f, 16.0f, 24.0f, 16.0f, CubeDeformation(0.5f)),
                PartPose.offset(0.0f, 24.0f, 0.0f)
            )
            return LayerDefinition.create(modelData, 128, 64)
        }
    }

    class WebbedData()
}