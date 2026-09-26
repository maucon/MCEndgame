package de.fuballer.mcendgame.main.component.item.custom.aspect.item.ascension

import de.fuballer.mcendgame.main.component.custom_attribute.AttributeFormats
import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.item.custom.aspect.AspectItem
import net.minecraft.network.chat.Component

class AspectOfAscension(
    settings: Properties,
) : AspectItem(settings) {
    companion object {
        const val MORE_DAMAGE_TAKEN = -0.5
        val MORE_DAMAGE_TAKEN_ATTRIBUTE = RollableCustomAttribute(CustomAttributeTypes.MORE_DAMAGE_TAKEN, 0, DoubleBounds(MORE_DAMAGE_TAKEN))

        const val MORE_DAMAGE_DEALT = 0.2
        val MORE_DAMAGE_DEALT_ATTRIBUTE = RollableCustomAttribute(CustomAttributeTypes.MORE_DAMAGE, 0, DoubleBounds(MORE_DAMAGE_DEALT))
    }

    override val tier = 2
    override val limit = 1
    override val description = mutableListOf(
        Component.translatable(TRANSLATABLE_DESCRIPTION_KEY + "ascension_0", AttributeFormats.formatDouble(-MORE_DAMAGE_TAKEN * 100)),
        Component.translatable(TRANSLATABLE_DESCRIPTION_KEY + "ascension_1", AttributeFormats.formatDouble(MORE_DAMAGE_DEALT * 100)),
        Component.translatable(TRANSLATABLE_DESCRIPTION_KEY + "ascension_2"),
    )
    override val disabledAspects = listOf<AspectItem>()
}