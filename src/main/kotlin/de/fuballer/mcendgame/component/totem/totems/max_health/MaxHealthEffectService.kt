package de.fuballer.mcendgame.component.totem.totems.max_health

import de.fuballer.mcendgame.component.totem.data.AttributeEffectServiceBase
import de.fuballer.mcendgame.framework.annotation.Service
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier

@Service
class MaxHealthEffectService : AttributeEffectServiceBase(
    MaxHealthTotemType,
    Attribute.GENERIC_MAX_HEALTH,
    AttributeModifier.Operation.ADD_NUMBER
)