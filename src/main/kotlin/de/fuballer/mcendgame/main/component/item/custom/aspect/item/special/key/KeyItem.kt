package de.fuballer.mcendgame.main.component.item.custom.aspect.item.special.key

import de.fuballer.mcendgame.main.component.dungeon.type.DungeonType
import de.fuballer.mcendgame.main.component.item.custom.aspect.AspectItem
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent

abstract class KeyItem(
    settings: Properties,
) : AspectItem(settings) {
    companion object {
        const val KEY_TRANSLATABLE_BASE_KEY = ASPECT_TRANSLATABLE_BASE_KEY + "key."
        const val KEY_TRANSLATABLE_DESCRIPTION_KEY = KEY_TRANSLATABLE_BASE_KEY + "description."
        val DISABLE_ASPECTS_DESCRIPTION = Component.translatable(KEY_TRANSLATABLE_DESCRIPTION_KEY + "disable_aspects")
    }

    abstract val specificDescription: List<MutableComponent>

    override val description
        get() = specificDescription + mutableListOf(DISABLE_ASPECTS_DESCRIPTION)

    override val tier = 0
    override val limit = 1
    override val disabledAspects = listOf<AspectItem>() // handled in AspectService

    abstract val dungeonType: DungeonType
}