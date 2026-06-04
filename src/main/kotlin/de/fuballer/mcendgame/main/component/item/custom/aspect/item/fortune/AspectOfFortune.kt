package de.fuballer.mcendgame.main.component.item.custom.aspect.item.fortune

import de.fuballer.mcendgame.main.component.item.custom.aspect.AspectItem
import net.minecraft.network.chat.Component

class AspectOfFortune(
    settings: Properties,
) : AspectItem(settings) {
    override val tier = 2
    override val limit = 1
    override val description = mutableListOf(Component.translatable(TRANSLATABLE_DESCRIPTION_KEY + "fortune"))
    override val disabledAspects = listOf<AspectItem>()
}