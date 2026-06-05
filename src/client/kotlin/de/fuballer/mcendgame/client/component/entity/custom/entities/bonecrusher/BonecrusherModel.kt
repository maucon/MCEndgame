package de.fuballer.mcendgame.client.component.entity.custom.entities.bonecrusher

import com.geckolib.model.GeoModel
import com.geckolib.renderer.base.GeoRenderState
import de.fuballer.mcendgame.main.component.entity.custom.entities.bonecrusher.BonecrusherEntity
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil

class BonecrusherModel : GeoModel<BonecrusherEntity>() {
    companion object {
        val MODEL_IDENTIFIER = IdentifierUtil.default("entity/bonecrusher")
        val TEXTURE_IDENTIFIER = IdentifierUtil.default("textures/entity/bonecrusher/bonecrusher.png")
        val ANIMATION_IDENTIFIER = IdentifierUtil.default("entity/bonecrusher")
    }

    override fun getModelResource(renderState: GeoRenderState) = MODEL_IDENTIFIER

    override fun getTextureResource(renderState: GeoRenderState) = TEXTURE_IDENTIFIER

    override fun getAnimationResource(entity: BonecrusherEntity) = ANIMATION_IDENTIFIER
}