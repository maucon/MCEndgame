package de.fuballer.mcendgame.component.artifact.artifacts.movement_speed

import de.fuballer.mcendgame.component.artifact.data.AttributeEffectServiceBase
import de.fuballer.mcendgame.framework.annotation.Component
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier

@Component
class MovementSpeedEffectService : AttributeEffectServiceBase(
    MovementSpeedArtifactType,
    Attribute.GENERIC_MOVEMENT_SPEED,
    AttributeModifier.Operation.ADD_SCALAR
)