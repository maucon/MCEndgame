package de.fuballer.mcendgame.component.totem.totems.movement_speed

import de.fuballer.mcendgame.component.totem.data.AttributeEffectServiceBase
import de.fuballer.mcendgame.framework.annotation.Component
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier

@Component
class MovementSpeedEffectService : AttributeEffectServiceBase(
    MovementSpeedTotemType,
    Attribute.GENERIC_MOVEMENT_SPEED,
    AttributeModifier.Operation.ADD_SCALAR
)