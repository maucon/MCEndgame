package de.fuballer.mcendgame.client.component.entity.custom.entities.beastweaver.beastweaver_vine

import com.geckolib.renderer.GeoEntityRenderer
import com.geckolib.renderer.base.BoneSnapshots
import com.geckolib.renderer.base.GeoRenderState
import com.geckolib.renderer.base.RenderPassInfo
import de.fuballer.mcendgame.main.component.entity.custom.entities.beastweaver.beastweaver_vine.BeastweaverVineEntity
import de.fuballer.mcendgame.main.util.extension.FloatExtension.clampedLerp
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState
import kotlin.math.cos
import kotlin.math.sin

class BeastweaverVineRenderer<R>(
    context: EntityRendererProvider.Context
) : GeoEntityRenderer<BeastweaverVineEntity, R>(context, BeastweaverVineModel()) where R : LivingEntityRenderState, R : GeoRenderState {
    companion object {
        private val BONE_NAMES = listOf("vine") + (0..8).map { "vine$it" }
        private val BONE_EMERGE_DURATION = BeastweaverVineEntity.EMERGE_DURATION_TICKS / BONE_NAMES.size
        private val BONE_DATA = BONE_NAMES.mapIndexed { index, name ->
            VineBoneData(name, BeastweaverVineEntity.EMERGE_DURATION_TICKS - BONE_EMERGE_DURATION * (index + 1))
        }
    }

    override fun adjustModelBonesForRender(renderPassInfo: RenderPassInfo<R>, snapshots: BoneSnapshots) {
        super.adjustModelBonesForRender(renderPassInfo, snapshots)

        val state = renderPassInfo.renderState()
        val age = state.ageInTicks // already includes partial tick

        BONE_DATA.forEachIndexed { index, data ->
            snapshots.get(data.name).ifPresent { bone ->
                val emergeProgress = ((age - data.emergeDelay) / BONE_EMERGE_DURATION).coerceIn(0F, 1F)

                val preEmergedYOffset = (1F - emergeProgress).clampedLerp(0F, 6F)
                bone.translateY -= preEmergedYOffset

                if (emergeProgress > 0F) {
                    val movementTime = age * 0.08
                    val phase = index * 0.6

                    val swayX = sin(movementTime + phase) * 4 * emergeProgress
                    val swayZ = cos(movementTime * 0.8 + phase) * 5 * emergeProgress

                    bone.rotX = Math.toRadians(swayX).toFloat()
                    bone.rotZ = Math.toRadians(swayZ).toFloat()
                }
            }
        }
    }

    private data class VineBoneData(
        val name: String,
        val emergeDelay: Int,
    )
}