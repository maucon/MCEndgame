package de.fuballer.mcendgame.main.component.totem.db

import de.maucon.mauconframework.stereotype.Entity
import net.minecraft.core.Holder
import net.minecraft.resources.Identifier
import net.minecraft.world.entity.ai.attributes.Attribute
import java.util.*

class PlayerTotemVanillaAttributesEntity(
    override var id: UUID,
    var attributes: List<Pair<Holder<Attribute>, Identifier>> = listOf(),
) : Entity<UUID>