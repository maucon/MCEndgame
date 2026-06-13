package de.fuballer.mcendgame.client.component.entity.custom.entities.skeleton_mage

import de.fuballer.mcendgame.main.component.entity.custom.entities.skeleton_mage.SkeletonMageEntity
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import net.minecraft.client.renderer.entity.AbstractSkeletonRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider

class SkeletonMageRenderer(
    context: EntityRendererProvider.Context
) : AbstractSkeletonRenderer<SkeletonMageEntity, SkeletonMageRenderState>(context, SkeletonMageModel.SKELETON_MAGE, SkeletonMageModel.SKELETON_MAGE_ARMOR) {
    companion object {
        val TEXTURE = IdentifierUtil.default("textures/entity/skeleton_mage/skeleton_mage.png")
        val INNER_TEXTURES = listOf(
               IdentifierUtil.default("textures/entity/skeleton_mage/skeleton_mage_inner.png"),
//            IdentifierUtil.default("textures/entity/skeleton_mage/skeleton_mage_inner_0.png"),
//            IdentifierUtil.default("textures/entity/skeleton_mage/skeleton_mage_inner_1.png"),
//            IdentifierUtil.default("textures/entity/skeleton_mage/skeleton_mage_inner_2.png"),
//            IdentifierUtil.default("textures/entity/skeleton_mage/skeleton_mage_inner_3.png"),
//            IdentifierUtil.default("textures/entity/skeleton_mage/skeleton_mage_inner_4.png"),
//            IdentifierUtil.default("textures/entity/skeleton_mage/skeleton_mage_inner_5.png"),
//            IdentifierUtil.default("textures/entity/skeleton_mage/skeleton_mage_inner_6.png"),
//            IdentifierUtil.default("textures/entity/skeleton_mage/skeleton_mage_inner_7.png"),
        )
    }

    init {
        addLayer(SkeletonMageInnerLayer(this, context.modelSet, SkeletonMageModel.SKELETON_MAGE_INNER, INNER_TEXTURES))
    }

    override fun getTextureLocation(state: SkeletonMageRenderState) = TEXTURE

    override fun createRenderState() = SkeletonMageRenderState()
}