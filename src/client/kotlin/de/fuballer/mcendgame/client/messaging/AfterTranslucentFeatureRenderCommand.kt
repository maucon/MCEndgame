package de.fuballer.mcendgame.client.messaging

import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext

data class AfterTranslucentFeatureRenderCommand(
    val context: LevelRenderContext,
)