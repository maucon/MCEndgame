package de.fuballer.mcendgame.client.component.render

import net.minecraft.client.renderer.rendertype.LayeringTransform
import net.minecraft.client.renderer.rendertype.OutputTarget
import net.minecraft.client.renderer.rendertype.RenderSetup
import net.minecraft.client.renderer.rendertype.RenderType
import net.minecraft.resources.Identifier
import net.minecraft.util.Util
import java.util.function.Function

object CustomRenderLayers {
    val LINK: RenderType = RenderType.create(
        "link",
        RenderSetup.builder(CustomRenderPipelines.LINK_PIPELINE)
            .useLightmap()
            .createRenderSetup()
    )

    fun ghostly(texture: Identifier) = GHOSTLY.apply(texture)
    val GHOSTLY: Function<Identifier, RenderType> = Util.memoize<Identifier, RenderType> { texture ->
        val renderSetup = RenderSetup.builder(CustomRenderPipelines.GHOSTLY_PIPELINE)
            .withTexture("Sampler0", texture)
            .useOverlay()
            .sortOnUpload()
            .setOutline(RenderSetup.OutlineProperty.AFFECTS_OUTLINE)
            .createRenderSetup()
        RenderType.create("ghostly", renderSetup)
    }

    fun boundAbyss(texture: Identifier) = BOUND_ABYSS.apply(texture)
    val BOUND_ABYSS: Function<Identifier, RenderType> = Util.memoize<Identifier, RenderType> { texture ->
        val renderSetup = RenderSetup.builder(CustomRenderPipelines.BOUND_ABYSS_PIPELINE)
            .withTexture("Sampler0", texture)
            .useLightmap()
            .useOverlay()
            .setLayeringTransform(LayeringTransform.VIEW_OFFSET_Z_LAYERING)
            .affectsCrumbling()
            .setOutline(RenderSetup.OutlineProperty.AFFECTS_OUTLINE)
            .createRenderSetup();
        RenderType.create("bound_abyss", renderSetup)
    }

    fun beastweaverAttack(texture: Identifier) = BEASTWEAVER_ATTACK.apply(texture)
    val BEASTWEAVER_ATTACK: Function<Identifier, RenderType> = Util.memoize<Identifier, RenderType> { texture ->
        val renderSetup = RenderSetup.builder(CustomRenderPipelines.BEASTWEAVER_ATTACK_PIPELINE)
            .withTexture("Sampler0", texture)
            .setOutputTarget(OutputTarget.MAIN_TARGET)
            .useLightmap()
            .sortOnUpload()
            .setOutline(RenderSetup.OutlineProperty.NONE)
            .createRenderSetup()
        RenderType.create("beastweaver_attack", renderSetup)
    }
}