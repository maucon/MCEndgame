package de.fuballer.mcendgame.client.component.entity.custom.entities.beastweaver

import com.geckolib.animatable.GeoAnimatable
import com.geckolib.constant.DataTickets
import com.geckolib.renderer.base.GeoRenderState
import com.geckolib.renderer.base.GeoRenderer
import de.fuballer.mcendgame.client.component.render.geo_layers.CustomBonesProgressingTextureGeoLayer
import de.fuballer.mcendgame.main.util.ColorUtil
import net.minecraft.client.renderer.rendertype.RenderType
import net.minecraft.client.renderer.rendertype.RenderTypes
import net.minecraft.resources.Identifier

class BeastweaverSpiritAttackGeoLayer<T : GeoAnimatable, O : Any, R : GeoRenderState>(
    renderer: GeoRenderer<T, O, R>,
    bones: List<String>,
    progress: (R) -> Float,
    textures: Map<Float, Identifier>,
    val alpha: (Float) -> Float = { 1F },
    baseTexture: Identifier = textures[textures.keys.first()]!!,
    val active: (R) -> Boolean = { true },
) : CustomBonesProgressingTextureGeoLayer<T, O, R>(renderer, bones, progress, textures, baseTexture) {
    override fun getRenderType(renderState: R, texture: Identifier): RenderType {
        return RenderTypes.entityTranslucentEmissive(texture)
    }

    override fun isActive(renderState: R) = active(renderState)

    override fun addRenderData(animatable: T, relatedObject: O?, renderState: R, partialTick: Float) {
        val baseColor = renderState.getGeckolibData(DataTickets.RENDER_COLOR) ?: ColorUtil.rgbaToInt(255, 255, 255, 255)
        val newColor = ColorUtil.multiplyAlpha(baseColor, alpha(progress(renderState)).coerceIn(0F, 1F))
        renderState.addGeckolibData(DataTickets.RENDER_COLOR, newColor)
    }
}