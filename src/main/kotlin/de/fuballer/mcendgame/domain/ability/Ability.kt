package de.fuballer.mcendgame.domain.ability

import org.bukkit.entity.LivingEntity

interface Ability {
    fun cast(caster: LivingEntity, target: LivingEntity)
}
