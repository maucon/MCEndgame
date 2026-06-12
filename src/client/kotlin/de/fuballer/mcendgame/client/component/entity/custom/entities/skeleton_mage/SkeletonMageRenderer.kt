package de.fuballer.mcendgame.client.component.entity.custom.entities.skeleton_mage

import de.fuballer.mcendgame.main.component.entity.custom.entities.skeleton_mage.SkeletonMageEntity
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import net.minecraft.client.model.geom.ModelLayers
import net.minecraft.client.renderer.entity.AbstractSkeletonRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider

class SkeletonMageRenderer(
    context: EntityRendererProvider.Context
) : AbstractSkeletonRenderer<SkeletonMageEntity, SkeletonMageRenderState>(context, ModelLayers.SKELETON, ModelLayers.SKELETON_ARMOR) {
    companion object {
        val TEXTURE = IdentifierUtil.default("textures/entity/skeleton_mage/skeleton_mage.png")
    }

    override fun getTextureLocation(state: SkeletonMageRenderState) = TEXTURE

    override fun createRenderState() = SkeletonMageRenderState()
}