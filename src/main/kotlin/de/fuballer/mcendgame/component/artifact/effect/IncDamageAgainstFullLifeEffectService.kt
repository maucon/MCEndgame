package de.fuballer.mcendgame.component.artifact.effect

import de.fuballer.mcendgame.domain.artifact.ArtifactType
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.ArtifactUtil
import de.fuballer.mcendgame.util.EventUtil
import de.fuballer.mcendgame.util.WorldUtil
import org.bukkit.Color
import org.bukkit.Particle
import org.bukkit.attribute.Attribute
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageByEntityEvent

@Component
class IncDamageAgainstFullLifeEffectService {
    @EventHandler
    fun on(event: EntityDamageByEntityEvent) {
        if (WorldUtil.isNotDungeonWorld(event.entity.world)) return

        val player = EventUtil.getPlayerDamager(event) ?: return
        process(player, event)
    }

    private fun process(player: Player, event: EntityDamageByEntityEvent) {
        val tier = ArtifactUtil.getHighestTier(player, ArtifactType.INC_DMG_AGAINST_FULL_LIFE) ?: return

        val entity = event.entity as? LivingEntity ?: return
        if (entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value > entity.health + 0.1) return

        spawnParticles(entity)

        val (increasedDamage) = ArtifactType.INC_DMG_AGAINST_FULL_LIFE.values[tier]!!
        val dmgMultiplier = 1 + increasedDamage / 100

        event.damage *= dmgMultiplier
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