package de.fuballer.mcendgame.client.component.entity.custom.entities.beastweaver

import com.geckolib.model.GeoModel
import com.geckolib.renderer.base.GeoRenderState
import de.fuballer.mcendgame.main.component.entity.custom.entities.beastweaver.BeastweaverEntity
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil

class BeastweaverModel : GeoModel<BeastweaverEntity>() {
    companion object {
        val MODEL_IDENTIFIER = IdentifierUtil.default("entity/beastweaver")
        val TEXTURE_IDENTIFIER = IdentifierUtil.default("textures/entity/beastweaver/beastweaver.png")
        val ANIMATION_IDENTIFIER = IdentifierUtil.default("entity/beastweaver")
    }

    override fun getModelResource(renderState: GeoRenderState) = MODEL_IDENTIFIER

    override fun getTextureResource(renderState: GeoRenderState) = TEXTURE_IDENTIFIER

    override fun getAnimationResource(entity: BeastweaverEntity) = ANIMATION_IDENTIFIER
}