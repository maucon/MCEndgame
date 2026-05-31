package de.fuballer.mcendgame.client.component.screen

import com.mojang.logging.LogUtils
import de.fuballer.mcendgame.main.component.block.blocks.dungeon_device.DungeonDeviceScreenHandler
import de.fuballer.mcendgame.main.component.block.blocks.dungeon_device.networking.DungeonDeviceTrainingPayload
import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute
import de.fuballer.mcendgame.main.component.dungeon.enemy.EnemyLevelScalingSettings
import de.fuballer.mcendgame.main.component.dungeon.level.DungeonLevelSettings
import de.fuballer.mcendgame.main.component.item_filter.ItemFilterCommand
import de.fuballer.mcendgame.main.component.killer.KillerCommand
import de.fuballer.mcendgame.main.component.totem.TotemCommand
import de.fuballer.mcendgame.main.messaging.misc.GetCustomAttributesTextsCommand
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import de.maucon.mauconframework.command.CommandGateway
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gl.RenderPipelines
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.ScreenRect
import net.minecraft.client.gui.screen.ButtonTextures
import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.client.gui.tooltip.Tooltip
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.gui.widget.TextWidget
import net.minecraft.client.gui.widget.TexturedButtonWidget
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text
import kotlin.math.ceil
import kotlin.math.min

private val TEXTURE = IdentifierUtil.default("textures/gui/container/dungeon_device.png")
private val OPEN_DUNGEON_BUTTON_TEXT = Text.translatable("container.mcendgame.dungeon_device.open")
private val ENEMY_ATTRIBUTES_TEXT = Text.translatable("container.mcendgame.dungeon_device.enemy_attributes")
private val BOSS_ATTRIBUTES_TEXT = Text.translatable("container.mcendgame.dungeon_device.boss_attributes")

private val PROGRESS_TEXTURE = IdentifierUtil.default("textures/gui/sprites/dungeon_device/progress.png")

private val ATTRIBUTE_PANEL_TEXTURE = IdentifierUtil.default("textures/gui/container/dungeon_device_attribute_panel.png")
private const val ATTRIBUTE_PANEL_TEXTURE_EDGE_WIDTH = 4
private const val ATTRIBUTE_PANEL_MAX_WIDTH = 150
private const val ATTRIBUTE_HEADER_WIDGET_HEIGHT = 10
private const val ATTRIBUTE_TEXT_WIDGET_SCALE = 0.75f
private const val ATTRIBUTE_TEXT_WIDGET_HEIGHT = 10
private const val ATTRIBUTE_TEXT_WIDGET_Y_OFFSET = 10

private const val SHOW_ATTRIBUTES_BUTTON_WIDTH = 10
private const val SHOW_ATTRIBUTES_BUTTON_HEIGHT = 20
private val SHOW_ATTRIBUTES_BUTTON_TEXT = Text.literal(">")
private val HIDE_ATTRIBUTES_BUTTON_TEXT = Text.literal("<")

private const val COMMAND_BUTTONS_OFFSET = 2
private val TOTEMS_BUTTON_TEXTURES = ButtonTextures(IdentifierUtil.default("dungeon_device/totems"), IdentifierUtil.default("dungeon_device/totems_highlighted"))
private val TOTEMS_BUTTON_TOOLTIP_TEXT = Text.translatable("container.mcendgame.dungeon_device.totems_tooltip")
private val FILTER_BUTTON_TEXTURES = ButtonTextures(IdentifierUtil.default("dungeon_device/filter"), IdentifierUtil.default("dungeon_device/filter_highlighted"))
private val FILTER_BUTTON_TOOLTIP_TEXT = Text.translatable("container.mcendgame.dungeon_device.filter_tooltip")
private val KILLER_BUTTON_TEXTURES = ButtonTextures(IdentifierUtil.default("dungeon_device/killer"), IdentifierUtil.default("dungeon_device/killer_highlighted"))
private val KILLER_BUTTON_TOOLTIP_TEXT = Text.translatable("container.mcendgame.dungeon_device.killer_tooltip")
private val TRAINING_BUTTON_TEXTURES = ButtonTextures(IdentifierUtil.default("dungeon_device/training"), IdentifierUtil.default("dungeon_device/training_highlighted"))
private val TRAINING_BUTTON_TOOLTIP_TEXT = Text.translatable("container.mcendgame.dungeon_device.training_tooltip")

@Environment(EnvType.CLIENT)
class DungeonDeviceScreen(
    handler: DungeonDeviceScreenHandler,
    private val inventory: PlayerInventory,
    title: Text,
) : HandledScreen<DungeonDeviceScreenHandler>(handler, inventory, title) {
    private val log = LogUtils.getLogger()
    private var showLevelAttributes = false
    private var levelScalingTextWidgets = mutableListOf<TextWidget>()

    private val createDungeonButton = ButtonWidget
        .builder(OPEN_DUNGEON_BUTTON_TEXT, ::onCreateDungeonButtonPress)
        .size(36, 12)
        .build()

    override fun init() {
        super.init()
        titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2

        createDungeonButton.setPosition((width - backgroundWidth) / 2 + 70, (height - backgroundHeight) / 2 + 62)
        addDrawableChild(createDungeonButton)

        val playerDungeonLevel = handler.payload.playerDungeonLevel
        addDrawableChild(
            TextWidget(
                (width - backgroundWidth) / 2 + 8,
                (height - backgroundHeight) / 2 + 35,
                100,
                10,
                Text.translatable("text.mcendgame.dungeon.device.level", playerDungeonLevel.level),
                textRenderer
            )
        )

        addDrawableChild(
            ButtonWidget.builder(SHOW_ATTRIBUTES_BUTTON_TEXT) { toggleButton ->
                showLevelAttributes = !showLevelAttributes
                toggleButton.x = getToggleButtonX()
                toggleButton.message = if (showLevelAttributes) HIDE_ATTRIBUTES_BUTTON_TEXT else SHOW_ATTRIBUTES_BUTTON_TEXT
            }.dimensions(
                getToggleButtonX(),
                (height - SHOW_ATTRIBUTES_BUTTON_HEIGHT) / 2,
                SHOW_ATTRIBUTES_BUTTON_WIDTH,
                SHOW_ATTRIBUTES_BUTTON_HEIGHT
            ).build()
        )

        val commandButtonsX = (width - backgroundWidth) / 2 - 20 - COMMAND_BUTTONS_OFFSET
        val commandButtonsY = (height - backgroundHeight) / 2
        addDrawableChild(
            TexturedButtonWidget(commandButtonsX, commandButtonsY, 20, 18, TOTEMS_BUTTON_TEXTURES) { button ->
                MinecraftClient.getInstance().networkHandler?.sendChatCommand(TotemCommand.NAME)
            }.apply {
                setTooltip(Tooltip.of(TOTEMS_BUTTON_TOOLTIP_TEXT))
            }
        )
        addDrawableChild(
            TexturedButtonWidget(commandButtonsX, commandButtonsY + (18 + COMMAND_BUTTONS_OFFSET), 20, 18, FILTER_BUTTON_TEXTURES) { button ->
                MinecraftClient.getInstance().networkHandler?.sendChatCommand(ItemFilterCommand.NAME)
            }.apply {
                setTooltip(Tooltip.of(FILTER_BUTTON_TOOLTIP_TEXT))
            }
        )
        addDrawableChild(
            TexturedButtonWidget(commandButtonsX, commandButtonsY + 2 * (18 + COMMAND_BUTTONS_OFFSET), 20, 18, KILLER_BUTTON_TEXTURES) { button ->
                MinecraftClient.getInstance().networkHandler?.sendChatCommand(KillerCommand.NAME)
            }.apply {
                setTooltip(Tooltip.of(KILLER_BUTTON_TOOLTIP_TEXT))
            }
        )
        addDrawableChild(
            TexturedButtonWidget(
                commandButtonsX,
                commandButtonsY + 3 * (18 + COMMAND_BUTTONS_OFFSET),
                20,
                18,
                TRAINING_BUTTON_TEXTURES,
                ::onCreateTrainingDungeonButtonPress
            ).apply { setTooltip(Tooltip.of(TRAINING_BUTTON_TOOLTIP_TEXT)) }
        )

        initLevelScalingDetails(playerDungeonLevel.level)
    }

    private fun getToggleButtonX(): Int {
        val hiddenX = (width + backgroundWidth) / 2
        if (!showLevelAttributes) return hiddenX
        return min(width - SHOW_ATTRIBUTES_BUTTON_WIDTH - 5, hiddenX + ATTRIBUTE_PANEL_MAX_WIDTH)
    }

    private fun initLevelScalingDetails(dungeonLevel: Int) {
        levelScalingTextWidgets = mutableListOf<TextWidget>()

        val x = (width + backgroundWidth) / 2 + 6
        var y = (height - backgroundHeight) / 2 + 6
        val width = getLevelScalingTextWidgetWidth()

        val enemyAttributes = EnemyLevelScalingSettings.getEnemyLevelAttributes(dungeonLevel)
        if (enemyAttributes.isNotEmpty()) {
            y += initLevelScalingHeader(ENEMY_ATTRIBUTES_TEXT, x, y, width) + 3
            y += initLevelScalingAttributeTextsPart(enemyAttributes, x, y, width)
            y += 5
        }

        val bossAttributes = EnemyLevelScalingSettings.getBossLevelAttributes(dungeonLevel)
        if (bossAttributes.isNotEmpty()) {
            y += initLevelScalingHeader(BOSS_ATTRIBUTES_TEXT, x, y, width) + 3
            y += initLevelScalingAttributeTextsPart(bossAttributes, x, y, width)
            y += 5
        }
    }

    private fun getLevelScalingTextWidgetWidth(): Int {
        val xStart = (width + backgroundWidth) / 2 + 5
        val widgetWidth = ((width - SHOW_ATTRIBUTES_BUTTON_WIDTH - 10) - xStart)
        return min(widgetWidth, ATTRIBUTE_PANEL_MAX_WIDTH - 10)
    }

    private fun initLevelScalingHeader(
        text: Text,
        x: Int,
        y: Int,
        width: Int,
    ): Int {
        levelScalingTextWidgets.add(
            ScalableTextWidget(x, y, width, ATTRIBUTE_HEADER_WIDGET_HEIGHT, text, textRenderer, 1f)
        )
        return ATTRIBUTE_HEADER_WIDGET_HEIGHT
    }

    private fun initLevelScalingAttributeTextsPart(
        attributes: List<CustomAttribute>,
        xStart: Int,
        yStart: Int,
        width: Int,
    ): Int {
        if (attributes.isEmpty()) return 0

        val attributeTextCommand = GetCustomAttributesTextsCommand(attributes)
        val cmd = CommandGateway.apply(attributeTextCommand)

        val widgetWidth = (width / ATTRIBUTE_TEXT_WIDGET_SCALE).toInt()

        for ((index, text) in cmd.texts.withIndex()) {
            levelScalingTextWidgets.add(
                ScalableTextWidget(
                    xStart,
                    yStart + index * ATTRIBUTE_TEXT_WIDGET_Y_OFFSET,
                    widgetWidth,
                    ATTRIBUTE_TEXT_WIDGET_HEIGHT,
                    text,
                    textRenderer,
                    ATTRIBUTE_TEXT_WIDGET_SCALE
                )
            )
        }

        return (attributes.size - 1) * ATTRIBUTE_TEXT_WIDGET_Y_OFFSET + ceil(ATTRIBUTE_TEXT_WIDGET_HEIGHT * ATTRIBUTE_TEXT_WIDGET_SCALE).toInt()
    }

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        super.render(context, mouseX, mouseY, delta)
        if (showLevelAttributes) renderAttributesPanel(context, mouseX, mouseY, delta)

        val progressScreenRect = getProgressScreenRect()
        context.drawTexture(
            RenderPipelines.GUI_TEXTURED,
            PROGRESS_TEXTURE,
            progressScreenRect.left, progressScreenRect.top,
            0F, 8F * handler.payload.playerDungeonLevel.levelProgress,
            progressScreenRect.width, progressScreenRect.height,
            30, 24,
        )

        drawMouseoverTooltip(context, mouseX, mouseY)
    }

    override fun drawBackground(context: DrawContext, delta: Float, mouseX: Int, mouseY: Int) {
        val textureX = (width - backgroundWidth) / 2
        val textureY = (height - backgroundHeight) / 2

        context.drawTexture(
            RenderPipelines.GUI_TEXTURED,
            TEXTURE,
            textureX,
            textureY,
            0.0f,
            0.0f,
            backgroundWidth,
            backgroundHeight,
            256,
            256
        )
    }

    override fun drawMouseoverTooltip(drawContext: DrawContext, mouseX: Int, mouseY: Int) {
        super.drawMouseoverTooltip(drawContext, mouseX, mouseY)

        val progressScreenRect = getProgressScreenRect()
        if (mouseX < progressScreenRect.left || mouseX > progressScreenRect.right ||
            mouseY < progressScreenRect.top || mouseY > progressScreenRect.bottom
        ) return
        drawContext.drawTooltip(
            this.textRenderer,
            Text.translatable(
                "container.mcendgame.dungeon_device.progress_tooltip",
                handler.payload.playerDungeonLevel.levelProgress,
                DungeonLevelSettings.LEVEL_INCREASE_THRESHOLD
            ),
            mouseX,
            mouseY,
        )
    }

    private fun getProgressScreenRect() = ScreenRect((width - backgroundWidth) / 2 + 9, (height - backgroundHeight) / 2 + 45, 30, 8)

    private fun renderAttributesPanel(
        context: DrawContext,
        mouseX: Int,
        mouseY: Int,
        delta: Float,
    ) {
        val x1 = (width + backgroundWidth) / 2
        val y1 = (height - backgroundHeight) / 2
        var x2 = width - SHOW_ATTRIBUTES_BUTTON_WIDTH - 5

        val w = min(x2 - x1, ATTRIBUTE_PANEL_MAX_WIDTH)
        x2 = x1 + w

        context.drawTexture(
            RenderPipelines.GUI_TEXTURED,
            ATTRIBUTE_PANEL_TEXTURE,
            x1,
            y1,
            0f,
            0f,
            ATTRIBUTE_PANEL_TEXTURE_EDGE_WIDTH,
            backgroundHeight,
            256,
            256
        )

        context.drawTexture(
            RenderPipelines.GUI_TEXTURED,
            ATTRIBUTE_PANEL_TEXTURE,
            x1 + ATTRIBUTE_PANEL_TEXTURE_EDGE_WIDTH,
            y1,
            ATTRIBUTE_PANEL_TEXTURE_EDGE_WIDTH.toFloat(),
            0f,
            w - 2 * ATTRIBUTE_PANEL_TEXTURE_EDGE_WIDTH,
            backgroundHeight,
            256,
            256
        )

        context.drawTexture(
            RenderPipelines.GUI_TEXTURED,
            ATTRIBUTE_PANEL_TEXTURE,
            x2 - ATTRIBUTE_PANEL_TEXTURE_EDGE_WIDTH,
            y1,
            176f - ATTRIBUTE_PANEL_TEXTURE_EDGE_WIDTH,
            0f,
            ATTRIBUTE_PANEL_TEXTURE_EDGE_WIDTH,
            backgroundHeight,
            256,
            256
        )

        levelScalingTextWidgets.forEach { it.render(context, mouseX, mouseY, delta) }
    }

    private fun onCreateDungeonButtonPress(button: ButtonWidget) {
        ClientPlayNetworking.send(handler.payload)
        close()
        log.info("Dungeon opened by ${inventory.player.gameProfile.name}")
    }

    private fun onCreateTrainingDungeonButtonPress(button: ButtonWidget) {
        val payload = DungeonDeviceTrainingPayload.from(handler.payload)
        ClientPlayNetworking.send(payload)
        close()
        log.info("Training dungeon opened by ${inventory.player.gameProfile.name}")
    }
}