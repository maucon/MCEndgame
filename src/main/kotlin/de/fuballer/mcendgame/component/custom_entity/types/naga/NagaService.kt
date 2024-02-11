package de.fuballer.mcendgame.component.custom_entity.types.naga

import de.fuballer.mcendgame.component.custom_entity.types.poison_spit.PoisonSpitEntityType
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.technical.extension.EntityExtension.getCustomEntityType
import de.fuballer.mcendgame.util.EnemyUtil
import org.bukkit.Sound
import org.bukkit.entity.AbstractArrow
import org.bukkit.entity.EntityType
import org.bukkit.entity.LlamaSpit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityShootBowEvent

@Component
class NagaService : Listener {
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    fun onEntityShootBow(event: EntityShootBowEvent) {
        val entity = event.entity
        if (entity.getCustomEntityType() != NagaEntityType) return
        val projectile = event.projectile as? AbstractArrow ?: return

        val spit = EnemyUtil.shootCustomProjectile(entity, projectile, EntityType.LLAMA_SPIT, Sound.ENTITY_LLAMA_SPIT) as LlamaSpit
        spit.customName = PoisonSpitEntityType.customName

        event.isCancelled = true
    }
}
