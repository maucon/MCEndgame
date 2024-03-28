package de.fuballer.mcendgame.component.custom_entity.types.naga

import de.fuballer.mcendgame.component.custom_entity.types.poison_spit.PoisonSpitEntityType
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.EnemyUtil
import de.fuballer.mcendgame.util.extension.EntityExtension.getCustomEntityType
import org.bukkit.Sound
import org.bukkit.entity.AbstractArrow
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.LlamaSpit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.ProjectileLaunchEvent

@Component
class NagaService : Listener {
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    fun on(event: ProjectileLaunchEvent) {
        val projectile = event.entity as? AbstractArrow ?: return
        val shooter = projectile.shooter as? LivingEntity ?: return

        if (shooter.getCustomEntityType() != NagaEntityType) return

        val spit = EnemyUtil.shootCustomProjectile(shooter, projectile, EntityType.LLAMA_SPIT, Sound.ENTITY_LLAMA_SPIT) as LlamaSpit
        spit.customName = PoisonSpitEntityType.customName

        event.isCancelled = true
    }
}
