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
    private val recipeRepo: RecipeRepository,
    private val server: Server
) : Listener {
    @EventHandler
    fun on(event: PlayerJoinEvent) {
        val player = event.player

        val recipes = recipeRepo.findAll()
            .map { it.id }

        player.discoverRecipes(recipes)
    }

    @EventHandler
    fun on(event: DiscoverRecipeAddEvent) {
        server.addRecipe(event.recipe)

        val entity = RecipeEntity(event.key)
        recipeRepo.save(entity)
    }
}