package de.fuballer.mcendgame.component.custom_entity.ability.abilities

import de.fuballer.mcendgame.component.custom_entity.ability.Ability
import de.fuballer.mcendgame.component.custom_entity.ability.AbilitySettings
import de.fuballer.mcendgame.util.DungeonUtil
import org.bukkit.entity.Creature
import org.bukkit.entity.LivingEntity

object ChangeTargetAbility : Ability {
    override fun cast(caster: LivingEntity) {
        val creature = caster as? Creature ?: return

        val targets = DungeonUtil.getNearbyPlayers(caster, AbilitySettings.DEFAULT_TARGET_RANGE)
        if (targets.isEmpty()) return

        creature.target = targets.random()
    }
}