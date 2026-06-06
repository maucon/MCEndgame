package de.fuballer.mcendgame.client.component.datagen.property

import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.initializer.Initializer
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperties

@Injectable
object CustomPropertyRegisterer {
    @Initializer
    fun init() {
        RangeSelectItemModelProperties.ID_MAPPER.put(IdentifierUtil.default("bow/pull"), BowPullDurationProperty.CODEC)
    }
}