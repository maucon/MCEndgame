package de.fuballer.mcendgame.client.component.entity.custom.entities.bandit

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.model.HumanoidModel
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.model.geom.PartNames
import net.minecraft.client.renderer.entity.state.HumanoidRenderState
import net.minecraft.client.renderer.rendertype.RenderTypes
import net.minecraft.resources.Identifier
import net.minecraft.world.entity.HumanoidArm
import java.util.function.Function

open class BanditModel(
    root: ModelPart,
    private val slim: Boolean,
) : HumanoidModel<BanditRenderState>(root, Function { texture: Identifier -> RenderTypes.entityTranslucent(texture) }) {
    private val bodyParts: MutableList<ModelPart> = mutableListOf(head, body, leftArm, rightArm, leftLeg, rightLeg)
    val leftSleeve: ModelPart = leftArm.getChild(LEFT_SLEEVE)
    val rightSleeve: ModelPart = rightArm.getChild(RIGHT_SLEEVE)
    val leftPants: ModelPart = leftLeg.getChild(LEFT_PANTS)
    val rightPants: ModelPart = rightLeg.getChild(RIGHT_PANTS)
    val jacket: ModelPart = body.getChild(PartNames.JACKET)

    override fun setupAnim(state: BanditRenderState) {
        hat.visible = state.showHat
        jacket.visible = state.showJacket
        leftPants.visible = state.showLeftPants
        rightPants.visible = state.showRightPants
        leftSleeve.visible = state.showLeftSleeve
        rightSleeve.visible = state.showRightSleeve
        super.setupAnim(state)
    }

    override fun translateToHand(state: HumanoidRenderState, arm: HumanoidArm, poseStack: PoseStack) {
        root().translateAndRotate(poseStack)
        val part = getArm(arm)
        if (slim) {
            val offset = 0.5f * (if (arm == HumanoidArm.RIGHT) 1 else -1).toFloat()
            part.x += offset
            part.translateAndRotate(poseStack)
            part.x -= offset
        } else {
            part.translateAndRotate(poseStack)
        }
    }

    companion object {
        protected const val LEFT_SLEEVE: String = "left_sleeve"
        protected const val RIGHT_SLEEVE: String = "right_sleeve"
        protected const val LEFT_PANTS: String = "left_pants"
        protected const val RIGHT_PANTS: String = "right_pants"
    }
}