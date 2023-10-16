package de.fuballer.mcendgame.component.dungeon.enemy.custom_entity.necromancer

import de.fuballer.mcendgame.framework.stereotype.EventListener
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntitySpellCastEvent

class NecromancerListener(
    private val necromancerService: NecromancerService
) : EventListener {
    @EventHandler
    fun onEntitySpellCast(event: EntitySpellCastEvent) = necromancerService.onEntitySpellCast(event)
}