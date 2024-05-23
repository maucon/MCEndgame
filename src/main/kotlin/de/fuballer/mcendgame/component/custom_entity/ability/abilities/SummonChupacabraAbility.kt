package de.fuballer.mcendgame.component.custom_entity.ability.abilities

import de.fuballer.mcendgame.component.custom_entity.ability.Ability
import de.fuballer.mcendgame.component.custom_entity.ability.AbilitySettings
import de.fuballer.mcendgame.component.custom_entity.types.chupacabra.ChupacabraEntityType
import de.fuballer.mcendgame.util.DungeonUtil
import de.fuballer.mcendgame.util.SummonerUtil
import org.bukkit.Particle
import org.bukkit.entity.Creature
import org.bukkit.entity.LivingEntity
import kotlin.math.min

private const val MAX_CHUPACABRA = 5
private const val MAX_CHUPACABRA_PER_EXTRA_PLAYER = 3
private const val CHUPACABRA_PER_CAST = 3

object SummonChupacabraAbility : Ability {
    override fun cast(caster: LivingEntity) {
        val creature = caster as? Creature ?: return

        val targets = DungeonUtil.getNearbyPlayers(caster, AbilitySettings.DEFAULT_TARGET_RANGE)

        val maxChupacabra = MAX_CHUPACABRA + MAX_CHUPACABRA_PER_EXTRA_PLAYER * (targets.size - 1)
        val minions = SummonerUtil.getMinionEntities(creature)

        val spawnAmount = min(CHUPACABRA_PER_CAST, maxChupacabra - minions.size)
        if (spawnAmount <= 0) return

        SummonerUtil.summonerService.summonMinions(
            creature,
            ChupacabraEntityType,
            spawnAmount, creature.location
        )

        val location = caster.eyeLocation
        caster.world.spawnParticle(
            Particle.ASH,
            location.x, location.y, location.z,
            100, 0.5, 0.5, 0.5, 0.0001
        )
    }
}