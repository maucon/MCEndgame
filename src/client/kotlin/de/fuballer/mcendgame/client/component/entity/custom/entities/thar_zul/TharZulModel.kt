package de.fuballer.mcendgame.client.component.entity.custom.entities.thar_zul

import com.geckolib.model.GeoModel
import com.geckolib.renderer.base.GeoRenderState
import de.fuballer.mcendgame.main.component.entity.custom.entities.thar_zul.TharZulEntity
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil

class TharZulModel : GeoModel<TharZulEntity>() {
    companion object {
        val MODEL_IDENTIFIER = IdentifierUtil.default("entity/thar_zul")
        val TEXTURE_IDENTIFIER = IdentifierUtil.default("textures/entity/thar_zul/thar_zul.png")
        val ANIMATION_IDENTIFIER = IdentifierUtil.default("entity/thar_zul")
    }

    override fun getModelResource(renderState: GeoRenderState) = MODEL_IDENTIFIER

    override fun getTextureResource(renderState: GeoRenderState) = TEXTURE_IDENTIFIER

    override fun getAnimationResource(entity: TharZulEntity) = ANIMATION_IDENTIFIER
}