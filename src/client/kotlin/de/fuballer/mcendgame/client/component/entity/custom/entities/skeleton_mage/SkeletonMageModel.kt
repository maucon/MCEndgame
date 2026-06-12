package de.fuballer.mcendgame.client.component.entity.custom.entities.skeleton_mage

import com.geckolib.model.GeoModel
import com.geckolib.renderer.base.GeoRenderState
import de.fuballer.mcendgame.main.component.entity.custom.entities.skeleton_mage.SkeletonMageEntity
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil

class SkeletonMageModel : GeoModel<SkeletonMageEntity>() {
    companion object {
        val MODEL_IDENTIFIER = IdentifierUtil.default("entity/skeleton_mage")
        val TEXTURE_IDENTIFIER = IdentifierUtil.default("textures/entity/skeleton_mage/skeleton_mage.png")
        val ANIMATION_IDENTIFIER = IdentifierUtil.default("entity/skeleton_mage")
    }

    override fun getModelResource(renderState: GeoRenderState) = MODEL_IDENTIFIER

    override fun getTextureResource(renderState: GeoRenderState) = TEXTURE_IDENTIFIER

    override fun getAnimationResource(entity: SkeletonMageEntity) = ANIMATION_IDENTIFIER
}