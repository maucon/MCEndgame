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
import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.components.*
import net.minecraft.client.gui.navigation.ScreenRectangle
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.network.chat.Component
import net.minecraft.util.CommonColors
import net.minecraft.world.entity.player.Inventory
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min

private val TEXTURE = IdentifierUtil.default("textures/gui/container/dungeon_device.png")

private val OPEN_DUNGEON_BUTTON_TEXT = Component.translatable("container.mcendgame.dungeon_device.open")

private val LEVEL_INCREASE_TEXT = Component.literal("+")
private val LEVEL_INCREASE_TOOLTIP = Component.translatable("container.mcendgame.dungeon_device.level_increase_tooltip")
private const val LEVEL_INCREASE_LIMIT_TOOLTIP_KEY = "container.mcendgame.dungeon_device.level_increase_tooltip_limit"
private val LEVEL_DECREASE_TEXT = Component.literal("-")
private val LEVEL_DECREASE_TOOLTIP = Component.translatable("container.mcendgame.dungeon_device.level_decrease_tooltip")
private val LEVEL_DECREASE_ABOVE_INCREASE_LIMIT_TOOLTIP = Component.translatable("container.mcendgame.dungeon_device.level_decrease_above_increase_limit_tooltip")
    .withStyle { style -> style.withBold(false).withColor(ChatFormatting.RED) }
private val LEVEL_LOCKED_TEXT = Component.literal("🔒")
private val LEVEL_UNLOCKED_TEXT = Component.literal("\uD83D\uDD13")
private val LEVEL_UNLOCK_TOOLTIP = Component.translatable("container.mcendgame.dungeon_device.level_unlock_tooltip")
private val LEVEL_LOCK_TOOLTIP = Component.translatable("container.mcendgame.dungeon_device.level_lock_tooltip")
private val LEVEL_LOCK_EXPLANATION_TOOLTIP = Component.translatable("container.mcendgame.dungeon_device.level_lock_explanation_tooltip")
private val LEVEL_UNLOCK_EXPLANATION_TOOLTIP = Component.translatable("container.mcendgame.dungeon_device.level_unlock_explanation_tooltip")
private val PROGRESS_TEXTURE = IdentifierUtil.default("textures/gui/sprites/dungeon_device/progress.png")

private const val LEVEL_BUTTONS_SIZE = 12
private const val LEVEL_BUTTONS_X_OFFSET = 7
private const val LEVEL_BUTTONS_Y_OFFSET = 25
private const val LEVEL_BUTTONS_Y_SPACING = 2

private const val LEVEL_TEXT_X_OFFSET = LEVEL_BUTTONS_X_OFFSET + LEVEL_BUTTONS_SIZE + 4
private const val LEVEL_TEXT_Y_OFFSET = 35
private const val LEVEL_TEXT_SCALING = 0.95f

private val ATTRIBUTE_PANEL_TEXTURE = IdentifierUtil.default("textures/gui/container/dungeon_device_attribute_panel.png")
private val ENEMY_ATTRIBUTES_TEXT = Component.translatable("container.mcendgame.dungeon_device.enemy_attributes")
private val BOSS_ATTRIBUTES_TEXT = Component.translatable("container.mcendgame.dungeon_device.boss_attributes")
private const val ATTRIBUTE_PANEL_TEXTURE_EDGE_WIDTH = 4
private const val ATTRIBUTE_PANEL_MAX_WIDTH = 150
private const val ATTRIBUTE_HEADER_WIDGET_HEIGHT = 10
private const val ATTRIBUTE_TEXT_WIDGET_SCALE = 0.75f
private const val ATTRIBUTE_TEXT_WIDGET_HEIGHT = 10
private const val ATTRIBUTE_TEXT_WIDGET_Y_OFFSET = 10

private const val SHOW_ATTRIBUTES_BUTTON_WIDTH = 10
private const val SHOW_ATTRIBUTES_BUTTON_HEIGHT = 20
private val SHOW_ATTRIBUTES_BUTTON_TEXT = Component.literal(">")
private val HIDE_ATTRIBUTES_BUTTON_TEXT = Component.literal("<")

private const val COMMAND_BUTTONS_OFFSET = 2
private val TOTEMS_BUTTON_TEXTURES = WidgetSprites(IdentifierUtil.default("dungeon_device/totems"), IdentifierUtil.default("dungeon_device/totems_highlighted"))
private val TOTEMS_BUTTON_TOOLTIP_TEXT = Component.translatable("container.mcendgame.dungeon_device.totems_tooltip")
private val FILTER_BUTTON_TEXTURES = WidgetSprites(IdentifierUtil.default("dungeon_device/filter"), IdentifierUtil.default("dungeon_device/filter_highlighted"))
private val FILTER_BUTTON_TOOLTIP_TEXT = Component.translatable("container.mcendgame.dungeon_device.filter_tooltip")
private val KILLER_BUTTON_TEXTURES = WidgetSprites(IdentifierUtil.default("dungeon_device/killer"), IdentifierUtil.default("dungeon_device/killer_highlighted"))
private val KILLER_BUTTON_TOOLTIP_TEXT = Component.translatable("container.mcendgame.dungeon_device.killer_tooltip")
private val TRAINING_BUTTON_TEXTURES = WidgetSprites(IdentifierUtil.default("dungeon_device/training"), IdentifierUtil.default("dungeon_device/training_highlighted"))
private val TRAINING_BUTTON_TOOLTIP_TEXT = Component.translatable("container.mcendgame.dungeon_device.training_tooltip")

@Environment(EnvType.CLIENT)
class DungeonDeviceScreen(
    handler: DungeonDeviceScreenHandler,
    private val inventory: Inventory,
    title: Component,
) : AbstractContainerScreen<DungeonDeviceScreenHandler>(handler, inventory, title) {
    private val log = LogUtils.getLogger()
    private var showLevelAttributes = false
    private var levelScalingTextWidgets = mutableListOf<StringWidget>()

    val playerDungeonLevel = handler.payload.playerDungeonLevel

    private val createDungeonButton = Button
        .builder(OPEN_DUNGEON_BUTTON_TEXT, ::onCreateDungeonButtonPress)
        .size(36, 12)
        .build()

    private lateinit var increaseLevelButton: Button
    private lateinit var decreaseLevelButton: Button
    private lateinit var lockLevelButton: Button

    override fun init() {
        super.init()
        titleLabelX = (imageWidth - font.width(title)) / 2

        val basePanelX = (width - imageWidth) / 2
        val basePanelY = (height - imageHeight) / 2

        createDungeonButton.setPosition(basePanelX + 70, basePanelY + 62)
        addRenderableWidget(createDungeonButton)

        val levelButtonsX = basePanelX + LEVEL_BUTTONS_X_OFFSET
        val plusButtonY = basePanelY + LEVEL_BUTTONS_Y_OFFSET
        val minusButtonY = plusButtonY + LEVEL_BUTTONS_SIZE + LEVEL_BUTTONS_Y_SPACING
        val lockButtonY = minusButtonY + LEVEL_BUTTONS_SIZE + LEVEL_BUTTONS_Y_SPACING

        increaseLevelButton = Button.builder(LEVEL_INCREASE_TEXT) {
            playerDungeonLevel.level = min(playerDungeonLevel.level + 1, DungeonLevelSettings.getClientSetLevelLimit(playerDungeonLevel.highestReached))
            playerDungeonLevel.levelProgress = 0
            updateDungeonLevel()
        }
            .bounds(levelButtonsX, plusButtonY, LEVEL_BUTTONS_SIZE, LEVEL_BUTTONS_SIZE)
            .build()

        decreaseLevelButton = Button.builder(LEVEL_DECREASE_TEXT) {
            playerDungeonLevel.level = max(playerDungeonLevel.level - 1, 1)
            playerDungeonLevel.levelProgress = 0
            updateDungeonLevel()
        }
            .bounds(levelButtonsX, minusButtonY, LEVEL_BUTTONS_SIZE, LEVEL_BUTTONS_SIZE)
            .build()

        lockLevelButton = Button.builder(Component.empty()) {
            playerDungeonLevel.locked = !playerDungeonLevel.locked
            updateDungeonLevel()
        }
            .bounds(levelButtonsX, lockButtonY, LEVEL_BUTTONS_SIZE, LEVEL_BUTTONS_SIZE)
            .build()

        updateLevelButtons()

        addRenderableWidget(increaseLevelButton)
        addRenderableWidget(decreaseLevelButton)
        addRenderableWidget(lockLevelButton)

        addRenderableWidget(
            Button.builder(SHOW_ATTRIBUTES_BUTTON_TEXT) { toggleButton ->
                showLevelAttributes = !showLevelAttributes
                toggleButton.x = getToggleButtonX()
                toggleButton.message = if (showLevelAttributes) HIDE_ATTRIBUTES_BUTTON_TEXT else SHOW_ATTRIBUTES_BUTTON_TEXT
            }.bounds(
                getToggleButtonX(),
                (height - SHOW_ATTRIBUTES_BUTTON_HEIGHT) / 2,
                SHOW_ATTRIBUTES_BUTTON_WIDTH,
                SHOW_ATTRIBUTES_BUTTON_HEIGHT
            ).build()
        )

        val commandButtonsX = basePanelX - 20 - COMMAND_BUTTONS_OFFSET
        addRenderableWidget(
            ImageButton(commandButtonsX, basePanelY, 20, 18, TOTEMS_BUTTON_TEXTURES) { button ->
                Minecraft.getInstance().connection?.sendCommand(TotemCommand.NAME)
            }.apply {
                setTooltip(Tooltip.create(TOTEMS_BUTTON_TOOLTIP_TEXT))
            }
        )
        addRenderableWidget(
            ImageButton(commandButtonsX, basePanelY + (18 + COMMAND_BUTTONS_OFFSET), 20, 18, FILTER_BUTTON_TEXTURES) { button ->
                Minecraft.getInstance().connection?.sendCommand(ItemFilterCommand.NAME)
            }.apply {
                setTooltip(Tooltip.create(FILTER_BUTTON_TOOLTIP_TEXT))
            }
        )
        addRenderableWidget(
            ImageButton(commandButtonsX, basePanelY + 2 * (18 + COMMAND_BUTTONS_OFFSET), 20, 18, KILLER_BUTTON_TEXTURES) { button ->
                Minecraft.getInstance().connection?.sendCommand(KillerCommand.NAME)
            }.apply {
                setTooltip(Tooltip.create(KILLER_BUTTON_TOOLTIP_TEXT))
            }
        )
        addRenderableWidget(
            ImageButton(
                commandButtonsX,
                basePanelY + 3 * (18 + COMMAND_BUTTONS_OFFSET),
                20,
                18,
                TRAINING_BUTTON_TEXTURES,
                ::onCreateTrainingDungeonButtonPress
            ).apply { setTooltip(Tooltip.create(TRAINING_BUTTON_TOOLTIP_TEXT)) }
        )

        initLevelScalingDetails(playerDungeonLevel.level)
    }

    private fun updateDungeonLevel() {
        updateLevelButtons()
        initLevelScalingDetails(playerDungeonLevel.level)
        sendUpdateDungeonLevelPayload()
    }

    private fun updateLevelButtons() {
        val playerLevel = playerDungeonLevel.level
        val highestReached = playerDungeonLevel.highestReached
        val increaseLimit = DungeonLevelSettings.getClientSetLevelLimit(highestReached)

        // INCREASE BUTTON
        val increaseBlocked = playerLevel + 1 > increaseLimit
        val increaseTooltipText = LEVEL_INCREASE_TOOLTIP.copy()
        if (increaseBlocked) increaseTooltipText.append(Component.literal("\n"))
            .append(
                Component.translatable(
                    LEVEL_INCREASE_LIMIT_TOOLTIP_KEY,
                    increaseLimit,
                    (DungeonLevelSettings.CLIENT_SET_LEVEL_LIMIT_PERCENTAGE * 100).toInt(),
                    highestReached,
                ).withStyle { style -> style.withBold(false).withColor(ChatFormatting.DARK_GRAY) }
            )
        increaseLevelButton.setTooltip(Tooltip.create(increaseTooltipText))

        increaseLevelButton.active = !increaseBlocked

        // DECREASE BUTTON
        val decreaseTooltipText = LEVEL_DECREASE_TOOLTIP.copy()
        if (playerLevel > increaseLimit) decreaseTooltipText.append(Component.literal("\n"))
            .append(LEVEL_DECREASE_ABOVE_INCREASE_LIMIT_TOOLTIP.withStyle { style -> style.withBold(false).withColor(ChatFormatting.RED) })
        decreaseLevelButton.setTooltip(Tooltip.create(decreaseTooltipText))

        decreaseLevelButton.active = playerLevel > 1

        // LOCK BUTTON
        val locked = playerDungeonLevel.locked
        val lockButtonText = if (locked) LEVEL_LOCKED_TEXT else LEVEL_UNLOCKED_TEXT
        lockLevelButton.message = lockButtonText

        val lockTooltipText = if (locked) LEVEL_UNLOCK_TOOLTIP.copy() else LEVEL_LOCK_TOOLTIP.copy()
        val lockExplanationText = if (locked) LEVEL_UNLOCK_EXPLANATION_TOOLTIP else LEVEL_LOCK_EXPLANATION_TOOLTIP
        lockTooltipText.append("\n").append(lockExplanationText.withStyle { style -> style.withBold(false).withColor(ChatFormatting.DARK_GRAY) })
        lockLevelButton.setTooltip(Tooltip.create(lockTooltipText))
    }

    private fun getToggleButtonX(): Int {
        val hiddenX = (width + imageWidth) / 2
        if (!showLevelAttributes) return hiddenX
        return min(width - SHOW_ATTRIBUTES_BUTTON_WIDTH - 5, hiddenX + ATTRIBUTE_PANEL_MAX_WIDTH)
    }

    private fun initLevelScalingDetails(dungeonLevel: Int) {
        levelScalingTextWidgets = mutableListOf()

        val x = (width + imageWidth) / 2 + 6
        var y = (height - imageHeight) / 2 + 6
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
        val xStart = (width + imageWidth) / 2 + 5
        val widgetWidth = ((width - SHOW_ATTRIBUTES_BUTTON_WIDTH - 10) - xStart)
        return min(widgetWidth, ATTRIBUTE_PANEL_MAX_WIDTH - 10)
    }

    private fun initLevelScalingHeader(
        text: Component,
        x: Int,
        y: Int,
        width: Int,
    ): Int {
        levelScalingTextWidgets.add(
            ScalableTextWidget(x, y, width, ATTRIBUTE_HEADER_WIDGET_HEIGHT, text, font, 1f)
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
                    font,
                    ATTRIBUTE_TEXT_WIDGET_SCALE
                )
            )
        }

        return (attributes.size - 1) * ATTRIBUTE_TEXT_WIDGET_Y_OFFSET + ceil(ATTRIBUTE_TEXT_WIDGET_HEIGHT * ATTRIBUTE_TEXT_WIDGET_SCALE).toInt()
    }

    override fun extractContents(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, delta: Float) {
        super.extractContents(graphics, mouseX, mouseY, delta)
        if (showLevelAttributes) renderAttributesPanel(graphics, mouseX, mouseY, delta)

        graphics.pose().pushMatrix()
        graphics.pose().scale(LEVEL_TEXT_SCALING, LEVEL_TEXT_SCALING, graphics.pose())

        graphics.text(
            font,
            Component.translatable(
                "text.mcendgame.dungeon.device.level",
                playerDungeonLevel.level
            ),
            (((width - imageWidth) / 2 + LEVEL_TEXT_X_OFFSET) / LEVEL_TEXT_SCALING).toInt(),
            (((height - imageHeight) / 2 + LEVEL_TEXT_Y_OFFSET) / LEVEL_TEXT_SCALING).toInt(),
            CommonColors.WHITE,
            true
        )

        graphics.pose().popMatrix()

        val progressScreenRect = getProgressScreenRect()
        graphics.blit(
            RenderPipelines.GUI_TEXTURED,
            PROGRESS_TEXTURE,
            progressScreenRect.left(), progressScreenRect.top(),
            0F, 8F * playerDungeonLevel.levelProgress,
            progressScreenRect.width, progressScreenRect.height,
            30, 24,
        )

        extractTooltip(graphics, mouseX, mouseY)
    }

    override fun extractBackground(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, delta: Float) {
        extractTransparentBackground(graphics)
        
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
            256
        )
    }

    override fun extractTooltip(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int) {
        super.extractTooltip(graphics, mouseX, mouseY)

        val progressScreenRect = getProgressScreenRect()
        if (mouseX < progressScreenRect.left() || mouseX > progressScreenRect.right() ||
            mouseY < progressScreenRect.top() || mouseY > progressScreenRect.bottom()
        ) return
        graphics.setTooltipForNextFrame(
            this.font,
            Component.translatable(
                "container.mcendgame.dungeon_device.progress_tooltip",
                playerDungeonLevel.levelProgress,
                DungeonLevelSettings.LEVEL_INCREASE_THRESHOLD
            ),
            mouseX,
            mouseY,
        )
    }

    private fun getProgressScreenRect() = ScreenRectangle(
        (width - imageWidth) / 2 + LEVEL_TEXT_X_OFFSET,
        (height - imageHeight) / 2 + LEVEL_TEXT_Y_OFFSET + 10,
        30,
        8,
    )

    private fun renderAttributesPanel(
        graphics: GuiGraphicsExtractor,
        mouseX: Int,
        mouseY: Int,
        delta: Float,
    ) {
        val x1 = (width + imageWidth) / 2
        val y1 = (height - imageHeight) / 2
        var x2 = width - SHOW_ATTRIBUTES_BUTTON_WIDTH - 5

        val w = min(x2 - x1, ATTRIBUTE_PANEL_MAX_WIDTH)
        x2 = x1 + w

        graphics.blit(
            RenderPipelines.GUI_TEXTURED,
            ATTRIBUTE_PANEL_TEXTURE,
            x1,
            y1,
            0f,
            0f,
            ATTRIBUTE_PANEL_TEXTURE_EDGE_WIDTH,
            imageHeight,
            256,
            256
        )

        graphics.blit(
            RenderPipelines.GUI_TEXTURED,
            ATTRIBUTE_PANEL_TEXTURE,
            x1 + ATTRIBUTE_PANEL_TEXTURE_EDGE_WIDTH,
            y1,
            ATTRIBUTE_PANEL_TEXTURE_EDGE_WIDTH.toFloat(),
            0f,
            w - 2 * ATTRIBUTE_PANEL_TEXTURE_EDGE_WIDTH,
            imageHeight,
            256,
            256
        )

        graphics.blit(
            RenderPipelines.GUI_TEXTURED,
            ATTRIBUTE_PANEL_TEXTURE,
            x2 - ATTRIBUTE_PANEL_TEXTURE_EDGE_WIDTH,
            y1,
            176f - ATTRIBUTE_PANEL_TEXTURE_EDGE_WIDTH,
            0f,
            ATTRIBUTE_PANEL_TEXTURE_EDGE_WIDTH,
            imageHeight,
            256,
            256
        )

        levelScalingTextWidgets.forEach { it.extractWidgetRenderState(graphics, mouseX, mouseY, delta) }
    }

    private fun onCreateDungeonButtonPress(button: Button) {
        ClientPlayNetworking.send(menu.payload)
        onClose()
        log.info("Dungeon opened by ${inventory.player.gameProfile.name}")
    }

    private fun onCreateTrainingDungeonButtonPress(button: Button) {
        val payload = DungeonDeviceTrainingPayload.from(menu.payload)
        ClientPlayNetworking.send(payload)
        onClose()
        log.info("Training dungeon opened by ${inventory.player.gameProfile.name}")
    }

    private fun sendUpdateDungeonLevelPayload() {
        val payload = UpdateDungeonLevelPayload(inventory.player.uuid, playerDungeonLevel)
        ClientPlayNetworking.send(payload)
    }
}