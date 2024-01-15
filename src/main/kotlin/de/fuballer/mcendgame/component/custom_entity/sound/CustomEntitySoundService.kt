package de.fuballer.mcendgame.component.custom_entity.sound

import de.fuballer.mcendgame.domain.persistent_data.DataTypeKeys
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.PersistentDataUtil
import org.bukkit.SoundCategory
import org.bukkit.entity.Entity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDeathEvent

@Component
class CustomEntitySoundService : Listener {
    @EventHandler
    fun onEntityDamage(event: EntityDamageEvent) {
        val entity = event.entity
        val sounds = getSounds(entity) ?: return

        entity.world.playSound(entity.location, sounds.hurt, SoundCategory.HOSTILE, 1.0f, 1.0f)
    }

    @EventHandler
    fun onEntityDeath(event: EntityDeathEvent) {
        val entity = event.entity
        val sounds = getSounds(entity) ?: return

        entity.world.playSound(entity.location, sounds.death, SoundCategory.HOSTILE, 1.0f, 1.0f)
    }

    private fun getSounds(entity: Entity): CustomEntitySoundData? {
        val type = PersistentDataUtil.getValue(entity, DataTypeKeys.CUSTOM_ENTITY_TYPE) ?: return null
        return CustomEntitySounds.getSounds(type)
    }
}