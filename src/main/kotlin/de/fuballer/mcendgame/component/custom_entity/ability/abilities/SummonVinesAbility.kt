package de.fuballer.mcendgame.component.custom_entity.ability.abilities

import de.fuballer.mcendgame.component.custom_entity.ability.Ability
import de.fuballer.mcendgame.component.custom_entity.ability.AbilitySettings
import de.fuballer.mcendgame.component.custom_entity.summoner.SummonerUtil
import de.fuballer.mcendgame.component.custom_entity.types.vine.VineEntityType
import de.fuballer.mcendgame.util.DungeonUtil
import de.fuballer.mcendgame.util.extension.EntityExtension.getMapTier
import org.bukkit.entity.Creature
import org.bukkit.entity.LivingEntity
import org.bukkit.util.Vector
import kotlin.random.Random

private fun getSummonVineAmountPerTarget(bossLevel: Int) = (3 + bossLevel / 5)

object SummonVinesAbility : Ability {
    override fun canCast(caster: LivingEntity): Boolean {
        val targets = DungeonUtil.getNearbyPlayers(caster, AbilitySettings.DEFAULT_TARGET_RANGE)
        return targets.isNotEmpty()
    }

    override fun cast(caster: LivingEntity) {
        val creature = caster as? Creature ?: return

        val mapTier = creature.getMapTier() ?: 1
        val amount = getSummonVineAmountPerTarget(mapTier)
        val targets = DungeonUtil.getNearbyPlayers(caster, AbilitySettings.DEFAULT_TARGET_RANGE)

        targets
            .flatMap { SummonerUtil.summonMinions(creature, VineEntityType, amount, it.location) }
            .forEach {
                it.velocity = Vector(1 - Random.nextDouble() * 2, 0.2 + Random.nextDouble() * 0.1, 1 - Random.nextDouble() * 2)
                it.isSilent = true
            }
    }
}