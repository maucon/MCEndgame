package de.fuballer.mcendgame.client.component.entity.custom.entities.skeleton_mage

import com.geckolib.animatable.GeoAnimatable
import com.geckolib.renderer.base.GeoRenderState
import com.geckolib.renderer.base.GeoRenderer
import com.geckolib.renderer.base.RenderPassInfo
import com.geckolib.renderer.layer.builtin.ItemArmorGeoLayer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.state.HumanoidRenderState
import net.minecraft.world.entity.LivingEntity

class SkeletonMageArmorLayer<T, O, R>(
    geoRenderer: GeoRenderer<T, O, R>,
    context: EntityRendererProvider.Context
) : ItemArmorGeoLayer<T, O, R>(geoRenderer, context) where T : LivingEntity, T : GeoAnimatable, O : Any, R : HumanoidRenderState, R : GeoRenderState {
    companion object {
        val DATA = listOf(
            RenderData.head("head"),

            RenderData.body("body"),

            RenderData.leftArm("left_arm"),
            RenderData.rightArm("right_arm"),

            RenderData.leftLeg("left_leg"),
            RenderData.rightLeg("right_leg"),

            RenderData.leftFoot("left_leg"),
            RenderData.rightFoot("right_leg"),
        )
    }

    override fun getRelevantBones(renderPassInfo: RenderPassInfo<R>): List<RenderData> = DATA

    // doesn't work if the original state is used ¯\_(ツ)_/¯
    override fun <S> getOrCreateHumanoidRenderState(renderState: R, forceNew: Boolean): S where S : HumanoidRenderState, S : GeoRenderState =
        super.getOrCreateHumanoidRenderState(renderState, true)
}