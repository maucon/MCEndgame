package de.fuballer.mcendgame.component.artifact.effects

import de.fuballer.mcendgame.event.PlayerDungeonJoinEvent
import de.fuballer.mcendgame.event.PlayerDungeonLeaveEvent
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.framework.stereotype.EventListener
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.EntityShootBowEvent
import org.bukkit.event.entity.EntityTargetEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerJoinEvent

@Component
class ArtifactEffectsListener(
    private val artifactEffectsService: ArtifactEffectsService
) : EventListener {
    @EventHandler
    fun onEntityDeath(event: EntityDeathEvent) = artifactEffectsService.onEntityDeath(event)

    @EventHandler
    fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) = artifactEffectsService.onEntityDamageByEntity(event)

    @EventHandler
    fun onShootBow(event: EntityShootBowEvent) = artifactEffectsService.onShootBow(event)

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) = artifactEffectsService.onPlayerJoin(event)

    @EventHandler
    fun onEntityBreed(event: PlayerInteractEntityEvent) = artifactEffectsService.onEntityBreed(event)

    @EventHandler
    fun onPlayerDungeonJoin(event: PlayerDungeonJoinEvent) = artifactEffectsService.onPlayerDungeonJoin(event)

    @EventHandler
    fun onPlayerDungeonLeave(event: PlayerDungeonLeaveEvent) = artifactEffectsService.onPlayerDungeonLeave(event)

    @EventHandler
    fun onEntityTarget(event: EntityTargetEvent) = artifactEffectsService.onEntityTarget(event)
}
