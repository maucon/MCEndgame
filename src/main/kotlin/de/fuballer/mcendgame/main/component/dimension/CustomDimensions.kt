package de.fuballer.mcendgame.main.component.dimension

import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import de.maucon.mauconframework.di.annotation.Injectable
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.dimension.DimensionType

@Injectable
object CustomDimensions {
    val DUNGEON: ResourceKey<DimensionType> = of("dungeon")

    private fun of(id: String) = ResourceKey.create(Registries.DIMENSION_TYPE, IdentifierUtil.default(id))
}