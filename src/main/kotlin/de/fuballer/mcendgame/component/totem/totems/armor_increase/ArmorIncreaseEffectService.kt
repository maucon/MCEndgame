package de.fuballer.mcendgame.component.totem.totems.armor_increase

import de.fuballer.mcendgame.component.totem.data.AttributeEffectServiceBase
import de.fuballer.mcendgame.framework.annotation.Component
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier

@Component
class ArmorIncreaseEffectService : AttributeEffectServiceBase(
    ArmorIncreaseTotemType,
    Attribute.GENERIC_ARMOR,
    AttributeModifier.Operation.ADD_SCALAR
)