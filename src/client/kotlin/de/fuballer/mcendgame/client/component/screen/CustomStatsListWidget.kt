package de.fuballer.mcendgame.client.component.screen

import de.fuballer.mcendgame.main.component.stats.CustomStatsRegistry
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.ObjectSelectionList
import net.minecraft.network.chat.CommonComponents
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.stats.Stat
import net.minecraft.stats.StatFormatter
import net.minecraft.stats.Stats

@Environment(EnvType.CLIENT)
class CustomStatsListWidget(
    client: Minecraft,
    width: Int,
    contentHeight: Int,
) : ObjectSelectionList<CustomStatsListWidget.Entry>(
    client, width, contentHeight, 33, 14
) {
    init {
        val statHandler = client.player?.stats
        if (statHandler != null) {
            for (id in CustomStatsRegistry.ENTRIES) {
                val stat = Stats.CUSTOM.get(id, StatFormatter.DEFAULT)
                addEntry(Entry(stat, statHandler.getValue(stat)))
            }
        }
    }

    override fun getRowWidth(): Int = 280

    override fun renderListBackground(context: GuiGraphics) {}

    override fun renderListSeparators(context: GuiGraphics) {}

    @Environment(EnvType.CLIENT)
    inner class Entry(
        private val stat: Stat<Identifier>,
        private val value: Int,
    ) : ObjectSelectionList.Entry<Entry>() {

        private val displayName: Component = Component.translatable(
            "stat.${stat.value.namespace}.${stat.value.path}"
        )

        private fun getFormatted(): String = stat.format(value)

        override fun renderContent(
            context: GuiGraphics,
            mouseX: Int,
            mouseY: Int,
            hovered: Boolean,
            deltaTicks: Float,
        ) {
            val textRenderer = minecraft.font
            val y = contentYMiddle - textRenderer.lineHeight / 2
            val index = this@CustomStatsListWidget.children().indexOf(this)
            val color = if (index % 2 == 0) -1 else -4539718 // white / gray alternating

            // Draw stat name on the left
            context.drawString(textRenderer, displayName, contentX + 2, y, color)

            // Draw stat value on the right
            val formatted = getFormatted()
            context.drawString(
                textRenderer,
                formatted,
                contentRight - textRenderer.width(formatted) - 4,
                y,
                color,
            )
        }

        override fun getNarration(): Component = Component.translatable(
            "narrator.select",
            Component.empty()
                .append(displayName)
                .append(CommonComponents.SPACE)
                .append(getFormatted()),
        )
    }
}