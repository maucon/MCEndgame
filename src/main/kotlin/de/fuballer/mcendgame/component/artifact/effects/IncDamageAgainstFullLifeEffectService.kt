package de.fuballer.mcendgame.component.artifact.effects

import de.fuballer.mcendgame.domain.ArtifactType
import de.fuballer.mcendgame.event.DamageCalculationEvent
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.ArtifactUtil
import de.fuballer.mcendgame.util.EventUtil
import de.fuballer.mcendgame.util.WorldUtil
import org.bukkit.Color
import org.bukkit.Particle
import org.bukkit.attribute.Attribute
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageByEntityEvent

@Component
class IncDamageAgainstFullLifeEffectService {
    @EventHandler
    fun on(event: DamageCalculationEvent) {
        if (WorldUtil.isNotDungeonWorld(event.player.world)) return

        val tier = ArtifactUtil.getHighestTier(event.player, ArtifactType.INC_DMG_AGAINST_FULL_LIFE) ?: return

        if (event.damaged.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value > event.damaged.health + 0.1) return

        spawnParticles(event.damaged)

        val (increasedDamage) = ArtifactType.INC_DMG_AGAINST_FULL_LIFE.values[tier]!!
        val moreDamage = increasedDamage / 100

        event.moreDamage.add(moreDamage)
    }

    private fun spawnParticles(entity: LivingEntity) {
        val location = entity.location
        val dustOptions = Particle.DustOptions(Color.fromRGB(255, 50, 50), 1.0f)

        entity.world.spawnParticle(
            Particle.REDSTONE,
            location.x, location.y + 1.3, location.z,
            25, 0.2, 0.3, 0.2, 0.01, dustOptions
        )
    }
}