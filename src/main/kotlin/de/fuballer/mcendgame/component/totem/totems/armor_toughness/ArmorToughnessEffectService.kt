package de.fuballer.mcendgame.component.totem.totems.armor_toughness

import de.fuballer.mcendgame.component.totem.data.AttributeEffectServiceBase
import de.fuballer.mcendgame.framework.annotation.Component
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier

@Component
class ArmorToughnessEffectService : AttributeEffectServiceBase(
    ArmorToughnessTotemType,
    Attribute.GENERIC_ARMOR_TOUGHNESS,
    AttributeModifier.Operation.ADD_NUMBER
)