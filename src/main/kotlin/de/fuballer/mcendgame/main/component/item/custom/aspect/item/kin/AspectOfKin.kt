package de.fuballer.mcendgame.main.component.item.custom.aspect.item.kin

import de.fuballer.mcendgame.main.component.item.custom.aspect.AspectItem
import net.minecraft.network.chat.Component

class AspectOfKin(
    settings: Properties,
) : AspectItem(settings) {
    override val tier = 2
    override val limit = 1
    override val description = mutableListOf(Component.translatable(TRANSLATABLE_DESCRIPTION_KEY + "kin"))
    override val disabledAspects = listOf<AspectItem>()
}