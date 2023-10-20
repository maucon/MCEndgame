package de.fuballer.mcendgame.component.recipe

import de.fuballer.mcendgame.component.recipe.db.RecipeEntity
import de.fuballer.mcendgame.component.recipe.db.RecipeRepository
import de.fuballer.mcendgame.event.DiscoverRecipeAddEvent
import de.fuballer.mcendgame.framework.annotation.Component
import org.bukkit.Server
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

@Component
class RecipeService(
    private val discoverRecipeRepo: RecipeRepository,
    private val server: Server
) : Listener {
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player

        val recipes = discoverRecipeRepo.findAll()
            .map { it.id }

        player.discoverRecipes(recipes)
    }

    @EventHandler
    fun onDiscoverRecipeAdd(event: DiscoverRecipeAddEvent) {
        server.addRecipe(event.recipe)

        val entity = RecipeEntity(event.key)
        discoverRecipeRepo.save(entity)
    }
}
