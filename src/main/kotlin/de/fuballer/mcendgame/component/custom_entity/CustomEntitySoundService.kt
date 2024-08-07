package de.fuballer.mcendgame.component.custom_entity

import de.fuballer.mcendgame.framework.annotation.Service
import de.fuballer.mcendgame.util.extension.EntityExtension.getCustomEntityType
import org.bukkit.SoundCategory
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDeathEvent

@Service
class CustomEntitySoundService : Listener {
    @EventHandler
    fun on(event: EntityDamageEvent) {
        val entity = event.entity
        val sounds = getSounds(entity) ?: return

        if (entity is LivingEntity && event.finalDamage >= entity.health) return

        entity.world.playSound(entity.location, sounds.hurt, SoundCategory.HOSTILE, 1.0f, 1.0f)
    }

    @EventHandler
    fun on(event: EntityDeathEvent) {
        val entity = event.entity
        val sounds = getSounds(entity) ?: return

        entity.world.playSound(entity.location, sounds.death, SoundCategory.HOSTILE, 1.0f, 1.0f)
    }

    private fun getSounds(entity: Entity): EntitySoundData? {
        val type = entity.getCustomEntityType() ?: return null
        return type.sounds
    }
}