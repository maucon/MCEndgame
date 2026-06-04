package de.fuballer.mcendgame.client.component.item.custom.armor.model.suede

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

class SuedeChestplateModel<S : HumanoidRenderState>(
    root: ModelPart
) : HumanoidModel<S>(root) {
    companion object {
        val MODEL_LAYER = ModelLayerLocation(IdentifierUtil.default("suede_chestplate"), "main")

        fun getTexturedModelData(): LayerDefinition {
            val modelData = MeshDefinition()
            val modelPartData = modelData.root

            val head = modelPartData.createEmptyChild(PartNames.HEAD)
            val hat = head.createEmptyChild(PartNames.HAT)
            val left_leg = modelPartData.createEmptyChild(PartNames.LEFT_LEG)
            val right_leg = modelPartData.createEmptyChild(PartNames.RIGHT_LEG)

            val body = modelPartData.addOrReplaceChild(PartNames.BODY, CubeListBuilder.create(), PartPose.offset(0.0f, 0.0f, 0.0f))

            val chestplate = body.addOrReplaceChild(
                "chestplate", CubeListBuilder.create().texOffs(21, 20).addBox(-4.0f, 7.0f, -2.0f, 8.0f, 4.0f, 4.0f, CubeDeformation(0.25f))
                    .texOffs(19, 9).addBox(-4.5f, 2.0f, -2.5f, 9.0f, 5.0f, 5.0f, CubeDeformation(0.0f))
                    .texOffs(21, 0).addBox(-3.5f, -0.25f, -2.25f, 7.0f, 3.0f, 5.0f, CubeDeformation(0.0f)), PartPose.offset(0.0f, 0.0f, 0.0f)
            )

            val left_arm = modelPartData.addOrReplaceChild(PartNames.LEFT_ARM, CubeListBuilder.create(), PartPose.offset(5.0f, 2.0f, 0.0f))

            val chestplate_left_arm = left_arm.addOrReplaceChild(
                "chestplate_left_arm", CubeListBuilder.create().texOffs(48, 8).addBox(-1.0f, 0.0f, -2.0f, 4.0f, 7.0f, 4.0f, CubeDeformation(0.1f))
                    .texOffs(46, 0).addBox(-1.5f, 0.5f, -2.5f, 5.0f, 3.0f, 5.0f, CubeDeformation(-0.25f)), PartPose.offset(0.0f, 0.0f, 0.0f)
            )

            val right_arm = modelPartData.addOrReplaceChild(PartNames.RIGHT_ARM, CubeListBuilder.create(), PartPose.offset(-5.0f, 2.0f, 0.0f))

            val chestplate_right_arm = right_arm.addOrReplaceChild(
                "chestplate_right_arm", CubeListBuilder.create().texOffs(2, 8).addBox(-3.0f, 0.0f, -2.0f, 4.0f, 7.0f, 4.0f, CubeDeformation(0.1f))
                    .texOffs(0, 0).addBox(-3.5f, 0.5f, -2.5f, 5.0f, 3.0f, 5.0f, CubeDeformation(-0.25f)), PartPose.offset(0.0f, 0.0f, 0.0f)
            )
            return LayerDefinition.create(modelData, 128, 128)
        }
    }

    override fun setupAnim(renderState: S) {}
}