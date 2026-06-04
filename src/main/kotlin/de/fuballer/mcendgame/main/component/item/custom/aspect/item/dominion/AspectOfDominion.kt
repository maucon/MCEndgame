package de.fuballer.mcendgame.main.component.item.custom.aspect.item.dominion

import de.fuballer.mcendgame.main.component.item.custom.aspect.AspectItem
import net.minecraft.network.chat.Component

class AspectOfDominion(
    settings: Properties,
) : AspectItem(settings) {
    override val tier = 2
    override val limit = 2
    override val description = mutableListOf(Component.translatable(TRANSLATABLE_DESCRIPTION_KEY + "dominion"))
    override val disabledAspects = listOf<AspectItem>()
}