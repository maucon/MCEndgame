package de.fuballer.mcendgame.client.component.screen.scarred_one

import de.fuballer.mcendgame.main.component.dungeon.generation.encounter.encounters.scarred_one.ScarredOneEffectTargetGroup
import de.fuballer.mcendgame.main.component.dungeon.generation.encounter.encounters.scarred_one.data.RolledScarredOneEffect
import de.fuballer.mcendgame.main.component.dungeon.generation.encounter.encounters.scarred_one.networking.ScarredOneResponsePayload
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.gui.screens.inventory.tooltip.TooltipRenderUtil
import net.minecraft.network.chat.Component
import net.minecraft.util.CommonColors
import java.util.*
import kotlin.math.max
import kotlin.math.min

private const val BACKGROUND_PADDING = 10
private const val ATTRIBUTE_LINE_OFFSET = 15
private const val BUTTON_WIDTH = 80
private const val BUTTON_HEIGHT = 20
private const val BUTTON_Y_PADDING = 15
private const val TOTAL_BUTTON_HEIGHT = BUTTON_HEIGHT + BUTTON_Y_PADDING * 2
private const val BUTTON_CENTER_GAP = 20

class ScarredOneScreen(
    val positiveEffects: List<RolledScarredOneEffect>,
    val negativeEffects: List<RolledScarredOneEffect>,
    val uuid: UUID,
) : Screen(Component.empty()) {
    private var effectsTextData = listOf<EffectTextData>()
    private var backgroundWidth = 0
    private var backgroundHeight = 0
    private var scrollOffset = 0.0
    private var maxScroll = 0
    private var backgroundX = 0
    private var backgroundY = 0

    override fun init() {
        super.init()

        setUp()

        addRenderableWidget(
            Button.builder(Component.literal("Accept")) {
                sendResponse(true)
            }.bounds(width / 2 + BUTTON_CENTER_GAP / 2, backgroundY + backgroundHeight + BUTTON_Y_PADDING, BUTTON_WIDTH, BUTTON_HEIGHT)
                .build()
        )

        addRenderableWidget(
            Button.builder(Component.literal("Decline")) {
                sendResponse(false)
            }.bounds(width / 2 - BUTTON_WIDTH - BUTTON_CENTER_GAP / 2, backgroundY + backgroundHeight + BUTTON_Y_PADDING, BUTTON_WIDTH, BUTTON_HEIGHT)
                .build()
        )
    }

    private fun setUp() {
        effectsTextData = positiveEffects.map { EffectTextData(it, true) }.sortedBy { it.targets } +
                negativeEffects.map { EffectTextData(it, false) }.sortedBy { it.targets }

        val targetLines = effectsTextData.distinctBy { it.positive to it.targets }.map { it.targets.text }

        val maxEffectLineWidth = effectsTextData.maxOfOrNull { font.width(it.text.string) } ?: 0
        val maxTargetLineWidth = targetLines.maxOfOrNull { font.width(it.string) } ?: 0
        val maxTextWidth = max(maxEffectLineWidth, maxTargetLineWidth)
        backgroundWidth = maxTextWidth + 2 * BACKGROUND_PADDING
        backgroundX = (width / 2) - (backgroundWidth / 2)

        val targetLineCount = targetLines.size
        val totalLineCount = effectsTextData.size + targetLineCount

        val textHeight = if (totalLineCount > 0) (totalLineCount - 1) * ATTRIBUTE_LINE_OFFSET + font.lineHeight else 0
        val totalTextHeight = BACKGROUND_PADDING * 2 + textHeight
        val availableHeight = (height - TOTAL_BUTTON_HEIGHT * 2).coerceAtLeast(0)
        backgroundHeight = min(totalTextHeight, availableHeight)
        maxScroll = max(0, totalTextHeight - backgroundHeight)
        backgroundY = (height / 2) - (backgroundHeight / 2)
    }

    override fun render(
        context: GuiGraphics,
        mouseX: Int,
        mouseY: Int,
        delta: Float
    ) {
        super.render(context, mouseX, mouseY, delta)

        TooltipRenderUtil.renderTooltipBackground(context, backgroundX, backgroundY, backgroundWidth, backgroundHeight, null)

        context.enableScissor(
            backgroundX,
            backgroundY,
            backgroundX + backgroundWidth,
            backgroundY + backgroundHeight
        )

        var lastTargets: ScarredOneEffectTargetGroup? = null
        var lastPositive: Boolean? = null
        var y = backgroundY + BACKGROUND_PADDING - scrollOffset.toInt()

        effectsTextData.forEach { effect ->
            if (effect.targets != lastTargets || effect.positive != lastPositive) {
                context.drawString(
                    font,
                    effect.targets.text,
                    backgroundX + BACKGROUND_PADDING,
                    y,
                    if (effect.positive) CommonColors.GREEN else CommonColors.RED,
                    false
                )

                lastTargets = effect.targets
                lastPositive = effect.positive
                y += ATTRIBUTE_LINE_OFFSET
            }

            context.drawString(
                font,
                effect.text,
                backgroundX + BACKGROUND_PADDING,
                y,
                CommonColors.BLUE, // is overruled by text color anyway
                false
            )
            y += ATTRIBUTE_LINE_OFFSET
        }

        context.disableScissor()
    }

    private data class EffectTextData(
        val positive: Boolean,
        val targets: ScarredOneEffectTargetGroup,
        val text: Component,
    ) {
        constructor(effect: RolledScarredOneEffect, positive: Boolean) : this(positive, effect.targets, effect.getAttribute())
    }

    override fun mouseScrolled(mouseX: Double, mouseY: Double, horizontalAmount: Double, verticalAmount: Double): Boolean {
        scrollOffset = (scrollOffset - verticalAmount * 10.0).coerceIn(0.0, maxScroll.toDouble())
        return true
    }

    override fun isPauseScreen(): Boolean = false

    fun sendResponse(accept: Boolean) {
        val payload = ScarredOneResponsePayload(accept, uuid)
        ClientPlayNetworking.send(payload)

        onClose()
    }
}