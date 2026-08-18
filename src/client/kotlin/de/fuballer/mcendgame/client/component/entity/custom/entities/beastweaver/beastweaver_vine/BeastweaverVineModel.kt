package de.fuballer.mcendgame.client.component.entity.custom.entities.beastweaver.beastweaver_vine

import com.geckolib.model.GeoModel
import com.geckolib.renderer.base.GeoRenderState
import de.fuballer.mcendgame.main.component.entity.custom.entities.beastweaver.beastweaver_vine.BeastweaverVineEntity
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil

class BeastweaverVineModel : GeoModel<BeastweaverVineEntity>() {
    companion object {
        val MODEL_IDENTIFIER = IdentifierUtil.default("entity/beastweaver_vine")
        val TEXTURE_IDENTIFIER = IdentifierUtil.default("textures/entity/beastweaver/beastweaver_vine/beastweaver_vine.png")
        val ANIMATION_IDENTIFIER = IdentifierUtil.default("entity/beastweaver_vine")
    }

    override fun getModelResource(renderState: GeoRenderState) = MODEL_IDENTIFIER

    override fun getTextureResource(renderState: GeoRenderState) = TEXTURE_IDENTIFIER

    override fun getAnimationResource(entity: BeastweaverVineEntity) = ANIMATION_IDENTIFIER
}