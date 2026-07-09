package de.fuballer.mcendgame.client.component.item.custom.armor.model.bound_abyss

import de.fuballer.mcendgame.client.component.item.custom.ModelPartDataExtension.createEmptyChild
import de.fuballer.mcendgame.client.util.EntityRenderStateMixinExtension.getLowHealthTicks
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
import org.joml.Vector3f

class BoundAbyssModel<S : HumanoidRenderState>(
    root: ModelPart
) : HumanoidModel<S>(root) {
    private val shoulderPadLeft: ModelPart
    private val vambraceLeft: ModelPart
    private val shoulderPadRight: ModelPart
    private val vambraceRight: ModelPart

    init {
        val chestplateArmLeft = this.leftArm.getChild("chestplateArmLeft")
        shoulderPadLeft = chestplateArmLeft.getChild("shoulderPadLeft")
        val sleeveLeft = chestplateArmLeft.getChild("sleeveLeft")
        vambraceLeft = sleeveLeft.getChild("vambraceLeft")

        val chestplateArmRight = this.rightArm.getChild("chestplateArmRight")
        shoulderPadRight = chestplateArmRight.getChild("shoulderPadRight")
        val sleeveRight = chestplateArmRight.getChild("sleeveRight")
        vambraceRight = sleeveRight.getChild("vambraceRight")
    }

    companion object {
        val MODEL_LAYER = ModelLayerLocation(IdentifierUtil.default("bound_abyss"), "main")

        fun getTexturedModelData(): LayerDefinition {
            val modelData = MeshDefinition()
            val modelPartData = modelData.root

            val head = modelPartData.createEmptyChild(PartNames.HEAD)
            val hat = head.createEmptyChild(PartNames.HAT)
            val right_leg = modelPartData.createEmptyChild(PartNames.RIGHT_LEG)
            val left_leg = modelPartData.createEmptyChild(PartNames.LEFT_LEG)

            val body = modelPartData.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0f, 0.0f, 0.0f))

            val chestplateBody = body.addOrReplaceChild(
                "chestplateBody",
                CubeListBuilder.create().texOffs(18, 35).addBox(-4.5f, -24.5f, -2.5f, 9.0f, 14.0f, 5.0f, CubeDeformation(0.25f))
                    .texOffs(16, 22).addBox(-5.0f, -24.85f, -3.0f, 10.0f, 7.0f, 6.0f, CubeDeformation(0.0f)),
                PartPose.offset(0.0f, 24.0f, 0.0f)
            )

            val belt = chestplateBody.addOrReplaceChild(
                "belt",
                CubeListBuilder.create().texOffs(14, 54).addBox(-5.5f, -2.0f, -3.5f, 11.0f, 3.0f, 7.0f, CubeDeformation(-0.5f)),
                PartPose.offsetAndRotation(0.0f, -15.25f, 0.0f, 0.0f, 0.0f, -0.0873f)
            )

            val left_arm =
                modelPartData.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(5.0f, 2.0f, 0.0f))

            val chestplateArmLeft = left_arm.addOrReplaceChild(
                "chestplateArmLeft",
                CubeListBuilder.create(),
                PartPose.offset(-5.0f, 22.0f, 0.0f)
            )

            val shoulderPadLeft = chestplateArmLeft.addOrReplaceChild(
                "shoulderPadLeft",
                CubeListBuilder.create().texOffs(44, 18).addBox(-3.0f, -2.0f, -2.5f, 5.0f, 5.0f, 5.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(7.0f, -23.0f, 0.0f, 0.0436f, 0.0436f, 0.1309f)
            )

            val sleeveLeft = chestplateArmLeft.addOrReplaceChild(
                "sleeveLeft",
                CubeListBuilder.create().texOffs(48, 30).addBox(4.0f, -24.0f, -2.0f, 4.0f, 12.0f, 4.0f, CubeDeformation(0.35f)),
                PartPose.offset(0.0f, 0.0f, 0.0f)
            )

            val vambraceLeft = sleeveLeft.addOrReplaceChild(
                "vambraceLeft",
                CubeListBuilder.create().texOffs(48, 46).addBox(4.0f, -18.0f, -2.0f, 4.0f, 6.0f, 4.0f, CubeDeformation(0.5f)),
                PartPose.offset(0.0f, 0.0f, 0.0f)
            )

            val right_arm =
                modelPartData.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-5.0f, 2.0f, 0.0f))

            val chestplateArmRight = right_arm.addOrReplaceChild(
                "chestplateArmRight",
                CubeListBuilder.create(),
                PartPose.offset(5.0f, 22.0f, 0.0f)
            )

            val shoulderPadRight = chestplateArmRight.addOrReplaceChild(
                "shoulderPadRight",
                CubeListBuilder.create().texOffs(0, 18).addBox(-2.0f, -2.0f, -2.5f, 5.0f, 5.0f, 5.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(-7.0f, -23.0f, 0.0f, 0.0436f, -0.0436f, -0.1309f)
            )

            val sleeveRight = chestplateArmRight.addOrReplaceChild(
                "sleeveRight",
                CubeListBuilder.create().texOffs(0, 30).addBox(-8.0f, -24.0f, -2.0f, 4.0f, 12.0f, 4.0f, CubeDeformation(0.35f)),
                PartPose.offset(0.0f, 0.0f, 0.0f)
            )

            val vambraceRight = sleeveRight.addOrReplaceChild(
                "vambraceRight",
                CubeListBuilder.create().texOffs(0, 46).addBox(-8.0f, -18.0f, -2.0f, 4.0f, 6.0f, 4.0f, CubeDeformation(0.5f)),
                PartPose.offset(0.0f, 0.0f, 0.0f)
            )

            return LayerDefinition.create(modelData, 64, 64)
        }
    }

    override fun setupAnim(renderState: S) {
        resetNotCopiedTransforms()
        setLowHealthAngles(renderState)
    }

    private fun resetNotCopiedTransforms() {
        shoulderPadLeft.resetPose()
        shoulderPadRight.resetPose()
        vambraceLeft.resetPose()
        vambraceRight.resetPose()
    }

    private fun setLowHealthAngles(renderState: S) {
        val lowHealthTicks20 = renderState.getLowHealthTicks()
        val openPercent = lowHealthTicks20 / 20F

        shoulderPadLeft.offsetPos(Vector3f(openPercent * 1.5F, openPercent * -0.8F, 0F))
        shoulderPadRight.offsetPos(Vector3f(openPercent * -1.5F, openPercent * -0.8F, 0F))
        vambraceLeft.offsetPos(Vector3f(openPercent * 1.2F, 0F, 0F))
        vambraceRight.offsetPos(Vector3f(openPercent * -1.2F, 0F, 0F))
    }
}