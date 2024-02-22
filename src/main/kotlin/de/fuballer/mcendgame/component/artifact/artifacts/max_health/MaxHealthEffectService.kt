package de.fuballer.mcendgame.component.artifact.artifacts.max_health

import de.fuballer.mcendgame.component.artifact.data.AttributeEffectServiceBase
import de.fuballer.mcendgame.framework.annotation.Component
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier

@Component
class MaxHealthEffectService : AttributeEffectServiceBase(
    MaxHealthArtifactType,
    Attribute.GENERIC_MAX_HEALTH,
    AttributeModifier.Operation.ADD_NUMBER
)