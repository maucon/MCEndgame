package de.fuballer.mcendgame.component.totem.totems.attack_speed

import de.fuballer.mcendgame.component.totem.data.AttributeEffectServiceBase
import de.fuballer.mcendgame.framework.annotation.Service
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier

@Service
class AttackSpeedEffectService : AttributeEffectServiceBase(
    AttackSpeedTotemType,
    Attribute.GENERIC_ATTACK_SPEED,
    AttributeModifier.Operation.ADD_SCALAR
)