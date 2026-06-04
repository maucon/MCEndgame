package de.fuballer.mcendgame.main.component.item.custom.totem

import de.fuballer.mcendgame.main.component.dungeon.loot.drop.ItemColor
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent

private const val TRANSLATABLE_BASE_KEY = "item.mcendgame.totem.type."

enum class TotemType(
    private val id: String,
    val color: ItemColor,
) {
    BASIC("basic", ItemColor.TOTEM_BASIC),
    EFFECT("effect", ItemColor.TOTEM_EFFECT),
    ULTIMATE("ultimate", ItemColor.TOTEM_ULTIMATE);

    fun getLore(): MutableComponent = Component.translatable("$TRANSLATABLE_BASE_KEY$id")
}