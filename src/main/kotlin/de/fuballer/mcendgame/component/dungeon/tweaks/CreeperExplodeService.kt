package de.fuballer.mcendgame.component.dungeon.tweaks

import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.extension.WorldExtension.isDungeonWorld
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.ExplosionPrimeEvent

@Component
class CreeperExplodeService : Listener {
    @EventHandler
    fun on(event: ExplosionPrimeEvent) {
        val entity = event.entity

        if (entity.type != EntityType.CREEPER) return
        if (!event.entity.world.isDungeonWorld()) return

        val creeper = entity as LivingEntity

        creeper.activePotionEffects
            .forEach { creeper.removePotionEffect(it.type) }
    }
}
