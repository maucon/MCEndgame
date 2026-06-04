package de.fuballer.mcendgame.client.component.screen

import com.mojang.authlib.GameProfile
import de.fuballer.mcendgame.main.component.killer.KillerScreenHandler
import de.fuballer.mcendgame.main.component.killer.db.KillerEntity
import de.fuballer.mcendgame.main.util.ColorUtil
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.client.gui.screens.inventory.InventoryScreen
import net.minecraft.client.player.RemotePlayer
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.EntitySpawnReason
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Inventory
import kotlin.jvm.optionals.getOrNull

private val TEXTURE = IdentifierUtil.default("textures/gui/container/killer.png")
private val TITLE_COLOR = ColorUtil.rgbaToInt(64, 64, 64, 255)
private const val ENTITY_DRAW_PANEL_X = 26
private const val ENTITY_DRAW_PANEL_WIDTH = 77
private const val ENTITY_DRAW_PANEL_Y = 18
private const val ENTITY_DRAW_PANEL_HEIGHT = 110
private const val ENTITY_DRAW_PANEL_RATIO = ENTITY_DRAW_PANEL_WIDTH / ENTITY_DRAW_PANEL_HEIGHT.toDouble()
private const val ENTITY_BASE_SIZE = 75

class KillerScreen(
    handler: KillerScreenHandler,
    inventory: Inventory,
    title: Component,
) : AbstractContainerScreen<KillerScreenHandler>(handler, inventory, title) {
    val statusEffectsDisplay = CustomStatusEffectsDisplay(this)
    var killer: LivingEntity? = null
    var trimmedTitle: Component? = null

    init {
        imageWidth = 111
        imageHeight = 136

        statusEffectsDisplay.backgroundHeight = 24
        statusEffectsDisplay.smallWidth = 24
        statusEffectsDisplay.yOffsetPerEffect =
            { effectCount -> if (effectCount <= 4) 25 else (imageHeight - statusEffectsDisplay.backgroundHeight) / (effectCount - 1) }
        statusEffectsDisplay.spriteXOffset = { 3 }
        statusEffectsDisplay.spriteYOffset = 3
        statusEffectsDisplay.descriptionTextYOffset = 8
        statusEffectsDisplay.renderDurationText = false
        statusEffectsDisplay.isWide = { false }

        handler.killerEntity?.let { killer = getKillerEntityAsLivingEntity(it) }
    }

    private fun getKillerEntityAsLivingEntity(
        killerEntity: KillerEntity,
    ): LivingEntity? {
        val type = BuiltInRegistries.ENTITY_TYPE.getValue(killerEntity.type) ?: return null
        val world = Minecraft.getInstance().level!!

        var livingEntity: LivingEntity
        if (type != EntityType.PLAYER) {
            livingEntity = type.create(world, EntitySpawnReason.COMMAND) as LivingEntity
        } else {
            val name = killerEntity.displayName.getOrNull()?.string ?: ""
            val profile = GameProfile(killerEntity.killerUUID, name)
            livingEntity = RemotePlayer(world, profile)
        }

        killerEntity.equipment.forEach { livingEntity.setItemSlot(it.key, it.value) }
        killerEntity.statusEffects.forEach { livingEntity.addEffect(it) }

        return livingEntity
    }

    override fun render(
        context: GuiGraphics,
        mouseX: Int,
        mouseY: Int,
        deltaTicks: Float
    ) {
        super.render(context, mouseX, mouseY, deltaTicks)
        val effects = menu.killerEntity?.statusEffects ?: listOf()
        statusEffectsDisplay.drawStatusEffects(
            context,
            leftPos + imageWidth + 1,
            topPos,
            mouseX,
            mouseY,
            effects,
        )
        renderTooltip(context, mouseX, mouseY)
    }

    override fun renderBg(
        context: GuiGraphics,
        deltaTicks: Float,
        mouseX: Int,
        mouseY: Int
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
            imageWidth,
            imageHeight,
        )

        drawKillerEntity(context, mouseX, mouseY)
    }

    private fun drawKillerEntity(
        context: GuiGraphics,
        mouseX: Int,
        mouseY: Int,
    ) {
        val livingKiller = killer ?: return
        val killerRatio = livingKiller.bbWidth / livingKiller.bbHeight

        val sizeFactor =
            1.0 / if (killerRatio > ENTITY_DRAW_PANEL_RATIO) livingKiller.bbWidth / ENTITY_DRAW_PANEL_RATIO.toFloat() else livingKiller.bbHeight
        val size = (ENTITY_BASE_SIZE * sizeFactor).toInt()

        InventoryScreen.renderEntityInInventoryFollowsMouse(
            context,
            leftPos + ENTITY_DRAW_PANEL_X,
            topPos + ENTITY_DRAW_PANEL_Y,
            leftPos + ENTITY_DRAW_PANEL_X + ENTITY_DRAW_PANEL_WIDTH,
            topPos + ENTITY_DRAW_PANEL_Y + ENTITY_DRAW_PANEL_HEIGHT,
            size,
            0.0625F,
            mouseX.toFloat(),
            mouseY.toFloat(),
            livingKiller
        )
    }

    override fun renderLabels(
        context: GuiGraphics,
        mouseX: Int,
        mouseY: Int
    ) {
        if (trimmedTitle == null) trimTitle()
        context.drawString(font, trimmedTitle!!, titleLabelX, titleLabelY, TITLE_COLOR, false)
    }

    private fun trimTitle() {
        val literal = title.string
        val maxWidth = imageWidth - titleLabelX * 2
        val baseWidth = font.width(literal)
        if (baseWidth <= maxWidth) {
            trimmedTitle = title
            return
        }

        val ellipsis = "..."
        val trimmedMaxWidth = maxWidth - font.width(ellipsis)
        val trimmed = font.plainSubstrByWidth(literal, trimmedMaxWidth)
        trimmedTitle = Component.literal(trimmed + ellipsis)
    }
}