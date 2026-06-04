package de.fuballer.mcendgame.main.component.item.custom.aspect.item.duality

import de.fuballer.mcendgame.main.component.item.custom.aspect.AspectItem
import net.minecraft.network.chat.Component

class AspectOfDuality(
    settings: Properties,
) : AspectItem(settings) {
    override val tier = 2
    override val limit = 1
    override val description = mutableListOf(
        Component.translatable(TRANSLATABLE_DESCRIPTION_KEY + "duality_0"),
        Component.translatable(TRANSLATABLE_DESCRIPTION_KEY + "duality_1"),
    )
    override val disabledAspects = listOf<AspectItem>()
}