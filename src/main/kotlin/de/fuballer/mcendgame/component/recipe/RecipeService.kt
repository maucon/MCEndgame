package de.fuballer.mcendgame.component.recipe

import de.fuballer.mcendgame.component.recipe.db.RecipeEntity
import de.fuballer.mcendgame.component.recipe.db.RecipeRepository
import de.fuballer.mcendgame.event.DiscoverRecipeAddEvent
import de.fuballer.mcendgame.framework.stereotype.Service
import org.bukkit.Bukkit
import org.bukkit.event.player.PlayerJoinEvent

class RecipeService(
    private val discoverRecipeRepo: RecipeRepository
) : Service {
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player

        val recipes = discoverRecipeRepo.findAll()
            .map { it.id }

        player.discoverRecipes(recipes)
    }

    fun onDiscoverRecipeAdd(event: DiscoverRecipeAddEvent) {
        Bukkit.addRecipe(event.recipe)

        val entity = RecipeEntity(event.key)
        discoverRecipeRepo.save(entity)
    }
}
