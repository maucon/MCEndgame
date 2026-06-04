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

class DruidsChestplateModel<S : HumanoidRenderState>(
    root: ModelPart
) : HumanoidModel<S>(root) {
    companion object {
        val MODEL_LAYER = ModelLayerLocation(IdentifierUtil.default("druids_chestplate"), "main")

        fun getTexturedModelData(): LayerDefinition {
            val modelData = MeshDefinition()
            val modelPartData = modelData.root

            val head = modelPartData.createEmptyChild(PartNames.HEAD)
            val hat = head.createEmptyChild(PartNames.HAT)
            val right_leg = modelPartData.createEmptyChild(PartNames.RIGHT_LEG)
            val left_leg = modelPartData.createEmptyChild(PartNames.LEFT_LEG)

            val body = modelPartData.createEmptyChild(PartNames.BODY)

            val chestplate =
                body.addOrReplaceChild("chestplate", CubeListBuilder.create(), PartPose.offset(0.0f, 0.0f, 0.0f))

            val chestplate_armor = chestplate.addOrReplaceChild(
                "chestplate_armor",
                CubeListBuilder.create().texOffs(40, 89).addBox(-4.5f, -0.25f, -2.75f, 9.0f, 5.0f, 5.0f, CubeDeformation(0.5f))
                    .texOffs(44, 105).addBox(-4.5f, 4.0f, 1.2f, 9.0f, 2.0f, 1.0f, CubeDeformation(0.5f)),
                PartPose.offset(0.0f, 0.0f, 0.0f)
            )

            val armor_bottom_front = chestplate_armor.addOrReplaceChild(
                "armor_bottom_front",
                CubeListBuilder.create().texOffs(44, 99).addBox(-4.5f, 0.5f, 0.5f, 9.0f, 5.0f, 1.0f, CubeDeformation(0.5f)),
                PartPose.offsetAndRotation(0.0f, 5.25f, -3.25f, 0.0873f, 0.0f, 0.0f)
            )

            val chestplate_base = chestplate.addOrReplaceChild(
                "chestplate_base",
                CubeListBuilder.create().texOffs(41, 108).addBox(-4.5f, -0.3f, -2.75f, 9.0f, 6.0f, 5.0f, CubeDeformation(0.3f))
                    .texOffs(42, 119).addBox(-4.0f, 6.1f, -2.0f, 8.0f, 5.0f, 4.0f, CubeDeformation(0.51f)),
                PartPose.offset(0.0f, 0.0f, 0.0f)
            )

            val left_arm =
                modelPartData.addOrReplaceChild(
                    PartNames.LEFT_ARM,
                    CubeListBuilder.create(),
                    PartPose.offset(5.0f, 2.0f, 0.0f)
                )

            val chestplate_left_arm = left_arm.addOrReplaceChild(
                "chestplate_left_arm",
                CubeListBuilder.create().texOffs(68, 91).addBox(-1.0f, -2.0f, -2.0f, 4.0f, 6.0f, 4.0f, CubeDeformation(0.4f))
                    .texOffs(68, 101).addBox(-1.0f, 4.0f, -2.0f, 4.0f, 6.0f, 4.0f, CubeDeformation(0.26f))
                    .texOffs(84, 101).addBox(-1.5f, 4.0f, -2.5f, 5.0f, 2.0f, 5.0f, CubeDeformation(-0.15f))
                    .texOffs(84, 108).addBox(-1.5f, 6.55f, -2.5f, 5.0f, 4.0f, 5.0f, CubeDeformation(-0.15f)),
                PartPose.offset(0.0f, 0.0f, 0.0f)
            )

            val shoulderpad_left = chestplate_left_arm.addOrReplaceChild(
                "shoulderpad_left",
                CubeListBuilder.create().texOffs(84, 93).addBox(0.0f, 0.5f, -2.0f, 3.0f, 4.0f, 4.0f, CubeDeformation(0.5f)),
                PartPose.offsetAndRotation(0.5f, -3.0f, 0.1f, 0.0f, 0.0436f, 0.0873f)
            )

            val right_arm =
                modelPartData.addOrReplaceChild(
                    PartNames.RIGHT_ARM,
                    CubeListBuilder.create(),
                    PartPose.offset(-5.0f, 2.0f, 0.0f)
                )

            val chestplate_right_arm = right_arm.addOrReplaceChild(
                "chestplate_right_arm",
                CubeListBuilder.create().texOffs(24, 91).addBox(-3.0f, -2.0f, -2.0f, 4.0f, 6.0f, 4.0f, CubeDeformation(0.4f))
                    .texOffs(24, 101).addBox(-3.0f, 4.0f, -2.0f, 4.0f, 6.0f, 4.0f, CubeDeformation(0.26f))
                    .texOffs(4, 101).addBox(-3.5f, 4.0f, -2.5f, 5.0f, 2.0f, 5.0f, CubeDeformation(-0.15f))
                    .texOffs(4, 108).addBox(-3.5f, 6.55f, -2.5f, 5.0f, 4.0f, 5.0f, CubeDeformation(-0.15f)),
                PartPose.offset(0.0f, 0.0f, 0.0f)
            )

            val shoulderpad_right = chestplate_right_arm.addOrReplaceChild(
                "shoulderpad_right",
                CubeListBuilder.create().texOffs(10, 93).addBox(-3.0f, 0.5f, -2.0f, 3.0f, 4.0f, 4.0f, CubeDeformation(0.5f)),
                PartPose.offsetAndRotation(-0.5f, -3.0f, 0.1f, 0.0f, -0.0436f, -0.0873f)
            )
            return LayerDefinition.create(modelData, 128, 128)
        }
    }

    override fun setupAnim(renderState: S) {}
}