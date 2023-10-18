package de.fuballer.mcendgame.component.recipe.db

import de.fuballer.mcendgame.framework.AbstractMapRepository
import de.fuballer.mcendgame.framework.annotation.Component
import org.bukkit.NamespacedKey

@Component
class RecipeRepository : AbstractMapRepository<NamespacedKey, RecipeEntity>()
