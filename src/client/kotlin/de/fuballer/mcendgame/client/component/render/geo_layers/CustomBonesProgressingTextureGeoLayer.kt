package de.fuballer.mcendgame.client.component.render.geo_layers

import com.geckolib.GeckoLibConstants
import com.geckolib.animatable.GeoAnimatable
import com.geckolib.cache.model.GeoBone
import com.geckolib.cache.model.cuboid.CuboidGeoBone
import com.geckolib.renderer.base.*
import com.geckolib.renderer.layer.builtin.CustomBoneTextureGeoLayer
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.resources.Identifier
import java.util.function.BiConsumer

open class CustomBonesProgressingTextureGeoLayer<T : GeoAnimatable, O : Any, R : GeoRenderState>(
    renderer: GeoRenderer<T, O, R>,
    val bones: List<String>,
    val progress: (R) -> Float,
    val textures: Map<Float, Identifier>,
    val baseTexture: Identifier = textures[textures.keys.first()]!!,
    val activeThreshold: Float = 0F,
) : CustomBoneTextureGeoLayer<T, O, R>(renderer, bones.first(), baseTexture) {
    protected open fun isActive(renderState: R) = progress(renderState) >= activeThreshold

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
                        snapshot.skipRender(true)
                        snapshot.skipChildrenRender(true)
                    }
            }
        }
    }

    override fun addPerBoneRender(renderPassInfo: RenderPassInfo<R>, consumer: BiConsumer<GeoBone, PerBoneRender<R>>) {
        if (!renderPassInfo.willRender()) return
        val state = renderPassInfo.renderState()
        if (!isActive(state)) return
        if (!shouldRenderBone(state)) return

        fun renderBoneAndChildren(bone: GeoBone) {
            if (bone is CuboidGeoBone) {
                consumer.accept(bone) { passInfo, renderBone, renderTasks ->
                    renderBone(passInfo, renderBone, renderTasks)
                }
            }

            bone.children().forEach { renderBoneAndChildren(it) }
        }

        bones.forEach { boneName ->
            renderPassInfo.model()
                .getBone(boneName)
                .ifPresentOrElse({ bone -> renderBoneAndChildren(bone) })
                { GeckoLibConstants.LOGGER.error("Unable to find bone for CustomBonesProgressingTextureGeoLayer: {}, skipping", boneName) }
        }
    }
}