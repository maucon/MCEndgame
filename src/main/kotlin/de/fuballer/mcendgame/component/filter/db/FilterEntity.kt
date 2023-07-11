package de.fuballer.mcendgame.component.filter.db

import de.fuballer.mcendgame.framework.stereotype.Entity
import org.bukkit.Material
import java.util.*

data class FilterEntity(
    override var id: UUID,

    var filters: MutableSet<Material> = mutableSetOf()
) : Entity<UUID>
