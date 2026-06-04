package de.fuballer.mcendgame.client.component.entity.custom.entities.portal.type.legacy

import de.fuballer.mcendgame.client.component.entity.custom.entities.portal.PortalRenderState
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import net.minecraft.client.model.EntityModel
import net.minecraft.client.model.geom.ModelLayerLocation
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.model.geom.PartPose
import net.minecraft.client.model.geom.builders.CubeDeformation
import net.minecraft.client.model.geom.builders.CubeListBuilder
import net.minecraft.client.model.geom.builders.LayerDefinition
import net.minecraft.client.model.geom.builders.MeshDefinition

class LegacyPortalEntityModel(
    modelPart: ModelPart,
) : EntityModel<PortalRenderState>(modelPart) {
    private val portal: ModelPart = root.getChild("portal")

    companion object {
        val PORTAL = ModelLayerLocation(IdentifierUtil.default("legacy_portal"), "main")

        fun getTexturedModelData(): LayerDefinition {
            val modelData = MeshDefinition()
            val modelPartData = modelData.root
            val portal = modelPartData.addOrReplaceChild(
                "portal",
                CubeListBuilder.create().texOffs(0, 0).addBox(-8.0f, -16.0f, 0.0f, 16.0f, 32.0f, 0.0f, CubeDeformation(0.0f)),
                PartPose.offset(0.0f, 8.0f, 0.0f)
            )
            return LayerDefinition.create(modelData, 32, 32)
        }
    }
}