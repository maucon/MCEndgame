package de.fuballer.mcendgame.client.component.item.custom.armor.model.windstrider

import de.fuballer.mcendgame.client.component.item.custom.ModelPartDataExtension.createEmptyChild
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import net.minecraft.client.model.*
import net.minecraft.client.render.entity.model.BipedEntityModel
import net.minecraft.client.render.entity.model.EntityModelLayer
import net.minecraft.client.render.entity.model.EntityModelPartNames
import net.minecraft.client.render.entity.state.BipedEntityRenderState

class WindstriderModel<S : BipedEntityRenderState>(
    root: ModelPart
) : BipedEntityModel<S>(root) {
    companion object {
        val MODEL_LAYER = EntityModelLayer(IdentifierUtil.default("windstrider"), "main")

        fun getTexturedModelData(): TexturedModelData {
            val modelData = ModelData()
            val modelPartData = modelData.root

            val head = modelPartData.createEmptyChild(EntityModelPartNames.HEAD)
            val hat = head.createEmptyChild(EntityModelPartNames.HAT)
            val left_arm = modelPartData.createEmptyChild(EntityModelPartNames.LEFT_ARM)
            val right_arm = modelPartData.createEmptyChild(EntityModelPartNames.RIGHT_ARM)

            val body = modelPartData.addChild(EntityModelPartNames.BODY, ModelPartBuilder.create(), ModelTransform.origin(0.0f, 0.0f, 0.0f))

            val leggings_waist =
                body.addChild("leggings_waist", ModelPartBuilder.create().uv(9, 0).cuboid(-4.0f, 7.0f, -2.0f, 8.0f, 5.0f, 4.0f, Dilation(0.09f)), ModelTransform.origin(0.0f, 0.0f, 0.0f))

            val left_leg = modelPartData.addChild(EntityModelPartNames.LEFT_LEG, ModelPartBuilder.create(), ModelTransform.origin(2.0f, 12.0f, 0.0f))

            val left_leggings = left_leg.addChild(
                "left_leggings", ModelPartBuilder.create().uv(24, 28).cuboid(-2.0f, -0.9f, -2.045f, 4.0f, 11.0f, 4.0f, Dilation(0.05f))
                    .uv(22, 10).cuboid(-2.5f, -1.0f, -2.55f, 5.0f, 12.0f, 5.0f, Dilation(0.05f)), ModelTransform.origin(0.0f, 0.0f, 0.0f)
            )

            val right_leg = modelPartData.addChild(EntityModelPartNames.RIGHT_LEG, ModelPartBuilder.create(), ModelTransform.origin(-2.0f, 12.0f, 0.0f))

            val right_leggings = right_leg.addChild(
                "right_leggings", ModelPartBuilder.create().uv(2, 28).cuboid(-2.0f, -0.9f, -2.05f, 4.0f, 11.0f, 4.0f, Dilation(0.05f))
                    .uv(0, 10).cuboid(-2.5f, -1.0f, -2.5f, 5.0f, 12.0f, 5.0f, Dilation(0.05f)), ModelTransform.origin(0.0f, 0.0f, 0.0f)
            )
            return TexturedModelData.of(modelData, 64, 64)
        }
    }

    override fun setAngles(renderState: S) {}
}