package de.fuballer.mcendgame.client.component.entity.custom.entities.webhook

import de.fuballer.mcendgame.main.component.entity.custom.entities.webhook.WebhookEntity
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider

class WebhookRenderer(
    context: EntityRendererProvider.Context,
) : EntityRenderer<WebhookEntity, WebhookRenderState>(context) {
    override fun createRenderState(): WebhookRenderState = WebhookRenderState()
}