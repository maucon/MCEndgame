package de.fuballer.mcendgame.client.component.entity.custom.entities.scarred_one

import com.geckolib.model.GeoModel
import com.geckolib.renderer.base.GeoRenderState
import de.fuballer.mcendgame.main.component.entity.custom.entities.scarred_one.ScarredOneEntity
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil

class ScarredOneModel : GeoModel<ScarredOneEntity>() {
    companion object {
        val MODEL_IDENTIFIER = IdentifierUtil.default("entity/scarred_one")
        val TEXTURE_IDENTIFIER = IdentifierUtil.default("textures/entity/scarred_one/scarred_one.png")
        val ANIMATION_IDENTIFIER = IdentifierUtil.default("entity/scarred_one")
    }

    override fun getModelResource(renderState: GeoRenderState) = MODEL_IDENTIFIER

    override fun getTextureResource(renderState: GeoRenderState) = TEXTURE_IDENTIFIER

    override fun getAnimationResource(entity: ScarredOneEntity) = ANIMATION_IDENTIFIER
}