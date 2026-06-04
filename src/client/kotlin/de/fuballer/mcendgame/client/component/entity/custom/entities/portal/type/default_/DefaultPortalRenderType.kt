package de.fuballer.mcendgame.client.component.entity.custom.entities.portal.type.default_

import de.fuballer.mcendgame.client.component.entity.custom.entities.portal.PortalRenderState
import de.fuballer.mcendgame.client.component.entity.custom.entities.portal.PortalRenderer
import de.fuballer.mcendgame.client.component.entity.custom.entities.portal.type.PortalRenderType
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import net.minecraft.client.model.EntityModel
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.rendertype.RenderType
import net.minecraft.client.renderer.rendertype.RenderTypes
import net.minecraft.resources.Identifier

class DefaultPortalRenderType : PortalRenderType {
    companion object {
        const val ID = "default"
        private const val CHANGE_TEXTURE_TICKS = 2

        private val TEXTURES: Array<Identifier> = Array(31) { i ->
            IdentifierUtil.default("textures/entity/portal/default/portal_$i.png")
        }
    }

    override fun getId() = ID

    override fun getTexture(age: Float): Identifier {
        val tick = (age / CHANGE_TEXTURE_TICKS).toInt() % TEXTURES.size
        return TEXTURES[tick]
    }

    override fun getShadowRadius(): Float {
        return 0.2f
    }

    override fun getModel(context: EntityRendererProvider.Context): EntityModel<PortalRenderState> {
        return DefaultPortalEntityModel(context.bakeLayer(DefaultPortalEntityModel.PORTAL))
    }

    override fun getRenderLayer(renderer: PortalRenderer, state: PortalRenderState, showBody: Boolean, translucent: Boolean, showOutline: Boolean): RenderType {
        return RenderTypes.entityTranslucent(getTexture(state.ageInTicks), false)
    }
}