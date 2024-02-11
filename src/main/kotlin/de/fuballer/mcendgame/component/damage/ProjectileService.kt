package de.fuballer.mcendgame.component.damage

import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.technical.extension.ProjectileExtension.setAddedBaseDamage
import de.fuballer.mcendgame.util.DamageUtil
import de.fuballer.mcendgame.util.WorldUtil
import org.bukkit.attribute.Attribute
import org.bukkit.entity.AbstractArrow
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityShootBowEvent

@Component
class ProjectileService : Listener {
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    fun on(event: EntityShootBowEvent) {
        if (WorldUtil.isDungeonWorld(event.entity.world)) return // only for vanilla worlds
        val projectile = event.projectile as? AbstractArrow ?: return

        val addedDamage = getAddedDamage(event.entity)
        projectile.setAddedBaseDamage(addedDamage)
    }

    private fun getAddedDamage(entity: LivingEntity): Double {
        var damage = entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.value ?: return 0.0
        damage -= entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.baseValue ?: 0.0
        damage -= DamageUtil.getStrengthDamage(entity)

        return damage
    }
}