package de.fuballer.mcendgame.component.totem.totems.attack_damage

import de.fuballer.mcendgame.component.totem.data.AttributeEffectServiceBase
import de.fuballer.mcendgame.framework.annotation.Service
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier

@Service
class AttackDamageEffectService : AttributeEffectServiceBase(
    AttackDamageTotemType,
    Attribute.GENERIC_ATTACK_DAMAGE,
    AttributeModifier.Operation.ADD_NUMBER
)