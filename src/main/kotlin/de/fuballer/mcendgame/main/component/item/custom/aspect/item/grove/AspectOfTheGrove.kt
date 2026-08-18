package de.fuballer.mcendgame.main.component.item.custom.aspect.item.grove

import de.fuballer.mcendgame.main.component.item.custom.aspect.AspectItem
import de.fuballer.mcendgame.main.component.item.custom.aspect.AspectItems
import net.minecraft.network.chat.Component

class AspectOfTheGrove(
    settings: Properties,
) : AspectItem(settings) {
    companion object {
        const val MIN_DUNGEON_LEVEL = 10

        const val MIN_DROP_LEVEL = 10
        private const val BASE_DROP_PROBABILITY = 0.05
        private const val DROP_PROBABILITY_PER_LEVEL = 0.0025
        fun getDropProbability(level: Int) = if (level < MIN_DROP_LEVEL) 0.0 else BASE_DROP_PROBABILITY + DROP_PROBABILITY_PER_LEVEL * (level - MIN_DROP_LEVEL)
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
        AspectItems.ASPECT_OF_TYRANNY,
        AspectItems.ASPECT_OF_GREED,
        AspectItems.ASPECT_OF_DOMINION,
        AspectItems.ASPECT_OF_HORDES,
        AspectItems.ASPECT_OF_CURIO,
        AspectItems.ASPECT_OF_FORTUNE,
        AspectItems.ASPECT_OF_EMINENCE,
        AspectItems.ASPECT_OF_ANCESTORS,
        AspectItems.ASPECT_OF_ZEAL,
    )
}