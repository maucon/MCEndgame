package de.fuballer.mcendgame.client.component.screen

import com.mojang.authlib.GameProfile
import de.fuballer.mcendgame.main.component.inventory.EmptySpriteSlot
import de.fuballer.mcendgame.main.component.killer.KillerScreenHandler
import de.fuballer.mcendgame.main.component.killer.db.KillerEntity
import de.fuballer.mcendgame.main.util.ColorUtil
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.client.gui.screen.ingame.InventoryScreen
import net.minecraft.client.network.OtherClientPlayerEntity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.registry.Registries
import net.minecraft.text.Text
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
    inventory: PlayerInventory,
    title: Text,
) : HandledScreen<KillerScreenHandler>(handler, inventory, title) {
    val statusEffectsDisplay = CustomStatusEffectsDisplay(this)
    var killer: LivingEntity? = null
    var trimmedTitle: Text? = null

    init {
        backgroundWidth = 111
        backgroundHeight = 136

        statusEffectsDisplay.backgroundHeight = 24
        statusEffectsDisplay.smallWidth = 24
        statusEffectsDisplay.yOffsetPerEffect =
            { effectCount -> if (effectCount <= 4) 25 else (backgroundHeight - statusEffectsDisplay.backgroundHeight) / (effectCount - 1) }
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
        val type = Registries.ENTITY_TYPE.get(killerEntity.type) ?: return null
        val world = MinecraftClient.getInstance().world!!

        var livingEntity: LivingEntity
        if (type != EntityType.PLAYER) {
            livingEntity = type.create(world) as LivingEntity
        } else {
            val name = killerEntity.displayName.getOrNull()?.string ?: ""
            val profile = GameProfile(killerEntity.killerUUID, name)
            livingEntity = OtherClientPlayerEntity(world, profile)
        }

        killerEntity.equipment.forEach { livingEntity.equipStack(it.key, it.value) }
        killerEntity.statusEffects.forEach { livingEntity.addStatusEffect(it) }

        return livingEntity
    }

    override fun render(
        context: DrawContext,
        mouseX: Int,
        mouseY: Int,
        deltaTicks: Float
    ) {
        super.render(context, mouseX, mouseY, deltaTicks)
        val effects = handler.killerEntity?.statusEffects ?: listOf()
        statusEffectsDisplay.drawStatusEffects(
            context,
            x + backgroundWidth + 1,
            y,
            mouseX,
            mouseY,
            effects,
        )
        drawMouseoverTooltip(context, mouseX, mouseY)
    }

    override fun drawBackground(
        context: DrawContext,
        deltaTicks: Float,
        mouseX: Int,
        mouseY: Int
    ) {
        val textureX = (width - backgroundWidth) / 2
        val textureY = (height - backgroundHeight) / 2

        context.drawTexture(
            TEXTURE,
            textureX,
            textureY,
            0.0f,
            0.0f,
            backgroundWidth,
            backgroundHeight,
            backgroundWidth,
            backgroundHeight,
        )

        drawEmptySlotSprites(context)

        drawKillerEntity(context, mouseX, mouseY)
    }

    private fun drawEmptySlotSprites(context: DrawContext) {
        for (slot in handler.slots) {
            if (slot !is EmptySpriteSlot || slot.hasStack()) continue
            context.drawGuiTexture(
                slot.sprite,
                x + slot.x,
                y + slot.y,
                16,
                16
            )
        }
    }

    private fun drawKillerEntity(
        context: DrawContext,
        mouseX: Int,
        mouseY: Int,
    ) {
        val livingKiller = killer ?: return
        val killerRatio = livingKiller.width / livingKiller.height

        val sizeFactor =
            1.0 / if (killerRatio > ENTITY_DRAW_PANEL_RATIO) livingKiller.width / ENTITY_DRAW_PANEL_RATIO.toFloat() else livingKiller.height
        val size = (ENTITY_BASE_SIZE * sizeFactor).toInt()

        InventoryScreen.drawEntity(
            context,
            x + ENTITY_DRAW_PANEL_X,
            y + ENTITY_DRAW_PANEL_Y,
            x + ENTITY_DRAW_PANEL_X + ENTITY_DRAW_PANEL_WIDTH,
            y + ENTITY_DRAW_PANEL_Y + ENTITY_DRAW_PANEL_HEIGHT,
            size,
            0.0625F,
            mouseX.toFloat(),
            mouseY.toFloat(),
            livingKiller
        )
    }

    override fun drawForeground(
        context: DrawContext,
        mouseX: Int,
        mouseY: Int
    ) {
        if (trimmedTitle == null) trimTitle()
        context.drawText(textRenderer, trimmedTitle!!, titleX, titleY, TITLE_COLOR, false)
    }

    private fun trimTitle() {
        val literal = title.string
        val maxWidth = backgroundWidth - titleX * 2
        val baseWidth = textRenderer.getWidth(literal)
        if (baseWidth <= maxWidth) {
            trimmedTitle = title
            return
        }

        val ellipsis = "..."
        val trimmedMaxWidth = maxWidth - textRenderer.getWidth(ellipsis)
        val trimmed = textRenderer.trimToWidth(literal, trimmedMaxWidth)
        trimmedTitle = Text.literal(trimmed + ellipsis)
    }
}