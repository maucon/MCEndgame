package de.fuballer.mcendgame.client.component.entity.custom.entities.beastweaver

import com.geckolib.constant.DataTickets
import com.geckolib.constant.dataticket.DataTicket
import com.geckolib.renderer.GeoEntityRenderer
import com.geckolib.renderer.base.BoneSnapshots
import com.geckolib.renderer.base.GeoRenderState
import com.geckolib.renderer.base.RenderPassInfo
import com.google.common.reflect.TypeToken
import de.fuballer.mcendgame.client.component.render.geo_layers.CustomBonesProgressingTextureGeoLayer
import de.fuballer.mcendgame.main.component.entity.custom.entities.beastweaver.BeastweaverEntity
import de.fuballer.mcendgame.main.util.extension.FloatExtension.clampedLerp
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState
import org.joml.Vector3f
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
                    0.4F to IdentifierUtil.default("textures/entity/beastweaver/beastweaver_legs_1.png"),
                    0.55F to IdentifierUtil.default("textures/entity/beastweaver/beastweaver_legs_2.png"),
                    0.75F to IdentifierUtil.default("textures/entity/beastweaver/beastweaver_legs_3.png"),
                ),
                activeThreshold = 0.4F,
            )
        )
        withRenderLayer(
            CustomBonesProgressingTextureGeoLayer(
                this,
                listOf(
                    "rightArmUpper",
                    "rightArmUpperLower",
                    "leftArmUpper",
                    "leftArmUpperLower",
                ),
                { renderState -> renderState.getGeckolibData(SHOULDER_SPIKES_ANIM_TIME) ?: 0F },
                mapOf(
                    0F to IdentifierUtil.default("textures/entity/beastweaver/beastweaver_shoulder_0.png"),
                    0.71F to IdentifierUtil.default("textures/entity/beastweaver/beastweaver_shoulder_1.png"),
                ),
                activeThreshold = 0.71F,
            )
        )

        val getCameraRelativeEntityPos: (BeastweaverEntity, Float) -> Vector3f = { beastweaver, partialTick ->
            val camera = Minecraft.getInstance().gameRenderer.mainCamera
            val entityPos = beastweaver.getPosition(partialTick)
            val cameraPos = camera.position()
            Vector3f(
                (entityPos.x - cameraPos.x).toFloat(),
                (entityPos.y - cameraPos.y).toFloat(),
                (entityPos.z - cameraPos.z).toFloat()
            )
        }

        val getCameraRelativeEntityEyePos: (BeastweaverEntity, Float) -> Vector3f = { beastweaver, partialTick ->
            getCameraRelativeEntityPos(beastweaver, partialTick).add(0f, beastweaver.eyeHeight, 0f)
        }

        val getBearSwipeAttackGradientBounds: (Float) -> Pair<Float, Float> = { progress ->
            when {
                progress < 0.1f -> Pair(-0.1f, 0f)

                progress < 0.79f -> {
                    val t = (progress - 0.1f) / (0.79f - 0.1f)
                    val min = ((t - 0.35f) / 0.65f).clampedLerp(0f, 2.2f)
                    val max = t.clampedLerp(0f, 2.3f)
                    Pair(min, max)
                }

                progress < 1.08f -> Pair(2.5f, 2.6f)

                else -> {
                    val t = (progress - 1.08f) / (1.38f - 1.08f)
                    val min = t.clampedLerp(2.5f, -0.1f)
                    val max = ((t - 0.35f) / 0.65f).clampedLerp(2.6f, 0f)
                    Pair(min, max)
                }
            }
        }

        withRenderLayer(
            BeastweaverSpiritAttackGeoLayer(
                this,
                listOf("rightBearPaw"),
                progress = { renderState -> renderState.getGeckolibData(CURRENT_ATTACK_ANIM_TIME) ?: 0F },
                textures = mapOf(0F to IdentifierUtil.default("textures/entity/beastweaver/beastweaver_bear_paw_right_0.png")),
                getGradientOrigin = getCameraRelativeEntityEyePos,
                getGradientBounds = getBearSwipeAttackGradientBounds,
                active = { renderState -> renderState.getGeckolibData(CURRENT_ATTACK_NAME) == "attack.bear_swipe_right" },
            )
        )
        withRenderLayer(
            BeastweaverSpiritAttackGeoLayer(
                this,
                listOf("leftBearPaw"),
                progress = { renderState -> renderState.getGeckolibData(CURRENT_ATTACK_ANIM_TIME) ?: 0F },
                textures = mapOf(0F to IdentifierUtil.default("textures/entity/beastweaver/beastweaver_bear_paw_left_0.png")),
                getGradientOrigin = getCameraRelativeEntityEyePos,
                getGradientBounds = getBearSwipeAttackGradientBounds,
                active = { renderState -> renderState.getGeckolibData(CURRENT_ATTACK_NAME) == "attack.bear_swipe_left" },
            )
        )

        val getTailSweepAttackGradientBounds: (Float) -> Pair<Float, Float> = { progress ->
            when {
                progress < 0.7f -> {
                    val t = progress / 0.7f
                    val min = ((t - 0.35f) / 0.65f).clampedLerp(0f, 5f)
                    val max = t.clampedLerp(0f, 5.1f)
                    Pair(min, max)
                }

                progress < 1.2f -> Pair(5f, 5.1f)

                else -> {
                    val t = (progress - 1.2f) / (1.49f - 1.2f)
                    val min = t.clampedLerp(5f, -0.1f)
                    val max = ((t - 0.35f) / 0.65f).clampedLerp(5.1f, 0f)
                    Pair(min, max)
                }
            }
        }

        withRenderLayer(
            BeastweaverSpiritAttackGeoLayer(
                this,
                listOf("dragonTail0"),
                progress = { renderState -> renderState.getGeckolibData(CURRENT_ATTACK_ANIM_TIME) ?: 0F },
                textures = mapOf(0F to IdentifierUtil.default("textures/entity/beastweaver/beastweaver_dragon_tail_0.png")),
                getGradientOrigin = { beastweaver, partialTick -> getCameraRelativeEntityPos(beastweaver, partialTick).add(0f, beastweaver.eyeHeight * 0.5f, 0f) },
                getGradientBounds = getTailSweepAttackGradientBounds,
                active = { renderState -> renderState.getGeckolibData(CURRENT_ATTACK_NAME) == "attack.tail_sweep" },
            )
        )

        val getWingsLaunchAttackGradientBounds: (Float) -> Pair<Float, Float> = { progress ->
            when {
                progress < 0.8f -> {
                    val t = progress / 0.8f
                    val min = ((t - 0.35f) / 0.65f).clampedLerp(0f, 3.5f)
                    val max = t.clampedLerp(0f, 3.6f)
                    Pair(min, max)
                }

                progress < 1.4f -> Pair(3.5f, 3.6f)

                else -> {
                    val t = (progress - 1.4f) / (1.75f - 1.4f)
                    val min = t.clampedLerp(3.5f, -0.1f)
                    val max = ((t - 0.35f) / 0.65f).clampedLerp(3.6f, 0f)
                    Pair(min, max)
                }
            }
        }

        withRenderLayer(
            BeastweaverSpiritAttackGeoLayer(
                this,
                listOf("wings"),
                progress = { renderState -> renderState.getGeckolibData(CURRENT_ATTACK_ANIM_TIME) ?: 0F },
                textures = mapOf(0F to IdentifierUtil.default("textures/entity/beastweaver/beastweaver_wings_0.png")),
                getGradientOrigin = { beastweaver, partialTick -> getCameraRelativeEntityPos(beastweaver, partialTick).add(0f, beastweaver.eyeHeight * 0.5f, 0f) },
                getGradientBounds = getWingsLaunchAttackGradientBounds,
                active = { renderState -> renderState.getGeckolibData(CURRENT_ATTACK_NAME) == "attack.wings_launch" },
            )
        )

        val getElephantStompAttackGradientBounds: (Float) -> Pair<Float, Float> = { progress ->
            when {
                progress < 0.5f -> {
                    val t = progress / 0.5f
                    val min = ((t - 0.35f) / 0.65f).clampedLerp(0f, 3.0f)
                    val max = t.clampedLerp(0f, 3.1f)
                    Pair(min, max)
                }

                progress < 1.4f -> Pair(3.0f, 3.1f)

                else -> {
                    val t = (progress - 1.4f) / (2.0f - 1.4f)
                    val min = t.clampedLerp(2.0f, -0.1f)
                    val max = ((t - 0.35f) / 0.65f).clampedLerp(2.1f, 0f)
                    Pair(min, max)
                }
            }
        }

        withRenderLayer(
            BeastweaverSpiritAttackGeoLayer(
                this,
                listOf(
                    "leftElephantLeg",
                    "rightElephantLeg",
                ),
                progress = { renderState -> renderState.getGeckolibData(CURRENT_ATTACK_ANIM_TIME) ?: 0F },
                textures = mapOf(0F to IdentifierUtil.default("textures/entity/beastweaver/beastweaver_elephant_legs_0.png")),
                getGradientOrigin = getCameraRelativeEntityPos,
                getGradientBounds = getElephantStompAttackGradientBounds,
                active = { renderState -> renderState.getGeckolibData(CURRENT_ATTACK_NAME) == "attack.elephant_stomp" },
            )
        )
    }

    companion object {
        private val HIDDEN_BONES = DataTicket.create("hidden_bones", object : TypeToken<Set<String>>() {})
        private val TRANSFORM_PROGRESS = DataTicket.create("transform_progress", object : TypeToken<Float>() {})
        private val SHOULDER_SPIKES_ANIM_TIME = DataTicket.create("shoulder_spikes_anim_time", object : TypeToken<Float>() {})
        private val CURRENT_ATTACK_NAME = DataTicket.create("current_attack_name", object : TypeToken<String>() {})
        private val CURRENT_ATTACK_ANIM_TIME = DataTicket.create("current_attack_anim_time", object : TypeToken<Float>() {})
    }

    override fun addRenderData(animatable: BeastweaverEntity, relatedObject: Void?, renderState: R, partialTick: Float) {
        super.addRenderData(animatable, relatedObject, renderState, partialTick)

        renderState.addGeckolibData(HIDDEN_BONES, animatable.getHiddenBones())
        renderState.addGeckolibData(TRANSFORM_PROGRESS, animatable.getTransformProgress(partialTick))
        renderState.addGeckolibData(SHOULDER_SPIKES_ANIM_TIME, animatable.getShoulderSpikesAnimTime(partialTick))
        renderState.addGeckolibData(CURRENT_ATTACK_NAME, animatable.getCurrentAttackAnimName())
        renderState.addGeckolibData(CURRENT_ATTACK_ANIM_TIME, animatable.getCurrentAttackAnimTime(partialTick))
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