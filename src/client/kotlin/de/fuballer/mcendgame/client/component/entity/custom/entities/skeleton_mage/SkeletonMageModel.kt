package de.fuballer.mcendgame.client.component.entity.custom.entities.skeleton_mage

import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import net.minecraft.client.model.geom.ModelLayerLocation
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.model.monster.skeleton.SkeletonModel

class SkeletonMageModel(
    modelPart: ModelPart,
) : SkeletonModel<SkeletonMageRenderState>(modelPart) {
    companion object {
        val SKELETON_MAGE = ModelLayerLocation(IdentifierUtil.default("skeleton_mage"), "main")
    }
}