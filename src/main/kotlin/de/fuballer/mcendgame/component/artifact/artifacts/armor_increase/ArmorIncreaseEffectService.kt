package de.fuballer.mcendgame.component.artifact.artifacts.armor_increase

import de.fuballer.mcendgame.component.artifact.artifacts.armor_toughness.ArmorToughnessArtifactType
import de.fuballer.mcendgame.component.artifact.data.AttributeEffectServiceBase
import de.fuballer.mcendgame.framework.annotation.Component
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier

@Component
class ArmorIncreaseEffectService : AttributeEffectServiceBase(
    ArmorToughnessArtifactType,
    Attribute.GENERIC_ARMOR,
    AttributeModifier.Operation.ADD_SCALAR
)