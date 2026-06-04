package de.fuballer.mcendgame.client.component.screen

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.network.chat.CommonComponents
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.util.CommonColors
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffectUtil
import java.util.*

private val EFFECT_BACKGROUND_TEXTURE: Identifier = Identifier.withDefaultNamespace("container/inventory/effect_background")
private val AMBIENT_EFFECT_BACKGROUND_TEXTURE: Identifier = Identifier.withDefaultNamespace("container/inventory/effect_background_ambient")

class CustomStatusEffectsDisplay(
    val parent: AbstractContainerScreen<*>,
    val client: Minecraft = Minecraft.getInstance(),
) {
    var backgroundHeight = 32
    var wideWidth = 120
    var smallWidth = 32
    var isWide: (Int) -> Boolean = { space -> space >= 120 }

    var spriteSize = 18
    var spriteXOffset: (Boolean) -> Int = { wide -> if (wide) 6 else 7 }
    var spriteYOffset = 7

    var textXOffset = 28
    var descriptionTextYOffset = 6
    var descriptionTextColor = CommonColors.WHITE
    var durationTextYOffset = 16
    var durationTextColor = CommonColors.GRAY
    var renderDurationText = true

    var enableTooltip = true

    var yOffsetPerEffect: (Int) -> Int = { effectCount -> if (effectCount <= 5) 33 else 132 / (effectCount - 1) }

    fun drawStatusEffects(
        context: GuiGraphics,
        x: Int,
        y: Int,
        mouseX: Int,
        mouseY: Int,
        statusEffects: Collection<MobEffectInstance>,
    ) {
        val space = parent.width - x
        if (space < 32) return
        if (statusEffects.isEmpty()) return

        val wide = isWide(space)
        val yOffsetPerEffect = yOffsetPerEffect(statusEffects.size)

        val sortedEffects = statusEffects.sortedBy { it }
        var effectY = y
        sortedEffects.forEach {
            drawStatusEffectBackground(context, x, effectY, wide, it.isAmbient)
            drawStatusEffectSprite(context, x, effectY, it, wide)

            if (wide) drawStatusEffectDescription(context, x, effectY, it)
            effectY += yOffsetPerEffect
        }

        if (wide || !enableTooltip) return
        drawTooltip(context, sortedEffects, x, y, mouseX, mouseY, yOffsetPerEffect)
    }

    private fun drawTooltip(
        context: GuiGraphics,
        effects: Iterable<MobEffectInstance>,
        x: Int,
        y: Int,
        mouseX: Int,
        mouseY: Int,
        yOffsetPerEffect: Int,
    ) {
        if (mouseX < x || mouseX > x + smallWidth) return

        var yy = y
        var hoveredStatusEffectInstance: MobEffectInstance? = null
        effects.forEach {
            if (mouseY >= yy && mouseY <= yy + yOffsetPerEffect) {
                hoveredStatusEffectInstance = it
            }
            yy += yOffsetPerEffect
        }
        if (hoveredStatusEffectInstance == null) return

        val tooltip = mutableListOf(getStatusEffectDescription(hoveredStatusEffectInstance))
        if (renderDurationText) tooltip.add(MobEffectUtil.formatDuration(hoveredStatusEffectInstance, 1.0F, client.level!!.tickRateManager().tickrate()))

        context.setTooltipForNextFrame(parent.font, tooltip, Optional.empty(), mouseX, mouseY)
    }

    private fun drawStatusEffectBackground(
        context: GuiGraphics,
        x: Int,
        yBase: Int,
        wide: Boolean,
        ambient: Boolean,
    ) {
        context.blitSprite(
            RenderPipelines.GUI_TEXTURED,
            if (ambient) AMBIENT_EFFECT_BACKGROUND_TEXTURE else EFFECT_BACKGROUND_TEXTURE,
            x,
            yBase,
            if (wide) wideWidth else smallWidth,
            backgroundHeight
        )
    }

    private fun drawStatusEffectSprite(
        context: GuiGraphics,
        x: Int,
        yBase: Int,
        statusEffect: MobEffectInstance,
        wide: Boolean,
    ) {
        val sprite = Gui.getMobEffectSprite(statusEffect.effect)
        context.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, x + spriteXOffset(wide), yBase + spriteYOffset, spriteSize, spriteSize)
    }

    private fun drawStatusEffectDescription(
        context: GuiGraphics,
        x: Int,
        yBase: Int,
        statusEffect: MobEffectInstance,
    ) {
        val descriptionText = getStatusEffectDescription(statusEffect)
        context.drawString(parent.font, descriptionText, x + textXOffset, yBase + descriptionTextYOffset, descriptionTextColor)

        if (renderDurationText) {
            val durationText = MobEffectUtil.formatDuration(statusEffect, 1.0F, client.level!!.tickRateManager().tickrate())
            context.drawString(parent.font, durationText, x + textXOffset, yBase + durationTextYOffset, durationTextColor)
        }
    }

    private fun getStatusEffectDescription(
        statusEffect: MobEffectInstance,
    ): Component {
        val text = statusEffect.effect.value().displayName.copy()
        if (statusEffect.amplifier !in 1..9) return text
        return text.append(CommonComponents.SPACE).append(Component.translatable("enchantment.level." + (statusEffect.amplifier + 1)))
    }
}