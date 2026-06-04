package de.fuballer.mcendgame.client.component.entity.custom.entities.portal.type

import de.fuballer.mcendgame.client.component.entity.custom.entities.portal.PortalRenderState
import de.fuballer.mcendgame.client.component.entity.custom.entities.portal.PortalRenderer
import de.fuballer.mcendgame.client.component.entity.custom.entities.portal.type.default_.DefaultPortalRenderType
import de.fuballer.mcendgame.client.component.entity.custom.entities.portal.type.legacy.LegacyPortalRenderType
import net.minecraft.client.model.EntityModel
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.rendertype.RenderType
import net.minecraft.resources.Identifier

interface PortalRenderType {
    fun getId(): String
    fun getTexture(age: Float): Identifier
    fun getShadowRadius(): Float
    fun getModel(context: EntityRendererProvider.Context): EntityModel<PortalRenderState>
    fun getRenderLayer(renderer: PortalRenderer, state: PortalRenderState, showBody: Boolean, translucent: Boolean, showOutline: Boolean): RenderType? = null

    companion object {
        private val PORTAL_TYPES = mapOf(
            LegacyPortalRenderType.ID to LegacyPortalRenderType()
        )

        fun getType(typeName: String): PortalRenderType {
            return PORTAL_TYPES[typeName]
                ?: DefaultPortalRenderType()
        }
    }
}