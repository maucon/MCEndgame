package de.fuballer.mcendgame.client.component.render

import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.initializer.Initializer

/**
 * 26.3 compiles every registered pipeline while the shader manager reloads, so the mod's custom
 * pipelines have to be built before the first resource reload instead of lazily on first use.
 */
@Injectable
object CustomRenderPipelineRegisterer {
    @Initializer
    fun register() {
        CustomRenderLayers.LINK
    }
}
