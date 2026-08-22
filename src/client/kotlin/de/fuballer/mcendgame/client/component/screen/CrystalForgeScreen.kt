package de.fuballer.mcendgame.client.component.screen

import de.fuballer.mcendgame.main.component.block.blocks.crystalforge.CrystalForgeScreenHandler
import de.fuballer.mcendgame.main.component.block.blocks.crystalforge.CrystalForgeSettings
import de.fuballer.mcendgame.main.component.block.blocks.crystalforge.network.CrystalForgePayload
import de.fuballer.mcendgame.main.component.item.custom.crystal.CrystalItem
import de.fuballer.mcendgame.main.util.ColorUtil
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.StringWidget
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.Slot
import java.awt.Color
import kotlin.math.pow
import kotlin.math.sin

private val TEXTURE = IdentifierUtil.default("textures/gui/container/crystal_forge.png")
private val TEXTURE_SECONDARY = IdentifierUtil.default("textures/gui/container/crystal_forge_secondary.png")
private val FORGE_BUTTON_TEXT = Component.translatable("${CrystalForgeSettings.CONTAINER_BASE_KEY}forge_button")

private const val FORGE_ANIMATION_DURATION = 10.0

class CrystalForgeScreen(
    handler: CrystalForgeScreenHandler,
    inventory: Inventory,
    title: Component,
) : AbstractContainerScreen<CrystalForgeScreenHandler>(handler, inventory, title) {
    private val forgeButton = Button
        .builder(FORGE_BUTTON_TEXT, ::onForgeButtonPress)
        .size(36, 12)
        .build()

    private lateinit var forgeErrorText: StringWidget

    private var forgeAnimationTime = -1F
    private var forgeAnimationColor = Color.WHITE
    private var animateSecondaryOutputSlot = false

    override fun init() {
        super.init()

        val backgroundX = (width - imageWidth) / 2
        val backgroundY = (height - imageHeight) / 2

        forgeButton.setPosition(backgroundX + 70, backgroundY + 62)
        addRenderableWidget(forgeButton)

        forgeErrorText = StringWidget(
            backgroundX + 3,
            backgroundY - 10,
            200,
            10,
            Component.empty(),
            font
        )
        addRenderableWidget(forgeErrorText)
    }

    override fun extractContents(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, deltaTicks: Float) {
        super.extractContents(graphics, mouseX, mouseY, deltaTicks)
        extractTooltip(graphics, mouseX, mouseY)
    }

    override fun extractBackground(
        graphics: GuiGraphicsExtractor,
        mouseX: Int,
        mouseY: Int,
        deltaTicks: Float
    ) {
        extractTransparentBackground(graphics)

        val textureX = (width - imageWidth) / 2
        val textureY = (height - imageHeight) / 2

        graphics.blit(
            RenderPipelines.GUI_TEXTURED,
            if (!isSecondaryOutputSlotFilled()) TEXTURE else TEXTURE_SECONDARY,
            textureX,
            textureY,
            0.0f,
            0.0f,
            imageWidth,
            imageHeight,
            imageWidth,
            imageHeight,
        )

        val animationSlots = mutableListOf(menu.slots[0])
        if (animateSecondaryOutputSlot && isSecondaryOutputSlotFilled()) animationSlots.add(menu.slots[2])
        drawForgeAnimation(graphics, animationSlots, deltaTicks)
    }

    private fun drawForgeAnimation(
        graphics: GuiGraphicsExtractor,
        slots: List<Slot>,
        deltaTicks: Float,
    ) {
        if (forgeAnimationTime >= 0) {
            val progress = (forgeAnimationTime / FORGE_ANIMATION_DURATION).coerceIn(0.0, 1.0)
            val pulse = sin(progress.pow(0.4) * Math.PI)

            val bgAlpha = (pulse * 100).toInt()
            val bgColor = ColorUtil.rgbaToInt(forgeAnimationColor.red, forgeAnimationColor.green, forgeAnimationColor.blue, bgAlpha)

            val outlineAlpha = (pulse * 255).toInt()
            val outlineColor = ColorUtil.rgbaToInt(forgeAnimationColor.red, forgeAnimationColor.green, forgeAnimationColor.blue, outlineAlpha)

            slots.forEach { slot ->
                val toForgeSlotX = (width - imageWidth) / 2 + slot.x
                val toForgeSlotY = (height - imageHeight) / 2 + slot.y
                val forgeAnimationX1 = toForgeSlotX - 2
                val forgeAnimationY1 = toForgeSlotY - 2
                val forgeAnimationX2 = toForgeSlotX + 18
                val forgeAnimationY2 = toForgeSlotY + 18

                graphics.fill(forgeAnimationX1 + 1, forgeAnimationY1 + 1, forgeAnimationX2 - 1, forgeAnimationY2 - 1, bgColor)

                graphics.fill(forgeAnimationX1, forgeAnimationY1, forgeAnimationX2, forgeAnimationY1 + 1, outlineColor)
                graphics.fill(forgeAnimationX1, forgeAnimationY2 - 1, forgeAnimationX2, forgeAnimationY2, outlineColor)
                graphics.fill(forgeAnimationX1, forgeAnimationY1 + 1, forgeAnimationX1 + 1, forgeAnimationY2 - 1, outlineColor)
                graphics.fill(forgeAnimationX2 - 1, forgeAnimationY1 + 1, forgeAnimationX2, forgeAnimationY2 - 1, outlineColor)
            }

            forgeAnimationTime += deltaTicks
            if (forgeAnimationTime >= FORGE_ANIMATION_DURATION) forgeAnimationTime = -1F
        }
    }

    private fun onForgeButtonPress(button: Button) {
        val toForgeStack = menu.slots[0].item
        val crystalStack = menu.slots[1].item

        val crystalItem = crystalStack.item
        if (crystalItem !is CrystalItem) {
            forgeErrorText.message = CrystalForgeSettings.getForgeErrorText("no_crystal")
            return
        }

        val cannotForgeReason = crystalItem.canForge(toForgeStack, isSecondaryOutputSlotFilled())
        if (cannotForgeReason != null) {
            forgeErrorText.message = cannotForgeReason
        } else {
            forgeErrorText.message = Component.empty()
            ClientPlayNetworking.send(CrystalForgePayload())

            forgeAnimationTime = 0F
            forgeAnimationColor = crystalItem.forgeColor
            animateSecondaryOutputSlot = crystalItem.producesSecondaryOutput()
        }
    }

    private fun isSecondaryOutputSlotFilled() = !menu.slots[2].item.isEmpty
}