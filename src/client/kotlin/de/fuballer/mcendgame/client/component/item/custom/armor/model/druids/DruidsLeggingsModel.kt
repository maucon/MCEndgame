package de.fuballer.mcendgame.client.component.item.custom.armor.model.druids

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
import kotlin.math.*

class DruidsLeggingsModel<S : HumanoidRenderState>(
    root: ModelPart
) : HumanoidModel<S>(root) {
    private val battleSkirtBack: ModelPart
    private val battleSkirtFront: ModelPart

    init {
        val leggingsWaist = this.body.getChild("leggings_waist")
        val battleSkirt = leggingsWaist.getChild("battle_skirt")
        battleSkirtBack = battleSkirt.getChild("battle_skirt_back")
        battleSkirtFront = battleSkirt.getChild("battle_skirt_front")
    }

    companion object {
        val MODEL_LAYER = ModelLayerLocation(IdentifierUtil.default("druids_leggings"), "main")

        fun getTexturedModelData(): LayerDefinition {
            val modelData = MeshDefinition()
            val modelPartData = modelData.root

            val head = modelPartData.createEmptyChild(PartNames.HEAD)
            val hat = head.createEmptyChild(PartNames.HAT)
            val left_arm = modelPartData.createEmptyChild(PartNames.LEFT_ARM)
            val right_arm = modelPartData.createEmptyChild(PartNames.RIGHT_ARM)

            val body = modelPartData.createEmptyChild(PartNames.BODY)

            val leggings_waist = body.addOrReplaceChild(
                "leggings_waist",
                CubeListBuilder.create().texOffs(5, 40).addBox(-5.0f, 7.0f, -3.0f, 10.0f, 7.0f, 6.0f, CubeDeformation(0.05f))
                    .texOffs(9, 33).addBox(-4.0f, 9.0f, -2.0f, 8.0f, 3.0f, 4.0f, CubeDeformation(0.5f))
                    .texOffs(9, 27).addBox(-4.0f, 6.75f, -2.0f, 8.0f, 2.0f, 4.0f, CubeDeformation(0.26f)),
                PartPose.offset(0.0f, 0.0f, 0.0f)
            )

            val battle_skirt = leggings_waist.addOrReplaceChild(
                "battle_skirt",
                CubeListBuilder.create(),
                PartPose.offset(0.0f, 0.0f, 0.0f)
            )

            val battle_skirt_back = battle_skirt.addOrReplaceChild(
                "battle_skirt_back",
                CubeListBuilder.create().texOffs(49, 0).addBox(-4.0f, 0.0f, 0.0f, 8.0f, 14.0f, 0.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(0.0f, 9.5f, 2.75f, 0.0873f, 0.0f, 0.0f)
            )

            val battle_skirt_left = battle_skirt.addOrReplaceChild(
                "battle_skirt_left",
                CubeListBuilder.create().texOffs(43, 2).addBox(-3.0f, -1.0218f, -0.0005f, 3.0f, 5.0f, 0.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(4.75f, 10.5f, 2.25f, 0.0f, -1.5708f, -0.0436f)
            )

            val battle_skirt_right = battle_skirt.addOrReplaceChild(
                "battle_skirt_right",
                CubeListBuilder.create().texOffs(65, 2).addBox(0.0f, -1.0218f, -0.0005f, 3.0f, 5.0f, 0.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(-4.75f, 10.5f, 2.25f, 0.0f, 1.5708f, 0.0436f)
            )

            val battle_skirt_front = battle_skirt.addOrReplaceChild(
                "battle_skirt_front",
                CubeListBuilder.create().texOffs(53, 14).addBox(-2.0f, 0.0f, 0.0f, 4.0f, 6.0f, 0.0f, CubeDeformation(0.0f))
                    .texOffs(54, 20).addBox(-1.5f, 6.0f, 0.0f, 3.0f, 6.0f, 0.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(0.0f, 10.0f, -3.0f, -0.0436f, 0.0f, 0.0f)
            )

            val left_leg =
                modelPartData.addOrReplaceChild(
                    PartNames.LEFT_LEG,
                    CubeListBuilder.create(),
                    PartPose.offset(2.0f, 12.0f, 0.0f)
                )

            val left_leggings = left_leg.addOrReplaceChild(
                "left_leggings",
                CubeListBuilder.create().texOffs(24, 53).addBox(-2.1f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, CubeDeformation(0.5f)),
                PartPose.offset(0.0f, 0.0f, 0.0f)
            )

            val right_leg =
                modelPartData.addOrReplaceChild(
                    PartNames.RIGHT_LEG,
                    CubeListBuilder.create(),
                    PartPose.offset(-2.0f, 12.0f, 0.0f)
                )

            val right_leggings = right_leg.addOrReplaceChild(
                "right_leggings",
                CubeListBuilder.create().texOffs(2, 53).addBox(-1.9f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, CubeDeformation(0.5f)),
                PartPose.offset(0.0f, 0.0f, 0.0f)
            )
            return LayerDefinition.create(modelData, 128, 128)
        }
    }

    override fun setupAnim(renderState: S) {
        resetNotCopiedTransforms()
        setBattleSkirtAngles(renderState)
    }

    private fun resetNotCopiedTransforms() {
        battleSkirtBack.resetPose()
        battleSkirtFront.resetPose()
    }

    private fun setBattleSkirtAngles(renderState: S) {
        val minPitchFront = max(0F, abs(min(leftLeg.xRot, rightLeg.xRot)))
        battleSkirtFront.xRot -= minPitchFront

        val minPitchBack = max(0F, max(leftLeg.xRot, rightLeg.xRot))
        val speed = renderState.walkAnimationSpeed // 0.0 to 1.0
        val speedPitchBack = speed * 1.45F
        val randomPitchVariance = sin(renderState.ageInTicks / 3F) * 0.08F * speed
        battleSkirtBack.xRot += max(minPitchBack, speedPitchBack + randomPitchVariance)

        val randomRollVariance = cos(renderState.ageInTicks / 8F) * 0.03F
        battleSkirtBack.zRot += randomRollVariance
    }
}