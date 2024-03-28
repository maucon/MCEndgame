package de.fuballer.mcendgame.component.damage

import de.fuballer.mcendgame.component.item.attribute.AttributeType
import de.fuballer.mcendgame.component.item.equipment.Equipment
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.DamageUtil
import de.fuballer.mcendgame.util.extension.ProjectileExtension.setAddedBaseDamage
import de.fuballer.mcendgame.util.extension.WorldExtension.isDungeonWorld
import org.bukkit.attribute.Attribute
import org.bukkit.entity.AbstractArrow
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.ProjectileLaunchEvent

@Component
class ProjectileService : Listener {
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    fun on(event: ProjectileLaunchEvent) {
        val projectile = event.entity as? AbstractArrow ?: return
        val shooter = projectile.shooter as? LivingEntity ?: return
        val isDungeonWorld = shooter.world.isDungeonWorld()

        val addedDamage = getAddedDamage(shooter, isDungeonWorld)
        projectile.setAddedBaseDamage(addedDamage)
    }

    private fun getAddedDamage(shooter: LivingEntity, isDungeonWorld: Boolean): Double {
        val attribute = shooter.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE) ?: return 0.0

        var damage = attribute.value
        if (isDungeonWorld && shooter !is Player) {
            damage -= attribute.baseValue
            damage -= DamageUtil.getStrengthDamage(shooter)
        }

        shooter.equipment?.itemInMainHand?.also { item ->
            val equipment = Equipment.fromMaterial(item.type) ?: return@also
            val itemAttribute = equipment.baseAttributes.find { it.type == AttributeType.ATTACK_DAMAGE } ?: return@also

            damage -= itemAttribute.roll - attribute.baseValue
        }

        return damage
    }
}