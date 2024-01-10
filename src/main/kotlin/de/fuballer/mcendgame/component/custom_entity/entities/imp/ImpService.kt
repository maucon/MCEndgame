package de.fuballer.mcendgame.component.custom_entity.entities.imp

import de.fuballer.mcendgame.component.custom_entity.data.CustomEntityType
import de.fuballer.mcendgame.framework.annotation.Component
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.entity.Creature
import org.bukkit.entity.EntityType
import org.bukkit.entity.SmallFireball
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityShootBowEvent

@Component
class ImpService : Listener {
    @EventHandler
    fun onEntityShootBow(event: EntityShootBowEvent) {
        val entity = event.entity
        if (!CustomEntityType.isType(entity, CustomEntityType.IMP)) return

        val fireball = entity.world.spawnEntity(event.projectile.location, EntityType.SMALL_FIREBALL, false) as SmallFireball
        fireball.shooter = entity

        entity.world.playSound(entity.location, Sound.ITEM_FIRECHARGE_USE, SoundCategory.HOSTILE, 1f, 1f)

        event.isCancelled = true

        val creature = entity as? Creature ?: return
        val target = creature.target ?: return

        val vector = target.location.subtract(creature.location).toVector().normalize()
        fireball.velocity = vector
        fireball.direction = fireball.velocity
    }
}