package de.fuballer.mcendgame.component.custom_entity.ability

import org.bukkit.entity.LivingEntity

interface Ability {
    fun cast(caster: LivingEntity)
}
