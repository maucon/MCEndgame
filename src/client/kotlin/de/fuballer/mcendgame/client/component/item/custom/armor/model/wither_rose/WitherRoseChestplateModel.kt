package de.fuballer.mcendgame.client.component.item.custom.armor.model.wither_rose

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

class WitherRoseChestplateModel<S : HumanoidRenderState>(
    root: ModelPart,
) : HumanoidModel<S>(root) {
    companion object {
        val MODEL_LAYER = ModelLayerLocation(IdentifierUtil.default("wither_rose_chestplate"), "main")

        fun getTexturedModelData(): LayerDefinition {
            val modelData = MeshDefinition()
            val modelPartData = modelData.root

            val head = modelPartData.createEmptyChild(PartNames.HEAD)
            val hat = head.createEmptyChild(PartNames.HAT)
            val left_leg = modelPartData.createEmptyChild(PartNames.LEFT_LEG)
            val right_leg = modelPartData.createEmptyChild(PartNames.RIGHT_LEG)

            val body = modelPartData.addOrReplaceChild(PartNames.BODY, CubeListBuilder.create(), PartPose.offset(0.0f, 0.0f, 0.0f))

            val chestplate = body.addOrReplaceChild(
                "chestplate", CubeListBuilder.create().texOffs(0, 52).addBox(-4.5f, 5.5f, -2.5f, 9.0f, 5.0f, 5.0f, CubeDeformation(0.3f))
                    .texOffs(0, 39).addBox(-5.0f, -0.5f, -3.25f, 10.0f, 6.0f, 6.0f, CubeDeformation(0.3f)), PartPose.offset(0.0f, 0.0f, 0.0f)
            )

            val left_arm = modelPartData.addOrReplaceChild(PartNames.LEFT_ARM, CubeListBuilder.create(), PartPose.offset(5.0f, 2.0f, 0.0f))

            val chestplate_left_arm = left_arm.addOrReplaceChild(
                "chestplate_left_arm", CubeListBuilder.create().texOffs(33, 17).addBox(-1.0f, -2.0f, -2.0f, 4.0f, 5.0f, 4.0f, CubeDeformation(0.5f))
                    .texOffs(33, 27).addBox(-1.0f, 3.25f, -2.0f, 4.0f, 7.0f, 4.0f, CubeDeformation(0.1f)), PartPose.offset(0.0f, 0.0f, 0.0f)
            )

            val vambraceLeft = chestplate_left_arm.addOrReplaceChild(
                "vambraceLeft",
                CubeListBuilder.create().texOffs(50, 28).addBox(1.0f, 4.25f, -3.0f, 2.0f, 5.0f, 5.0f, CubeDeformation(0.0f)),
                PartPose.offset(0.5f, 0.0f, 0.5f)
            )

            val pauldronLeft = chestplate_left_arm.addOrReplaceChild(
                "pauldronLeft",
                CubeListBuilder.create().texOffs(33, 6).addBox(-3.5f, 0.5f, -2.5f, 3.0f, 5.0f, 5.0f, CubeDeformation(0.5f)),
                PartPose.offsetAndRotation(3.75f, -3.5f, 0.0f, 0.0f, 0.0f, -0.0873f)
            )

            val pauldronLeftTop = pauldronLeft.addOrReplaceChild(
                "pauldronLeftTop",
                CubeListBuilder.create().texOffs(33, 0).addBox(-4.0f, 0.0f, -2.0f, 4.0f, 1.0f, 4.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.1745f)
            )

            val right_arm = modelPartData.addOrReplaceChild(PartNames.RIGHT_ARM, CubeListBuilder.create(), PartPose.offset(-5.0f, 2.0f, 0.0f))

            val chestplate_right_arm = right_arm.addOrReplaceChild(
                "chestplate_right_arm", CubeListBuilder.create().texOffs(15, 27).addBox(-3.0f, 3.25f, -2.0f, 4.0f, 7.0f, 4.0f, CubeDeformation(0.1f))
                    .texOffs(15, 17).addBox(-3.0f, -2.0f, -2.0f, 4.0f, 5.0f, 4.0f, CubeDeformation(0.5f)), PartPose.offset(0.0f, 0.0f, 0.0f)
            )

            val vambraceRight = chestplate_right_arm.addOrReplaceChild(
                "vambraceRight",
                CubeListBuilder.create().texOffs(0, 28).addBox(-3.0f, 4.25f, -3.0f, 2.0f, 5.0f, 5.0f, CubeDeformation(0.0f)),
                PartPose.offset(-0.5f, 0.0f, 0.5f)
            )

            val pauldronRight = chestplate_right_arm.addOrReplaceChild(
                "pauldronRight",
                CubeListBuilder.create().texOffs(15, 6).addBox(0.5f, 0.5f, -2.5f, 3.0f, 5.0f, 5.0f, CubeDeformation(0.5f)),
                PartPose.offsetAndRotation(-3.75f, -3.5f, 0.0f, 0.0f, 0.0f, 0.0873f)
            )

            val pauldronRightTop = pauldronRight.addOrReplaceChild(
                "pauldronRightTop",
                CubeListBuilder.create().texOffs(15, 0).addBox(0.0f, 0.0f, -2.0f, 4.0f, 1.0f, 4.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, -0.1745f)
            )
            return LayerDefinition.create(modelData, 128, 128)
        }
    }

    override fun setupAnim(renderState: S) {}
}