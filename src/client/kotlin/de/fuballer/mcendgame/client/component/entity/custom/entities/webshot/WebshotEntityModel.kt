package de.fuballer.mcendgame.client.component.entity.custom.entities.webshot

import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import net.minecraft.client.model.EntityModel
import net.minecraft.client.model.geom.ModelLayerLocation
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.model.geom.PartPose
import net.minecraft.client.model.geom.builders.CubeDeformation
import net.minecraft.client.model.geom.builders.CubeListBuilder
import net.minecraft.client.model.geom.builders.LayerDefinition
import net.minecraft.client.model.geom.builders.MeshDefinition

class WebshotEntityModel(
    modelPart: ModelPart,
) : EntityModel<WebshotRenderState>(modelPart) {
    val webshot: ModelPart = root.getChild("webshot")

    companion object {
        val WEBSHOT = ModelLayerLocation(IdentifierUtil.default("webshot"), "main")

        fun getTexturedModelData(): LayerDefinition {
            val modelData = MeshDefinition()
            val modelPartData = modelData.root
            val webshot = modelPartData.addOrReplaceChild(
                "webshot",
                CubeListBuilder.create().texOffs(0, 5).addBox(-2.0f, -2.0f, -1.0f, 4.0f, 4.0f, 2.0f, CubeDeformation(0.0f))
                    .texOffs(2, 0).addBox(-1.5f, -1.5f, -2.0f, 3.0f, 3.0f, 1.0f, CubeDeformation(0.0f))
                    .texOffs(1, 12).addBox(-1.5f, -1.5f, 1.0f, 3.0f, 3.0f, 2.0f, CubeDeformation(0.0f))
                    .texOffs(2, 18).addBox(-1.0f, -1.0f, 3.0f, 2.0f, 2.0f, 2.0f, CubeDeformation(0.0f))
                    .texOffs(3, 23).addBox(-0.5f, -0.5f, 5.0f, 1.0f, 1.0f, 2.0f, CubeDeformation(0.0f)),
                PartPose.ZERO
            )
            return LayerDefinition.create(modelData, 32, 32)
        }
    }

    override fun setupAnim(
        renderState: WebshotRenderState,
    ) {
        super.setupAnim(renderState)
        webshot.zRot += renderState.ageInTicks * 0.12F
    }
}