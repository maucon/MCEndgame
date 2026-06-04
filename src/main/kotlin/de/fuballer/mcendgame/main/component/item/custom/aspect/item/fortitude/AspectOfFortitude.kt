package de.fuballer.mcendgame.main.component.item.custom.aspect.item.fortitude

import de.fuballer.mcendgame.main.component.custom_attribute.AttributeFormats
import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.item.custom.aspect.AspectItem
import net.minecraft.network.chat.Component

class AspectOfFortitude(
    settings: Properties,
) : AspectItem(settings) {
    companion object {
        const val LESS_DAMAGE_TAKEN = 0.3
        val LESS_DAMAGE_TAKEN_ROLL = DoubleRoll(DoubleBounds(-LESS_DAMAGE_TAKEN))
        val LESS_DAMAGE_TAKEN_ATTRIBUTE = CustomAttribute(CustomAttributeTypes.MORE_DAMAGE_TAKEN, roll = LESS_DAMAGE_TAKEN_ROLL)

        const val INCREASED_LOOT = 0.2
        val INCREASED_LOOT_ROLL = DoubleRoll(DoubleBounds(INCREASED_LOOT))
        val INCREASED_LOOT_ATTRIBUTE = CustomAttribute(CustomAttributeTypes.DROP_INCREASED_LOOT, roll = INCREASED_LOOT_ROLL)
    }

    override val tier = 2
    override val limit = 2
    override val description = mutableListOf(
        Component.translatable(TRANSLATABLE_DESCRIPTION_KEY + "fortitude_0", AttributeFormats.formatDouble(LESS_DAMAGE_TAKEN * 100)),
        Component.translatable(TRANSLATABLE_DESCRIPTION_KEY + "fortitude_1", AttributeFormats.formatDouble(INCREASED_LOOT * 100)),
    )
    override val disabledAspects = listOf<AspectItem>()
}