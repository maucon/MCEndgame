package de.fuballer.mcendgame.client.component.entity.custom.entities.beastweaver

import com.geckolib.GeckoLibConstants
import com.geckolib.animatable.GeoAnimatable
import com.geckolib.cache.model.GeoBone
import com.geckolib.cache.model.cuboid.CuboidGeoBone
import com.geckolib.renderer.base.*
import com.geckolib.renderer.layer.builtin.CustomBoneTextureGeoLayer
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.resources.Identifier
import java.util.function.BiConsumer

class CustomBonesProgressingTextureGeoLayer<T : GeoAnimatable, O : Any, R : GeoRenderState>(
    renderer: GeoRenderer<T, O, R>,
    val bones: List<String>,
    val progress: (R) -> Float,
    val textures: Map<Float, Identifier>,
    val baseTexture: Identifier = textures[textures.keys.first()]!!,
    val activeThreshold: Float = 0F,
) : CustomBoneTextureGeoLayer<T, O, R>(renderer, bones.first(), baseTexture) {
    private fun isActive(renderState: R) = progress(renderState) >= activeThreshold

    override fun getTextureResource(renderState: R): Identifier {
        val progress = progress(renderState)

        val reachedThresholds = textures.keys.filter { it <= progress }
        if (reachedThresholds.isEmpty()) return baseTexture
        return textures[reachedThresholds.max()]!!
    }

    override fun preRender(renderPassInfo: RenderPassInfo<R>, renderTasks: SubmitNodeCollector) {
        if (!renderPassInfo.willRender()) return
        if (!isActive(renderPassInfo.renderState())) return

        renderPassInfo.addBoneUpdater { _, snapshots: BoneSnapshots ->
            bones.forEach { bone ->
                snapshots.get(bone)
                    .filter { snapshot -> snapshot.bone is CuboidGeoBone && shouldRenderBone(renderPassInfo.renderState()) }
                    .ifPresent { snapshot ->
                        val skipChildren = snapshot.areChildrenHidden()
                        snapshot.skipRender(true)
                        snapshot.skipChildrenRender(skipChildren)
                    }
            }
        }
    }

    override fun addPerBoneRender(renderPassInfo: RenderPassInfo<R>, consumer: BiConsumer<GeoBone, PerBoneRender<R>>) {
        if (!renderPassInfo.willRender()) return
        val state = renderPassInfo.renderState()
        if (!isActive(state)) return
        if (!shouldRenderBone(state)) return

        bones.forEach { boneName ->
            renderPassInfo.model()
                .getBone(boneName)
                .filter { bone -> CuboidGeoBone::class.java.isInstance(bone) }
                .ifPresentOrElse({ bone ->
                    consumer.accept(bone) { renderPassInfo, bone, renderTasks ->
                        renderBone(renderPassInfo, bone, renderTasks)
                    }
                }
                ) { GeckoLibConstants.LOGGER.error("Unable to find bone for CustomBonesProgressingTextureGeoLayer: {}, skipping", boneName) }
        }
    }
}