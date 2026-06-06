package de.fuballer.mcendgame.main.component.item.custom.aspect.item.tyranny

import de.fuballer.mcendgame.main.component.item.custom.aspect.AspectItem
import net.minecraft.network.chat.Component

class AspectOfTyranny(
    settings: Properties,
) : AspectItem(settings) {
    companion object {
        const val ADDITIONAL_ELITES = 2
    }

    override val tier = 2
    override val limit = 4
    override val description = mutableListOf(Component.translatable(TRANSLATABLE_DESCRIPTION_KEY + "tyranny", ADDITIONAL_ELITES))
    override val disabledAspects = listOf<AspectItem>()
}