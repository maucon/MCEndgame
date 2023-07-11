package de.fuballer.mcendgame.component.recipe

import de.fuballer.mcendgame.event.DiscoverRecipeAddEvent
import de.fuballer.mcendgame.framework.stereotype.EventListener
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerJoinEvent

class RecipeListener(
    private val recipeService: RecipeService
) : EventListener {
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) = recipeService.onPlayerJoin(event)

    @EventHandler
    fun onDiscoverRecipeAdd(event: DiscoverRecipeAddEvent) = recipeService.onDiscoverRecipeAdd(event)
}
