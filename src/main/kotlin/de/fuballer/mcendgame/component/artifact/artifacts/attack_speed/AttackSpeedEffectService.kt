package de.fuballer.mcendgame.component.artifact.artifacts.attack_speed

import de.fuballer.mcendgame.component.artifact.data.AttributeEffectServiceBase
import de.fuballer.mcendgame.framework.annotation.Component
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier

@Component
class AttackSpeedEffectService : AttributeEffectServiceBase(
    AttackSpeedArtifactType,
    Attribute.GENERIC_ATTACK_SPEED,
    AttributeModifier.Operation.ADD_SCALAR
)