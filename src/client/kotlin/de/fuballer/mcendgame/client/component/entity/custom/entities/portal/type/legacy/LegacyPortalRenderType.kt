package de.fuballer.mcendgame.client.component.entity.custom.entities.portal.type.legacy

import de.fuballer.mcendgame.client.component.entity.custom.entities.portal.PortalRenderState
import de.fuballer.mcendgame.client.component.entity.custom.entities.portal.type.PortalRenderType
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import net.minecraft.client.model.EntityModel
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.resources.Identifier

class LegacyPortalRenderType : PortalRenderType {
    companion object {
        const val ID = "legacy"

        private val TEXTURE = IdentifierUtil.default("textures/entity/portal/legacy/portal.png")
    }

    override fun getId() = ID

    override fun getTexture(age: Float): Identifier = TEXTURE

    override fun getShadowRadius(): Float {
        return 0.3f
    }

    override fun getModel(context: EntityRendererProvider.Context): EntityModel<PortalRenderState> {
        return LegacyPortalEntityModel(context.bakeLayer(LegacyPortalEntityModel.PORTAL))
    }
}