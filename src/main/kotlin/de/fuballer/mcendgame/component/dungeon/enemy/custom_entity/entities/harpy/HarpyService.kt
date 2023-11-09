package de.fuballer.mcendgame.component.dungeon.enemy.custom_entity.entities.harpy

import de.fuballer.mcendgame.component.dungeon.enemy.custom_entity.data.CustomEntityType
import de.fuballer.mcendgame.framework.annotation.Component
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.entity.EntityType
import org.bukkit.entity.Snowball
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityShootBowEvent

@Component
class HarpyService : Listener {
    @EventHandler
    fun onEntityShootBow(event: EntityShootBowEvent) {
        val entity = event.entity
        if (!CustomEntityType.isType(entity, CustomEntityType.HARPY)) return

        val snowball = entity.world.spawnEntity(event.projectile.location, EntityType.SNOWBALL) as Snowball
        snowball.velocity = event.projectile.velocity

        snowball.shooter = entity

        entity.world.playSound(entity.location, Sound.ENTITY_SNOWBALL_THROW, SoundCategory.HOSTILE, 1f, 1f)

        event.isCancelled = true
    }
}