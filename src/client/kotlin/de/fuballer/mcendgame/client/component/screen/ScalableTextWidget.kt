package de.fuballer.mcendgame.client.component.screen

import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.components.StringWidget
import net.minecraft.network.chat.Component

class ScalableTextWidget(
    x: Int,
    y: Int,
    width: Int,
    height: Int,
    text: Component,
    textRenderer: Font,
    private val scale: Float,
) : StringWidget(x, y, width, height, text, textRenderer) {
    override fun extractWidgetRenderState(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, deltaTicks: Float) {
        val matrices = graphics.pose()

        matrices.pushMatrix()
        matrices.translate(x * (1 - scale), y * (1 - scale))
        matrices.scale(scale, scale)

        super.extractWidgetRenderState(graphics, mouseX, mouseY, deltaTicks)

        matrices.popMatrix()
    }
}