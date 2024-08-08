package de.fuballer.mcendgame.component.totem.totems.armor

import de.fuballer.mcendgame.component.totem.data.AttributeEffectServiceBase
import de.fuballer.mcendgame.framework.annotation.Service
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier

@Service
class ArmorEffectService : AttributeEffectServiceBase(
    ArmorTotemType,
    Attribute.GENERIC_ARMOR,
    AttributeModifier.Operation.ADD_NUMBER
)