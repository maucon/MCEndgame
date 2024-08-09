package de.fuballer.mcendgame.component.recipe.db

import de.fuballer.mcendgame.framework.InMemoryMapRepository
import de.fuballer.mcendgame.framework.annotation.Service
import org.bukkit.NamespacedKey

@Service
class RecipeRepository : InMemoryMapRepository<NamespacedKey, RecipeEntity>()