package de.fuballer.mcendgame.client.component.screen

import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
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
    override fun renderWidget(context: GuiGraphics, mouseX: Int, mouseY: Int, deltaTicks: Float) {
        val matrices = context.pose()

        matrices.pushMatrix()
        matrices.translate(x * (1 - scale), y * (1 - scale))
        matrices.scale(scale, scale)

        super.renderWidget(context, mouseX, mouseY, deltaTicks)

        matrices.popMatrix()
    }
}