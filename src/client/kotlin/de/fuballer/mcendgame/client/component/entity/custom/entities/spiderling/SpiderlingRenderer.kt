package de.fuballer.mcendgame.client.component.entity.custom.entities.spiderling

import de.fuballer.mcendgame.main.component.entity.custom.entities.spiderling.SpiderlingEntity
import net.minecraft.client.model.geom.ModelLayers
import net.minecraft.client.model.monster.spider.SpiderModel
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.MobRenderer
import net.minecraft.client.renderer.entity.layers.SpiderEyesLayer
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState
import net.minecraft.resources.Identifier

class SpiderlingRenderer(
    context: EntityRendererProvider.Context,
) : MobRenderer<SpiderlingEntity, LivingEntityRenderState, SpiderModel>(
    context,
    SpiderModel(context.bakeLayer(ModelLayers.SPIDER)),
    0.8f,
) {
    init {
        addLayer(SpiderEyesLayer(this))
    }

    companion object {
        val TEXTURE: Identifier = Identifier.withDefaultNamespace("textures/entity/spider/spider.png")
    }

    override fun getTextureLocation(state: LivingEntityRenderState) = TEXTURE

    override fun createRenderState() = LivingEntityRenderState()

    override fun getFlipDegrees() = 180.0f
}