package de.fuballer.mcendgame.component.totem.data

import de.fuballer.mcendgame.component.item.attribute.data.AttributeRoll
import de.fuballer.mcendgame.component.item.attribute.data.AttributeType
import de.fuballer.mcendgame.component.totem.TotemSettings
import de.fuballer.mcendgame.util.TextComponent
import net.kyori.adventure.text.Component

interface AttributeTotemType : TotemType {
    val attributeType: AttributeType

    fun getAttributeRollsByTier(tier: TotemTier): List<AttributeRoll<*>>

    override fun getLore(tier: TotemTier): List<Component> {
        val attributeRolls = getAttributeRollsByTier(tier)
        val stringLore = attributeType.lore(attributeRolls)
        return listOf(TextComponent.create(stringLore, TotemSettings.LORE_COLOR))
    }
}