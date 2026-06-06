package de.fuballer.mcendgame.main.component.item.custom.aspect.item.savagery

import de.fuballer.mcendgame.main.component.custom_attribute.AttributeFormats
import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.item.custom.aspect.AspectItem
import net.minecraft.network.chat.Component

class AspectOfSavagery(
    settings: Properties,
) : AspectItem(settings) {
    companion object {
        const val MORE_DAMAGE = 0.3
        val MORE_DAMAGE_ROLL = DoubleRoll(DoubleBounds(MORE_DAMAGE))
        val MORE_DAMAGE_ATTRIBUTE = CustomAttribute(CustomAttributeTypes.MORE_DAMAGE, roll = MORE_DAMAGE_ROLL)

        const val INCREASED_LOOT = 0.2
        val INCREASED_LOOT_ROLL = DoubleRoll(DoubleBounds(INCREASED_LOOT))
        val INCREASED_LOOT_ATTRIBUTE = CustomAttribute(CustomAttributeTypes.DROP_INCREASED_LOOT, roll = INCREASED_LOOT_ROLL)
    }

    override val tier = 2
    override val limit = 2
    override val description = mutableListOf(
        Component.translatable(TRANSLATABLE_DESCRIPTION_KEY + "savagery_0", AttributeFormats.formatDouble(MORE_DAMAGE * 100)),
        Component.translatable(TRANSLATABLE_DESCRIPTION_KEY + "savagery_1", AttributeFormats.formatDouble(INCREASED_LOOT * 100)),
    )
    override val disabledAspects = listOf<AspectItem>()
}