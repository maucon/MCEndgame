package de.fuballer.mcendgame.client.component.screen

import de.fuballer.mcendgame.main.component.totem.TotemScreenHandler
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

private val TEXTURE = IdentifierUtil.default("textures/gui/container/totem.png")

class TotemScreen(
    handler: TotemScreenHandler,
    inventory: Inventory,
    title: Component,
) : AbstractContainerScreen<TotemScreenHandler>(handler, inventory, title, 176, 169) {
    init {
        inventoryLabelY = imageHeight - 94
    }

    override fun extractContents(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, deltaTicks: Float) {
        super.extractContents(graphics, mouseX, mouseY, deltaTicks)
        extractTooltip(graphics, mouseX, mouseY)
    }

    override fun extractBackground(
        graphics: GuiGraphicsExtractor,
        mouseX: Int,
        mouseY: Int,
        deltaTicks: Float,
    ) {
        val textureX = (width - imageWidth) / 2
        val textureY = (height - imageHeight) / 2

        graphics.blit(
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