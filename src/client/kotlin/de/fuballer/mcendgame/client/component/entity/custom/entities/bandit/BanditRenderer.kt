package de.fuballer.mcendgame.client.component.entity.custom.entities.bandit

import de.fuballer.mcendgame.main.component.entity.custom.entities.bandit.BanditEntity
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import net.minecraft.client.model.geom.ModelLayers
import net.minecraft.client.renderer.entity.ArmorModelSet
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.HumanoidMobRenderer
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer
import net.minecraft.resources.Identifier

class BanditRenderer(
    context: EntityRendererProvider.Context,
    slim: Boolean,
) : HumanoidMobRenderer<BanditEntity, BanditRenderState, BanditModel>(
    context,
    BanditModel(context.bakeLayer(if (slim) ModelLayers.PLAYER_SLIM else ModelLayers.PLAYER), slim),
    0.5f,
) {
    companion object {
        val TEXTURE: Identifier = IdentifierUtil.default("textures/entity/bandit/gommehd.png")
    }

    init {
        addLayer(
            HumanoidArmorLayer(
                this,
                ArmorModelSet.bake(
                    if (slim) ModelLayers.PLAYER_SLIM_ARMOR else ModelLayers.PLAYER_ARMOR,
                    context.modelSet,
                ) { part -> BanditModel(part, slim) },
                context.equipmentRenderer,
            )
        )
    }

    override fun getTextureLocation(state: BanditRenderState) = TEXTURE

    override fun createRenderState() = BanditRenderState()

    override fun extractRenderState(entity: BanditEntity, state: BanditRenderState, partialTicks: Float) {
        super.extractRenderState(entity, state, partialTicks)
        state.modelType = entity.modelType
    }
}