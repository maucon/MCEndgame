package de.fuballer.mcendgame.main.component.item.custom.aspect.item.hordes

import de.fuballer.mcendgame.main.component.item.custom.aspect.AspectItem
import net.minecraft.network.chat.Component

class AspectOfHordes(
    settings: Properties,
) : AspectItem(settings) {
    companion object {
        const val INCREASED_ENEMIES = 0.15
    }

    override val tier = 2
    override val limit = 4
    override val description = mutableListOf(Component.translatable(TRANSLATABLE_DESCRIPTION_KEY + "hordes", (INCREASED_ENEMIES * 100).toInt()))
    override val disabledAspects = listOf<AspectItem>()
}