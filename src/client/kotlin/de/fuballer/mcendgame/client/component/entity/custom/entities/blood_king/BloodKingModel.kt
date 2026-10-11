package de.fuballer.mcendgame.client.component.entity.custom.entities.blood_king

import com.geckolib.model.GeoModel
import com.geckolib.renderer.base.GeoRenderState
import de.fuballer.mcendgame.main.component.entity.custom.entities.blood_king.BloodKingEntity
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil

class BloodKingModel : GeoModel<BloodKingEntity>() {
    companion object {
        val MODEL_IDENTIFIER = IdentifierUtil.default("entity/blood_king")
        val TEXTURE_IDENTIFIER = IdentifierUtil.default("textures/entity/blood_king/blood_king.png")
        val ANIMATION_IDENTIFIER = IdentifierUtil.default("entity/blood_king")
    }

    override fun getModelResource(renderState: GeoRenderState) = MODEL_IDENTIFIER

    override fun getTextureResource(renderState: GeoRenderState) = TEXTURE_IDENTIFIER

    override fun getAnimationResource(entity: BloodKingEntity) = ANIMATION_IDENTIFIER
}