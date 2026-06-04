package de.fuballer.mcendgame.client.component.screen

import de.fuballer.mcendgame.main.component.totem.TotemScreenHandler
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

private val TEXTURE = IdentifierUtil.default("textures/gui/container/totem.png")

class TotemScreen(
    handler: TotemScreenHandler,
    inventory: Inventory,
    title: Component,
) : AbstractContainerScreen<TotemScreenHandler>(handler, inventory, title) {
    init {
        imageWidth = 176
        imageHeight = 169
        inventoryLabelY = imageHeight - 94
    }

    override fun render(context: GuiGraphics, mouseX: Int, mouseY: Int, deltaTicks: Float) {
        super.render(context, mouseX, mouseY, deltaTicks)
        renderTooltip(context, mouseX, mouseY)
    }

    override fun renderBg(
        context: GuiGraphics,
        deltaTicks: Float,
        mouseX: Int,
        mouseY: Int,
    ) {
        val textureX = (width - imageWidth) / 2
        val textureY = (height - imageHeight) / 2

        context.blit(
            RenderPipelines.GUI_TEXTURED,
            TEXTURE,
            textureX,
            textureY,
            0.0f,
            0.0f,
            imageWidth,
            imageHeight,
            256,
            256,
        )
    }
}