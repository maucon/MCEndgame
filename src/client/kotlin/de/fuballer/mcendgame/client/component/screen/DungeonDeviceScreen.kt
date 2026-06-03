package de.fuballer.mcendgame.client.component.screen

import com.mojang.logging.LogUtils
import de.fuballer.mcendgame.main.component.block.blocks.dungeon_device.DungeonDeviceScreenHandler
import de.fuballer.mcendgame.main.component.block.blocks.dungeon_device.networking.DungeonDeviceTrainingPayload
import de.fuballer.mcendgame.main.component.block.blocks.dungeon_device.networking.UpdateDungeonLevelPayload
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
import net.minecraft.util.Colors
import net.minecraft.util.Formatting
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min

private val TEXTURE = IdentifierUtil.default("textures/gui/container/dungeon_device.png")

private val OPEN_DUNGEON_BUTTON_TEXT = Text.translatable("container.mcendgame.dungeon_device.open")

private val LEVEL_INCREASE_TEXT = Text.literal("+")
private val LEVEL_INCREASE_TOOLTIP = Text.translatable("container.mcendgame.dungeon_device.level_increase_tooltip")
private const val LEVEL_INCREASE_LIMIT_TOOLTIP_KEY = "container.mcendgame.dungeon_device.level_increase_tooltip_limit"
private val LEVEL_DECREASE_TEXT = Text.literal("-")
private val LEVEL_DECREASE_TOOLTIP = Text.translatable("container.mcendgame.dungeon_device.level_decrease_tooltip")
private val LEVEL_DECREASE_ABOVE_INCREASE_LIMIT_TOOLTIP = Text.translatable("container.mcendgame.dungeon_device.level_decrease_above_increase_limit_tooltip")
    .styled { style -> style.withBold(false).withColor(Formatting.RED) }
private val LEVEL_LOCKED_TEXT = Text.literal("🔒")
private val LEVEL_UNLOCKED_TEXT = Text.literal("\uD83D\uDD13")
private val LEVEL_UNLOCK_TOOLTIP = Text.translatable("container.mcendgame.dungeon_device.level_unlock_tooltip")
private val LEVEL_LOCK_TOOLTIP = Text.translatable("container.mcendgame.dungeon_device.level_lock_tooltip")
private val LEVEL_LOCK_EXPLANATION_TOOLTIP = Text.translatable("container.mcendgame.dungeon_device.level_lock_explanation_tooltip")
private val LEVEL_UNLOCK_EXPLANATION_TOOLTIP = Text.translatable("container.mcendgame.dungeon_device.level_unlock_explanation_tooltip")
private val PROGRESS_TEXTURE = IdentifierUtil.default("textures/gui/sprites/dungeon_device/progress.png")

private const val LEVEL_BUTTONS_SIZE = 12
private const val LEVEL_BUTTONS_X_OFFSET = 7
private const val LEVEL_BUTTONS_Y_OFFSET = 25
private const val LEVEL_BUTTONS_Y_SPACING = 2

private const val LEVEL_TEXT_X_OFFSET = LEVEL_BUTTONS_X_OFFSET + LEVEL_BUTTONS_SIZE + 4
private const val LEVEL_TEXT_Y_OFFSET = 35
private const val LEVEL_TEXT_SCALING = 0.95f

private val ATTRIBUTE_PANEL_TEXTURE = IdentifierUtil.default("textures/gui/container/dungeon_device_attribute_panel.png")
private val ENEMY_ATTRIBUTES_TEXT = Text.translatable("container.mcendgame.dungeon_device.enemy_attributes")
private val BOSS_ATTRIBUTES_TEXT = Text.translatable("container.mcendgame.dungeon_device.boss_attributes")
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

    val playerDungeonLevel = handler.payload.playerDungeonLevel

    private val createDungeonButton = ButtonWidget
        .builder(OPEN_DUNGEON_BUTTON_TEXT, ::onCreateDungeonButtonPress)
        .size(36, 12)
        .build()

    private lateinit var increaseLevelButton: ButtonWidget
    private lateinit var decreaseLevelButton: ButtonWidget
    private lateinit var lockLevelButton: ButtonWidget

    override fun init() {
        super.init()
        titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2

        val basePanelX = (width - backgroundWidth) / 2
        val basePanelY = (height - backgroundHeight) / 2

        createDungeonButton.setPosition(basePanelX + 70, basePanelY + 62)
        addDrawableChild(createDungeonButton)

        val levelButtonsX = basePanelX + LEVEL_BUTTONS_X_OFFSET
        val plusButtonY = basePanelY + LEVEL_BUTTONS_Y_OFFSET
        val minusButtonY = plusButtonY + LEVEL_BUTTONS_SIZE + LEVEL_BUTTONS_Y_SPACING
        val lockButtonY = minusButtonY + LEVEL_BUTTONS_SIZE + LEVEL_BUTTONS_Y_SPACING

        increaseLevelButton = ButtonWidget.builder(LEVEL_INCREASE_TEXT) {
            playerDungeonLevel.level = min(playerDungeonLevel.level + 1, DungeonLevelSettings.getClientSetLevelLimit(playerDungeonLevel.highestReached))
            playerDungeonLevel.levelProgress = 0
            updateLevelButtons()
            sendUpdateDungeonLevelPayload()
        }
            .dimensions(levelButtonsX, plusButtonY, LEVEL_BUTTONS_SIZE, LEVEL_BUTTONS_SIZE)
            .build()

        decreaseLevelButton = ButtonWidget.builder(LEVEL_DECREASE_TEXT) {
            playerDungeonLevel.level = max(playerDungeonLevel.level - 1, 1)
            playerDungeonLevel.levelProgress = 0
            updateLevelButtons()
            sendUpdateDungeonLevelPayload()
        }
            .dimensions(levelButtonsX, minusButtonY, LEVEL_BUTTONS_SIZE, LEVEL_BUTTONS_SIZE)
            .build()

        lockLevelButton = ButtonWidget.builder(Text.empty()) {
            playerDungeonLevel.locked = !playerDungeonLevel.locked
            updateLevelButtons()
            sendUpdateDungeonLevelPayload()
        }
            .dimensions(levelButtonsX, lockButtonY, LEVEL_BUTTONS_SIZE, LEVEL_BUTTONS_SIZE)
            .build()

        updateLevelButtons()

        addDrawableChild(increaseLevelButton)
        addDrawableChild(decreaseLevelButton)
        addDrawableChild(lockLevelButton)

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

        val commandButtonsX = basePanelX - 20 - COMMAND_BUTTONS_OFFSET
        addDrawableChild(
            TexturedButtonWidget(commandButtonsX, basePanelY, 20, 18, TOTEMS_BUTTON_TEXTURES) { button ->
                MinecraftClient.getInstance().networkHandler?.sendChatCommand(TotemCommand.NAME)
            }.apply {
                setTooltip(Tooltip.of(TOTEMS_BUTTON_TOOLTIP_TEXT))
            }
        )
        addDrawableChild(
            TexturedButtonWidget(commandButtonsX, basePanelY + (18 + COMMAND_BUTTONS_OFFSET), 20, 18, FILTER_BUTTON_TEXTURES) { button ->
                MinecraftClient.getInstance().networkHandler?.sendChatCommand(ItemFilterCommand.NAME)
            }.apply {
                setTooltip(Tooltip.of(FILTER_BUTTON_TOOLTIP_TEXT))
            }
        )
        addDrawableChild(
            TexturedButtonWidget(commandButtonsX, basePanelY + 2 * (18 + COMMAND_BUTTONS_OFFSET), 20, 18, KILLER_BUTTON_TEXTURES) { button ->
                MinecraftClient.getInstance().networkHandler?.sendChatCommand(KillerCommand.NAME)
            }.apply {
                setTooltip(Tooltip.of(KILLER_BUTTON_TOOLTIP_TEXT))
            }
        )
        addDrawableChild(
            TexturedButtonWidget(
                commandButtonsX,
                basePanelY + 3 * (18 + COMMAND_BUTTONS_OFFSET),
                20,
                18,
                TRAINING_BUTTON_TEXTURES,
                ::onCreateTrainingDungeonButtonPress
            ).apply { setTooltip(Tooltip.of(TRAINING_BUTTON_TOOLTIP_TEXT)) }
        )

        initLevelScalingDetails(playerDungeonLevel.level)
    }

    private fun updateLevelButtons() {
        val playerLevel = playerDungeonLevel.level
        val highestReached = playerDungeonLevel.highestReached
        val increaseLimit = DungeonLevelSettings.getClientSetLevelLimit(highestReached)

        // INCREASE BUTTON
        val increaseBlocked = playerLevel + 1 > increaseLimit
        val increaseTooltipText = LEVEL_INCREASE_TOOLTIP.copy()
        if (increaseBlocked) increaseTooltipText.append(Text.literal("\n"))
            .append(
                Text.translatable(
                    LEVEL_INCREASE_LIMIT_TOOLTIP_KEY,
                    increaseLimit,
                    (DungeonLevelSettings.CLIENT_SET_LEVEL_LIMIT_PERCENTAGE * 100).toInt(),
                    highestReached,
                ).styled { style -> style.withBold(false).withColor(Formatting.DARK_GRAY) }
            )
        increaseLevelButton.setTooltip(Tooltip.of(increaseTooltipText))

        increaseLevelButton.active = !increaseBlocked

        // DECREASE BUTTON
        val decreaseTooltipText = LEVEL_DECREASE_TOOLTIP.copy()
        if (playerLevel > increaseLimit) decreaseTooltipText.append(Text.literal("\n"))
            .append(LEVEL_DECREASE_ABOVE_INCREASE_LIMIT_TOOLTIP.styled { style -> style.withBold(false).withColor(Formatting.RED) })
        decreaseLevelButton.setTooltip(Tooltip.of(decreaseTooltipText))

        decreaseLevelButton.active = playerLevel > 1

        // LOCK BUTTON
        val locked = playerDungeonLevel.locked
        val lockButtonText = if (locked) LEVEL_LOCKED_TEXT else LEVEL_UNLOCKED_TEXT
        lockLevelButton.message = lockButtonText

        val lockTooltipText = if (locked) LEVEL_UNLOCK_TOOLTIP.copy() else LEVEL_LOCK_TOOLTIP.copy()
        val lockExplanationText = if (locked) LEVEL_UNLOCK_EXPLANATION_TOOLTIP else LEVEL_LOCK_EXPLANATION_TOOLTIP
        lockTooltipText.append("\n").append(lockExplanationText.styled { style -> style.withBold(false).withColor(Formatting.DARK_GRAY) })
        lockLevelButton.setTooltip(Tooltip.of(lockTooltipText))
    }

    private fun getToggleButtonX(): Int {
        val hiddenX = (width + backgroundWidth) / 2
        if (!showLevelAttributes) return hiddenX
        return min(width - SHOW_ATTRIBUTES_BUTTON_WIDTH - 5, hiddenX + ATTRIBUTE_PANEL_MAX_WIDTH)
    }

    private fun initLevelScalingDetails(dungeonLevel: Int) {
        levelScalingTextWidgets = mutableListOf()

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

        context.matrices.pushMatrix()
        context.matrices.scale(LEVEL_TEXT_SCALING, LEVEL_TEXT_SCALING, context.matrices)

        context.drawText(
            textRenderer,
            Text.translatable(
                "text.mcendgame.dungeon.device.level",
                playerDungeonLevel.level
            ),
            (((width - backgroundWidth) / 2 + LEVEL_TEXT_X_OFFSET) / LEVEL_TEXT_SCALING).toInt(),
            (((height - backgroundHeight) / 2 + LEVEL_TEXT_Y_OFFSET) / LEVEL_TEXT_SCALING).toInt(),
            Colors.WHITE,
            true
        )

        context.matrices.popMatrix()

        val progressScreenRect = getProgressScreenRect()
        context.drawTexture(
            RenderPipelines.GUI_TEXTURED,
            PROGRESS_TEXTURE,
            progressScreenRect.left, progressScreenRect.top,
            0F, 8F * playerDungeonLevel.levelProgress,
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
                playerDungeonLevel.levelProgress,
                DungeonLevelSettings.LEVEL_INCREASE_THRESHOLD
            ),
            mouseX,
            mouseY,
        )
    }

    private fun getProgressScreenRect() = ScreenRect(
        (width - backgroundWidth) / 2 + LEVEL_TEXT_X_OFFSET,
        (height - backgroundHeight) / 2 + LEVEL_TEXT_Y_OFFSET + 10,
        30,
        8,
    )

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

    private fun sendUpdateDungeonLevelPayload() {
        val payload = UpdateDungeonLevelPayload(inventory.player.uuid, playerDungeonLevel)
        ClientPlayNetworking.send(payload)
    }
}