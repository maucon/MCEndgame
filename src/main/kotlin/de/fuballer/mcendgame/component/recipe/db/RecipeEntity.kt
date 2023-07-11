package de.fuballer.mcendgame.component.recipe.db

import de.fuballer.mcendgame.framework.stereotype.Entity
import org.bukkit.NamespacedKey

class RecipeEntity(
    override var id: NamespacedKey
) : Entity<NamespacedKey>
