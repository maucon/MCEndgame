package de.fuballer.mcendgame.component.custom_entity.ability.abilities

import de.fuballer.mcendgame.component.custom_entity.ability.Ability
import org.bukkit.entity.Creature
import org.bukkit.entity.LivingEntity
import org.bukkit.util.Vector

private const val LEAP_RANGE = 0.25

object LeapAbility : Ability {
    override fun cast(caster: LivingEntity) {
        val creature = caster as? Creature ?: return
        val target = creature.target ?: return

        val vec = target.location.subtract(caster.location).multiply(LEAP_RANGE)
        caster.velocity = Vector(vec.x, vec.y + vec.length() / 4, vec.z)
    }
}