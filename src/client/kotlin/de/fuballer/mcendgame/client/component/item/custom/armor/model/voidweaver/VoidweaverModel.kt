package de.fuballer.mcendgame.client.component.item.custom.armor.model.voidweaver

import de.fuballer.mcendgame.client.component.item.custom.ModelPartDataExtension.createEmptyChild
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import net.minecraft.client.model.HumanoidModel
import net.minecraft.client.model.geom.ModelLayerLocation
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.model.geom.PartNames
import net.minecraft.client.model.geom.PartPose
import net.minecraft.client.model.geom.builders.CubeDeformation
import net.minecraft.client.model.geom.builders.CubeListBuilder
import net.minecraft.client.model.geom.builders.LayerDefinition
import net.minecraft.client.model.geom.builders.MeshDefinition
import net.minecraft.client.renderer.entity.state.HumanoidRenderState

class VoidweaverModel<S : HumanoidRenderState>(
    root: ModelPart
) : HumanoidModel<S>(root) {
    companion object {
        val MODEL_LAYER = ModelLayerLocation(IdentifierUtil.default("voidweaver"), "main")

        fun getTexturedModelData(): LayerDefinition {
            val modelData = MeshDefinition()
            val modelPartData = modelData.root

            val head = modelPartData.createEmptyChild(PartNames.HEAD)
            val hat = head.createEmptyChild(PartNames.HAT)
            val left_leg = modelPartData.createEmptyChild(PartNames.LEFT_LEG)
            val right_leg = modelPartData.createEmptyChild(PartNames.RIGHT_LEG)

            val body = modelPartData.addOrReplaceChild(PartNames.BODY, CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F))

            val chestplate = body.addOrReplaceChild(
                "chestplate", CubeListBuilder.create().texOffs(19, 13).addBox(-4.0F, 2.5F, -2.0F, 8.0F, 8.0F, 4.0F, CubeDeformation(0.2F))
                    .texOffs(17, 0).addBox(-4.5F, -0.5F, -2.6F, 9.0F, 7.0F, 5.0F, CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F)
            )

            val left_arm = modelPartData.addOrReplaceChild(PartNames.LEFT_ARM, CubeListBuilder.create(), PartPose.offset(5.0F, 2.0F, 0.0F))

            val chestplate_left_arm = left_arm.addOrReplaceChild(
                "chestplate_left_arm", CubeListBuilder.create().texOffs(46, 12).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 11.0F, 4.0F, CubeDeformation(0.05F))
                    .texOffs(46, 3).addBox(-0.85F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, CubeDeformation(0.3F)), PartPose.offset(0.0F, 0.0F, 0.0F)
            )

            val right_arm = modelPartData.addOrReplaceChild(PartNames.RIGHT_ARM, CubeListBuilder.create(), PartPose.offset(-5.0F, 2.0F, 0.0F))

            val chestplate_right_arm = right_arm.addOrReplaceChild(
                "chestplate_right_arm", CubeListBuilder.create().texOffs(0, 12).addBox(-13.0F, -2.0F, -2.0F, 4.0F, 11.0F, 4.0F, CubeDeformation(0.05F))
                    .texOffs(0, 3).addBox(-13.15F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, CubeDeformation(0.3F)), PartPose.offset(10.0F, 0.0F, 0.0F)
            )

            return LayerDefinition.create(modelData, 64, 32)
        }
    }

    override fun setupAnim(renderState: S) {}
}