package de.fuballer.mcendgame.client.component.entity.custom.entities.skeleton_mage

import de.fuballer.mcendgame.client.component.item.custom.ModelPartDataExtension.createEmptyChild
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import net.minecraft.client.model.geom.ModelLayerLocation
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.model.geom.PartPose
import net.minecraft.client.model.geom.builders.CubeDeformation
import net.minecraft.client.model.geom.builders.CubeListBuilder
import net.minecraft.client.model.geom.builders.LayerDefinition
import net.minecraft.client.model.geom.builders.MeshDefinition
import net.minecraft.client.model.monster.skeleton.SkeletonModel
import net.minecraft.client.renderer.entity.ArmorModelSet

class SkeletonMageModel(
    modelPart: ModelPart,
) : SkeletonModel<SkeletonMageRenderState>(modelPart) {
    companion object {
        private val IDENTIFIER = IdentifierUtil.default("skeleton_mage")
        val SKELETON_MAGE = ModelLayerLocation(IDENTIFIER, "main")
        val SKELETON_MAGE_INNER = ModelLayerLocation(IDENTIFIER, "inner")
        val SKELETON_MAGE_ARMOR: ArmorModelSet<ModelLayerLocation> =
            ArmorModelSet(
                ModelLayerLocation(IDENTIFIER, "helmet"),
                ModelLayerLocation(IDENTIFIER, "chestplate"),
                ModelLayerLocation(IDENTIFIER, "leggings"),
                ModelLayerLocation(IDENTIFIER, "boots")
            )

        fun createInnerLayer(): LayerDefinition {
            val mesh = MeshDefinition()
            val root = mesh.root
            val head = root.addOrReplaceChild(
                "head",
                CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, CubeDeformation(-0.25F)),
                PartPose.offset(0.0F, 0.0F, 0.0F)
            )
            head.createEmptyChild("hat")
            root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, CubeDeformation(-0.75F)), PartPose.offset(0.0F, 0.0F, 0.0F))
            root.createEmptyChild("right_arm")
            root.createEmptyChild("left_arm")
            root.createEmptyChild("right_leg")
            root.createEmptyChild("left_leg")
            return LayerDefinition.create(mesh, 64, 32)
        }
    }
}