package de.fuballer.mcendgame.client.component.entity.custom.entities.blood_queen

import com.geckolib.model.GeoModel
import com.geckolib.renderer.base.GeoRenderState
import de.fuballer.mcendgame.main.component.entity.custom.entities.blood_queen.BloodQueenEntity
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil

class BloodQueenModel : GeoModel<BloodQueenEntity>() {
    companion object {
        val MODEL_IDENTIFIER = IdentifierUtil.default("entity/blood_queen")
        val TEXTURE_IDENTIFIER = IdentifierUtil.default("textures/entity/blood_queen/blood_queen.png")
        val ANIMATION_IDENTIFIER = IdentifierUtil.default("entity/blood_queen")
    }

    override fun getModelResource(renderState: GeoRenderState) = MODEL_IDENTIFIER

    override fun getTextureResource(renderState: GeoRenderState) = TEXTURE_IDENTIFIER

    override fun getAnimationResource(entity: BloodQueenEntity) = ANIMATION_IDENTIFIER
}