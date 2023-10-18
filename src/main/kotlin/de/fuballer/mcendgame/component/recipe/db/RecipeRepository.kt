package de.fuballer.mcendgame.component.recipe.db

import de.fuballer.mcendgame.framework.AbstractMapRepository
import de.fuballer.mcendgame.framework.annotation.Repository
import org.bukkit.NamespacedKey

@Repository
class RecipeRepository : AbstractMapRepository<NamespacedKey, RecipeEntity>()
