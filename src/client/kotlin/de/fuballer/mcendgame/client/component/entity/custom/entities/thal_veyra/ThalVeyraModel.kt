package de.fuballer.mcendgame.client.component.entity.custom.entities.thal_veyra

import com.geckolib.model.GeoModel
import com.geckolib.renderer.base.GeoRenderState
import de.fuballer.mcendgame.main.component.entity.custom.entities.demonkin.thal_veyra.ThalVeyraEntity
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil

class ThalVeyraModel : GeoModel<ThalVeyraEntity>() {
    companion object {
        val MODEL_IDENTIFIER = IdentifierUtil.default("entity/thal_veyra")
        val TEXTURE_IDENTIFIER = IdentifierUtil.default("textures/entity/thal_veyra/thal_veyra.png")
        val ANIMATION_IDENTIFIER = IdentifierUtil.default("entity/thal_veyra")
    }

    override fun getModelResource(renderState: GeoRenderState) = MODEL_IDENTIFIER

    override fun getTextureResource(renderState: GeoRenderState) = TEXTURE_IDENTIFIER

    override fun getAnimationResource(entity: ThalVeyraEntity) = ANIMATION_IDENTIFIER
}