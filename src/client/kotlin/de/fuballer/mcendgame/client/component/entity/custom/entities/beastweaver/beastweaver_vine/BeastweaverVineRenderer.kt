package de.fuballer.mcendgame.client.component.entity.custom.entities.beastweaver.beastweaver_vine

import com.geckolib.constant.dataticket.DataTicket
import com.geckolib.renderer.GeoEntityRenderer
import com.geckolib.renderer.base.BoneSnapshots
import com.geckolib.renderer.base.GeoRenderState
import com.geckolib.renderer.base.RenderPassInfo
import com.google.common.reflect.TypeToken
import de.fuballer.mcendgame.main.component.entity.custom.entities.beastweaver.beastweaver_vine.BeastweaverVineEntity
import de.fuballer.mcendgame.main.component.entity.custom.entities.beastweaver.beastweaver_vine.BeastweaverVineSwayData
import de.fuballer.mcendgame.main.util.extension.FloatExtension.clampedLerp
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState
import net.minecraft.client.renderer.texture.OverlayTexture
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class BeastweaverVineRenderer<R>(
    context: EntityRendererProvider.Context
) : GeoEntityRenderer<BeastweaverVineEntity, R>(context, BeastweaverVineModel()) where R : LivingEntityRenderState, R : GeoRenderState {
    companion object {
        private const val BONE_COUNT = 9
        private val BONE_NAMES = listOf("vine") + (0..<BONE_COUNT).map { "vine$it" }
        private val BONE_EMERGE_DURATION = BeastweaverVineEntity.EMERGE_DURATION_TICKS / BONE_NAMES.size
        private val BONE_DEATH_DURATION = BeastweaverVineEntity.DEATH_DURATION_TICKS / BONE_NAMES.size
        private val BONE_DATA = BONE_NAMES.mapIndexed { index, name ->
            VineBoneData(
                name,
                BeastweaverVineEntity.EMERGE_DURATION_TICKS - BONE_EMERGE_DURATION * (index + 1),
                BONE_DEATH_DURATION * index,
            )
        }

        private val EMERGING_TICKS = DataTicket.create("emerging_ticks", object : TypeToken<Int>() {})
        private val IS_ATTACKING = DataTicket.create("is_attacking", object : TypeToken<Boolean>() {})
        private val ATTACK_ANIM_TIME_SEC = DataTicket.create("attack_anim_time", object : TypeToken<Float>() {})
        private val ROTATION_TO_TARGET_RAD = DataTicket.create("rotation_to_target", object : TypeToken<Float>() {})
        private val RANDOM_SWAY_DATA = DataTicket.create("random_Sway_data", object : TypeToken<BeastweaverVineSwayData>() {})
    }

    override fun addRenderData(
        animatable: BeastweaverVineEntity,
        relatedObject: Void?,
        renderState: R,
        partialTick: Float,
    ) {
        renderState.addGeckolibData(EMERGING_TICKS, animatable.emergingTicksClient)
        renderState.addGeckolibData(IS_ATTACKING, animatable.isPlayingAttackAnimation())
        renderState.addGeckolibData(ATTACK_ANIM_TIME_SEC, animatable.getCurrentAttackAnimTime(0F))
        renderState.addGeckolibData(ROTATION_TO_TARGET_RAD, animatable.entityData.get(BeastweaverVineEntity.RAD_ROTATION_TO_TARGET_DATA))
        renderState.addGeckolibData(RANDOM_SWAY_DATA, animatable.swayData)
    }

    override fun adjustModelBonesForRender(renderPassInfo: RenderPassInfo<R>, snapshots: BoneSnapshots) {
        super.adjustModelBonesForRender(renderPassInfo, snapshots)

        val state = renderPassInfo.renderState()
        val age = state.ageInTicks // includes partial tick
        val emergeDuration = (state.getGeckolibData(EMERGING_TICKS) ?: 0) + state.partialTick
        val deathDuration = state.deathTime

        val isAttacking = state.getGeckolibData(IS_ATTACKING) ?: false
        val attackAnimTimeSec = state.getGeckolibData(ATTACK_ANIM_TIME_SEC) ?: 0F

        val swayData = state.getGeckolibData(RANDOM_SWAY_DATA) ?: return
        val swayStrength = if (!isAttacking) 1F else 1F - min((attackAnimTimeSec / 0.5F), (2.42F - attackAnimTimeSec) / 0.5F).coerceIn(0.0F, 1.0F)

        val rotationToTargetRad = state.getGeckolibData(ROTATION_TO_TARGET_RAD)?.toDouble() ?: 0.0
        val xRotToTarget = -cos(rotationToTargetRad)
        val zRotToTarget = -sin(rotationToTargetRad)

        BONE_DATA.forEachIndexed { index, data ->
            snapshots.get(data.name).ifPresent { bone ->
                // emerge
                val emergeProgress = min(
                    ((emergeDuration - data.emergeDelay) / BONE_EMERGE_DURATION).coerceIn(0F, 1F),
                    1F - ((deathDuration - data.deathDelay) / BONE_DEATH_DURATION).coerceIn(0F, 1F),
                )
                val preEmergedYOffset = (1F - emergeProgress).clampedLerp(0F, 6F)
                bone.translateY -= preEmergedYOffset

                if (emergeProgress <= 0F) return@ifPresent

                // attack animation
                if (isAttacking) {
                    val animationRotation = -bone.rotZ
                    bone.rotX = (xRotToTarget * animationRotation).toFloat()
                    bone.rotZ = (zRotToTarget * animationRotation).toFloat()
                }

                // sway
                val phase = index * 0.6
                val swayX = sin(age * swayData.speedX + swayData.timeOffsetX + phase) * swayData.strengthX * emergeProgress * swayStrength
                val swayZ = cos(age * swayData.speedY + swayData.timeOffsetY + phase) * swayData.strengthY * emergeProgress * swayStrength

                bone.rotX += Math.toRadians(swayX).toFloat()
                bone.rotZ += Math.toRadians(swayZ).toFloat()
            }
        }
    }

    private data class VineBoneData(
        val name: String,
        val emergeDelay: Int,
        val deathDelay: Int,
    )

    override fun getDeathMaxRotation(renderState: GeoRenderState) = 0F

    override fun getPackedOverlay(
        animatable: BeastweaverVineEntity,
        relatedObject: Void?,
        u: Float,
        partialTick: Float,
    ) = OverlayTexture.NO_OVERLAY
}