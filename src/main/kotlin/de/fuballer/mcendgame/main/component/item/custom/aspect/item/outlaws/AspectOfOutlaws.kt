package de.fuballer.mcendgame.main.component.item.custom.aspect.item.outlaws

import de.fuballer.mcendgame.main.component.item.custom.aspect.AspectItem
import net.minecraft.network.chat.Component

class AspectOfOutlaws(
    settings: Properties,
) : AspectItem(settings) {
    companion object {
        const val ADDITIONAL_BANDITS = 2
    }

    override val tier = 2
    override val limit = 4
    override val description = mutableListOf(Component.translatable(TRANSLATABLE_DESCRIPTION_KEY + "outlaws", ADDITIONAL_BANDITS))
    override val disabledAspects = listOf<AspectItem>()
}