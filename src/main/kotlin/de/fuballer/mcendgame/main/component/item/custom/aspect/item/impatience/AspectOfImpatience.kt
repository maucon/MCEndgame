package de.fuballer.mcendgame.main.component.item.custom.aspect.item.impatience

import de.fuballer.mcendgame.main.component.item.custom.aspect.AspectItem
import net.minecraft.network.chat.Component

class AspectOfImpatience(
    settings: Properties,
) : AspectItem(settings) {
    companion object {
        const val ADDITIONAL_LEVELS = 2
    }

    override val tier = 2
    override val limit = 4
    override val description = mutableListOf(Component.translatable(TRANSLATABLE_DESCRIPTION_KEY + "impatience", ADDITIONAL_LEVELS))
    override val disabledAspects = listOf<AspectItem>()
}