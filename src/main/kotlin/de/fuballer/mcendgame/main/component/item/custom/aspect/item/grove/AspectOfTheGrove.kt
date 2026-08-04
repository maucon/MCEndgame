package de.fuballer.mcendgame.main.component.item.custom.aspect.item.grove

import de.fuballer.mcendgame.main.component.item.custom.aspect.AspectItem
import de.fuballer.mcendgame.main.component.item.custom.aspect.AspectItems
import net.minecraft.network.chat.Component

class AspectOfTheGrove(
    settings: Properties,
) : AspectItem(settings) {
    companion object {
        const val MIN_DUNGEON_LEVEL = 10
    }

    override val tier = 0
    override val limit = 1
    override val description = mutableListOf(
        Component.translatable(TRANSLATABLE_DESCRIPTION_KEY + "grove_0"),
        Component.translatable(TRANSLATABLE_DESCRIPTION_KEY + "grove_1", MIN_DUNGEON_LEVEL),
    )
    override val disabledAspects = listOf(
        AspectItems.ASPECT_OF_GHOSTS,
        AspectItems.ASPECT_OF_DUALITY,
        // TODO go through all and decide
    )
}