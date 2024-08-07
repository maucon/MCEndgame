package de.fuballer.mcendgame.component.custom_entity.ability.abilities

import de.fuballer.mcendgame.component.custom_entity.ability.Ability
import de.fuballer.mcendgame.component.custom_entity.ability.AbilitySettings
import de.fuballer.mcendgame.component.custom_entity.summoner.SummonerUtil
import de.fuballer.mcendgame.component.custom_entity.types.chupacabra.ChupacabraEntityType
import de.fuballer.mcendgame.util.DungeonUtil
import org.bukkit.Particle
import org.bukkit.entity.Creature
import org.bukkit.entity.LivingEntity
import kotlin.math.min

private const val MAX_CHUPACABRA = 5
private const val MAX_CHUPACABRA_PER_EXTRA_PLAYER = 3
private const val CHUPACABRA_PER_CAST = 2
private const val CHUPACABRA_PER_CAST_PER_EXTRA_PLAYER = 1

object SummonChupacabraAbility : Ability {
    override fun canCast(caster: LivingEntity): Boolean {
        val spawnAmount = getChupacabraSpawnAmount(caster)
        return spawnAmount > 0
    }

    override fun cast(caster: LivingEntity) {
        val creature = caster as? Creature ?: return

        val spawnAmount = getChupacabraSpawnAmount(caster)
        if (spawnAmount <= 0) return

        SummonerUtil.summonMinions(creature, ChupacabraEntityType, spawnAmount, creature.location)

        val location = caster.eyeLocation
        caster.world.spawnParticle(
            Particle.ASH,
            location.x, location.y, location.z,
            100, 0.5, 0.5, 0.5, 0.0001
        )
    }

    private fun getChupacabraSpawnAmount(caster: LivingEntity): Int {
        val targets = DungeonUtil.getNearbyPlayers(caster, AbilitySettings.DEFAULT_TARGET_RANGE).size
        val extraPlayers = targets - 1

        val maxChupacabra = MAX_CHUPACABRA + MAX_CHUPACABRA_PER_EXTRA_PLAYER * extraPlayers
        val amountToCap = maxChupacabra - SummonerUtil.getMinionEntities(caster).size

        val amount = CHUPACABRA_PER_CAST + CHUPACABRA_PER_CAST_PER_EXTRA_PLAYER * extraPlayers
        return min(amount, amountToCap)
    }
}