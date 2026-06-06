package de.fuballer.mcendgame.main.component.item.custom.aspect.item.ancestors

import de.fuballer.mcendgame.main.component.item.custom.aspect.AspectItem
import net.minecraft.network.chat.Component

class AspectOfAncestors(
    settings: Properties,
) : AspectItem(settings) {
    override val tier = 2
    override val limit = 4
    override val description = mutableListOf(Component.translatable(TRANSLATABLE_DESCRIPTION_KEY + "ancestors"))
    override val disabledAspects = listOf<AspectItem>()
}