package de.fuballer.mcendgame.main.component.item.custom.aspect.item.greed

import de.fuballer.mcendgame.main.component.item.custom.aspect.AspectItem
import net.minecraft.network.chat.Component

class AspectOfGreed(
    settings: Properties,
) : AspectItem(settings) {
    companion object {
        const val ADDITIONAL_LOOT_GOBLINS = 2
    }

    override val tier = 2
    override val limit = 4
    override val description = mutableListOf(Component.translatable(TRANSLATABLE_DESCRIPTION_KEY + "greed", ADDITIONAL_LOOT_GOBLINS))
    override val disabledAspects = listOf<AspectItem>()
}