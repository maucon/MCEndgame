package de.fuballer.mcendgame.component.recipe.db

import de.fuballer.mcendgame.db.AbstractMapRepository
import org.bukkit.NamespacedKey

class RecipeRepository : AbstractMapRepository<NamespacedKey, RecipeEntity>()
