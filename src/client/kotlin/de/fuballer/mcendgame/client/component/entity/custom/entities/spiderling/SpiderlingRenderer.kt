package de.fuballer.mcendgame.client.component.entity.custom.entities.spiderling

import de.fuballer.mcendgame.main.component.entity.custom.entities.spiderling.SpiderlingEntity
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.MobEntityRenderer
import net.minecraft.client.render.entity.feature.SpiderEyesFeatureRenderer
import net.minecraft.client.render.entity.model.EntityModelLayers
import net.minecraft.client.render.entity.model.SpiderEntityModel
import net.minecraft.client.render.entity.state.LivingEntityRenderState
import net.minecraft.util.Identifier

class SpiderlingRenderer(
    context: EntityRendererFactory.Context,
) : MobEntityRenderer<SpiderlingEntity, LivingEntityRenderState, SpiderEntityModel>(
    context,
    SpiderEntityModel(context.getPart(EntityModelLayers.SPIDER)),
    0.8f,
) {
    init {
        addFeature(SpiderEyesFeatureRenderer(this))
    }

    companion object {
        val TEXTURE: Identifier = Identifier.ofVanilla("textures/entity/spider/spider.png")
    }

    override fun getTexture(state: LivingEntityRenderState) = TEXTURE

    override fun createRenderState() = LivingEntityRenderState()

    override fun getLyingPositionRotationDegrees() = 180.0f
}