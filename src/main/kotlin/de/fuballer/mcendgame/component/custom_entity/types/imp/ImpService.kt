package de.fuballer.mcendgame.component.custom_entity.types.imp

import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.technical.extension.EntityExtension.getCustomEntityType
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
        if (entity.getCustomEntityType() != ImpEntityType) return

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