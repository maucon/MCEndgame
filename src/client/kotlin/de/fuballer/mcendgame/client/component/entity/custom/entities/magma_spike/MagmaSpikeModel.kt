package de.fuballer.mcendgame.client.component.entity.custom.entities.magma_spike

import com.geckolib.model.GeoModel
import com.geckolib.renderer.base.GeoRenderState
import de.fuballer.mcendgame.main.component.entity.custom.entities.magma_spike.MagmaSpikeEntity
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil

class MagmaSpikeModel : GeoModel<MagmaSpikeEntity>() {
    companion object {
        val MODEL_IDENTIFIER = IdentifierUtil.default("entity/magma_spike")
        val TEXTURE_IDENTIFIER = IdentifierUtil.default("textures/entity/magma_spike/magma_spike.png")
        val ANIMATION_IDENTIFIER = IdentifierUtil.default("entity/magma_spike")
    }

    override fun getModelResource(renderState: GeoRenderState) = MODEL_IDENTIFIER

    override fun getTextureResource(renderState: GeoRenderState) = TEXTURE_IDENTIFIER

    override fun getAnimationResource(entity: MagmaSpikeEntity) = ANIMATION_IDENTIFIER
}