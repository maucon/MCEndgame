package de.fuballer.mcendgame.component.custom_entity.types.elf_duelist

import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.EntityUtil
import de.fuballer.mcendgame.util.SchedulingUtil
import de.fuballer.mcendgame.util.extension.EntityExtension.getCustomEntityType
import de.fuballer.mcendgame.util.extension.EventExtension.cancel
import org.bukkit.Particle
import org.bukkit.entity.Arrow
import org.bukkit.entity.Creature
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.util.Vector

@Component
class ElfDuelistService : Listener {
    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    fun on(event: EntityDamageByEntityEvent) {
        if (event.damager.getCustomEntityType() == ElfDuelistEntityType) onEntityDamageByElfDuelist(event)
        if (event.entity.getCustomEntityType() == ElfDuelistEntityType) onElfDuelistDamageByEntity(event)
    }

    private fun onElfDuelistDamageByEntity(event: EntityDamageByEntityEvent) {
        val arrow = event.damager as? Arrow ?: return
        if (!ElfDuelistSettings.doesReflectArrow()) return

        val shooter = EntityUtil.getLivingEntityDamager(arrow) ?: return

        val reflectTarget = shooter.location.toVector().add(shooter.eyeLocation.toVector()).divide(Vector(2, 2, 2))
        val distance = reflectTarget.subtract(arrow.location.toVector())
        val addedYVelocity = ElfDuelistSettings.getReflectAddedYVelocity(distance.length())
        val newVelocity = distance.normalize().multiply(ElfDuelistSettings.REFLECT_ARROW_VELOCITY).add(addedYVelocity)

        arrow.velocity = newVelocity
        (event.entity as Creature).swingMainHand()
        event.cancel()

        val arrowLocation = arrow.location
        arrow.world.spawnParticle(Particle.CRIT, arrowLocation.x, arrowLocation.y, arrowLocation.z, 10, 0.1, 0.1, 0.1, 0.25)

        SchedulingUtil.runTaskLater(1L) {
            arrow.velocity = newVelocity
        }
    }

    private fun onEntityDamageByElfDuelist(event: EntityDamageByEntityEvent) {
        val direction = event.damager.location.toVector().subtract(event.entity.location.toVector())
        direction.multiply(Vector(1, 0, 1))
        direction.normalize()

        val velocity = direction.multiply(ElfDuelistSettings.RETREAT_VELOCITY).add(ElfDuelistSettings.RETREAT_ADDED_Y_VELOCITY)
        event.damager.velocity = velocity
    }
}
