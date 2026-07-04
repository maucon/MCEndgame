package de.fuballer.mcendgame.client.component.entity.custom.entities.beastweaver

import com.geckolib.constant.DataTickets
import com.geckolib.constant.dataticket.DataTicket
import com.geckolib.renderer.GeoEntityRenderer
import com.geckolib.renderer.base.BoneSnapshots
import com.geckolib.renderer.base.GeoRenderState
import com.geckolib.renderer.base.RenderPassInfo
import com.google.common.reflect.TypeToken
import de.fuballer.mcendgame.main.component.entity.custom.entities.beastweaver.BeastweaverEntity
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState
import kotlin.math.PI

class BeastweaverRenderer<R>(
    context: EntityRendererProvider.Context
) : GeoEntityRenderer<BeastweaverEntity, R>(context, BeastweaverModel()) where R : LivingEntityRenderState, R : GeoRenderState {
    init {
        withRenderLayer(
            CustomBonesProgressingTextureGeoLayer(
                this,
                listOf(
                    "leftArmBand",
                    "rightArmBand",
                    "belt",
                    "leftBootBone",
                    "rightBootBone",
                ),
                { renderState -> renderState.getGeckolibData(TRANSFORM_PROGRESS) ?: 0F },
                mapOf(
                    0F to IdentifierUtil.default("textures/entity/beastweaver/beastweaver_leather_0.png"),
                    0.75F to IdentifierUtil.default("textures/entity/beastweaver/beastweaver_leather_1.png"),
                    0.85F to IdentifierUtil.default("textures/entity/beastweaver/beastweaver_leather_2.png"),
                ),
                activeThreshold = 0.75F,
            )
        )
        withRenderLayer(
            CustomBonesProgressingTextureGeoLayer(
                this,
                listOf(
                    "chestSkin",
                    "breastSkin",
                ),
                { renderState -> renderState.getGeckolibData(TRANSFORM_PROGRESS) ?: 0F },
                mapOf(
                    0F to IdentifierUtil.default("textures/entity/beastweaver/beastweaver_upper_body_0.png"),
                    0.55F to IdentifierUtil.default("textures/entity/beastweaver/beastweaver_upper_body_1.png"),
                    0.75F to IdentifierUtil.default("textures/entity/beastweaver/beastweaver_upper_body_2.png"),
                    0.95F to IdentifierUtil.default("textures/entity/beastweaver/beastweaver_upper_body_3.png"),
                ),
                activeThreshold = 0.55F,
            )
        )
        withRenderLayer(
            CustomBonesProgressingTextureGeoLayer(
                this,
                listOf(
                    "leftLegUpperSkin",
                    "rightLegUpperSkin",
                    "leftLegLowerSkin",
                    "rightLegLowerSkin",
                ),
                { renderState -> renderState.getGeckolibData(TRANSFORM_PROGRESS) ?: 0F },
                mapOf(
                    0F to IdentifierUtil.default("textures/entity/beastweaver/beastweaver_legs_0.png"),
                    0.85F to IdentifierUtil.default("textures/entity/beastweaver/beastweaver_legs_3.png"),
                ),
                activeThreshold = 0.85F,
            )
        )
    }

    companion object {
        private val HIDDEN_BONES = DataTicket.create("hidden_bones", object : TypeToken<Set<String>>() {})
        private val TRANSFORM_PROGRESS = DataTicket.create("transform_progress", object : TypeToken<Float>() {})
    }

    override fun addRenderData(animatable: BeastweaverEntity, relatedObject: Void?, renderState: R, partialTick: Float) {
        super.addRenderData(animatable, relatedObject, renderState, partialTick)

        renderState.addGeckolibData(HIDDEN_BONES, animatable.getHiddenBones())
        renderState.addGeckolibData(TRANSFORM_PROGRESS, animatable.getTransformProgress(partialTick))
    }

    override fun adjustModelBonesForRender(renderPassInfo: RenderPassInfo<R>, snapshots: BoneSnapshots) {
        super.adjustModelBonesForRender(renderPassInfo, snapshots)

        snapshots.get("head").ifPresent {
            var pitch = renderPassInfo.getGeckolibData(DataTickets.ENTITY_PITCH)!!
            pitch = Math.clamp(pitch, -35F, 35F)
            it.rotX = -pitch * PI.toFloat() / 180F

            var yaw = renderPassInfo.getGeckolibData(DataTickets.ENTITY_YAW)!!
            yaw = Math.clamp(yaw, -45F, 45F)
            it.rotY = -yaw * PI.toFloat() / 180F
        }

        val toHide = renderPassInfo.getGeckolibData(HIDDEN_BONES) ?: return
        toHide.forEach { name ->
            snapshots.get(name).ifPresent {
                it.skipRender(true)
                it.skipChildrenRender(true)
            }
        }
    }
}