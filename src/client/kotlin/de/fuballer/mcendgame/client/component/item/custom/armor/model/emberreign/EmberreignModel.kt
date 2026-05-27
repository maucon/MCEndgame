package de.fuballer.mcendgame.client.component.item.custom.armor.model.emberreign

import de.fuballer.mcendgame.client.component.item.custom.ModelPartDataExtension.createEmptyChild
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import net.minecraft.client.model.*
import net.minecraft.client.render.entity.model.BipedEntityModel
import net.minecraft.client.render.entity.model.EntityModelLayer
import net.minecraft.client.render.entity.model.EntityModelPartNames
import net.minecraft.client.render.entity.state.BipedEntityRenderState

class EmberreignModel<S : BipedEntityRenderState>(
    root: ModelPart
) : BipedEntityModel<S>(root) {
    companion object {
        val MODEL_LAYER = EntityModelLayer(IdentifierUtil.default("emberreign"), "main")

        fun getTexturedModelData(): TexturedModelData {
            val modelData = ModelData()
            val modelPartData = modelData.root

            val body = modelPartData.createEmptyChild(EntityModelPartNames.BODY)
            val head = modelPartData.createEmptyChild(EntityModelPartNames.HEAD)
            val hat = head.createEmptyChild(EntityModelPartNames.HAT)
            val left_arm = modelPartData.createEmptyChild(EntityModelPartNames.LEFT_ARM)
            val right_arm = modelPartData.createEmptyChild(EntityModelPartNames.RIGHT_ARM)

            val left_leg = modelPartData.addChild(EntityModelPartNames.LEFT_LEG, ModelPartBuilder.create(), ModelTransform.origin(2.0f, 12.0f, 0.0f))

            val left_boot = left_leg.addChild(
                "left_boot", ModelPartBuilder.create().uv(33, 21).cuboid(-2.5f, 6.5f, -2.5f, 5.0f, 6.0f, 5.0f, Dilation(-0.15f))
                    .uv(31, 33).cuboid(-3.0f, 10.0f, -3.0f, 6.0f, 3.0f, 6.0f, Dilation(-0.25f)), ModelTransform.origin(0.0f, 0.0f, 0.0f)
            )

            val left_feather_ribbon = left_boot.addChild(
                "left_feather_ribbon", ModelPartBuilder.create().uv(29, 11).cuboid(-3.5f, -1.0f, -3.5f, 7.0f, 2.0f, 7.0f, Dilation(-0.5f))
                    .uv(37, 0).cuboid(2.4f, -4.5f, -2.5f, 1.0f, 5.0f, 5.0f, Dilation(-0.5f)), ModelTransform.of(0.0f, 9.0f, 0.0f, -0.0873f, 0.0f, 0.0f)
            )

            val right_leg = modelPartData.addChild(EntityModelPartNames.RIGHT_LEG, ModelPartBuilder.create(), ModelTransform.origin(-2.0f, 12.0f, 0.0f))

            val right_boot = right_leg.addChild(
                "right_boot", ModelPartBuilder.create().uv(2, 33).cuboid(-3.0f, 10.0f, -3.0f, 6.0f, 3.0f, 6.0f, Dilation(-0.25f))
                    .uv(4, 21).cuboid(-2.5f, 6.5f, -2.5f, 5.0f, 6.0f, 5.0f, Dilation(-0.15f)), ModelTransform.origin(0.0f, 0.0f, 0.0f)
            )

            val right_feather_ribbon = right_boot.addChild(
                "right_feather_ribbon", ModelPartBuilder.create().uv(0, 11).cuboid(-3.5f, -1.0f, -3.5f, 7.0f, 2.0f, 7.0f, Dilation(-0.5f))
                    .uv(8, 0).cuboid(-3.4f, -4.5f, -2.5f, 1.0f, 5.0f, 5.0f, Dilation(-0.5f)), ModelTransform.of(0.0f, 9.0f, 0.0f, -0.0873f, 0.0f, 0.0f)
            )
            return TexturedModelData.of(modelData, 64, 64)
        }
    }

    override fun setAngles(renderState: S) {}
}