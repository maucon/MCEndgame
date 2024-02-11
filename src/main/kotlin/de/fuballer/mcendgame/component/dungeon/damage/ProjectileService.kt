package de.fuballer.mcendgame.component.dungeon.damage

import de.fuballer.mcendgame.framework.annotation.Component
import org.bukkit.event.Listener

@Component
class ProjectileService : Listener {
//    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
//    fun on(event: EntityShootBowEvent) {
//        if (WorldUtil.isNotDungeonWorld(event.entity.world)) return
//        val projectile = event.projectile as? AbstractArrow ?: return
//
//        val addedDamage = getAddedDamage(event.entity)
//        projectile.setAddedBaseDamage(addedDamage)
//    }
//
//    private fun getAddedDamage(entity: LivingEntity): Double {
//        var damage = entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.value ?: return 0.0
//
//        if (entity is Player) {
//            damage -= entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.baseValue ?: 0.0
//            damage -= DamageUtil.getStrengthDamage(entity)
//        }
//
//        return damage
//    }
}