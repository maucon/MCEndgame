package de.fuballer.mcendgame.main.functional.item_tag

import de.fuballer.mcendgame.main.util.extension.CodecExtension.setOf
import de.fuballer.mcendgame.main.util.minecraft.RegistryUtil
import de.maucon.mauconframework.di.annotation.Injectable
import net.minecraft.core.component.DataComponentType

@Injectable
object ItemTagComponentType {
    val COMPONENT_TYPE: DataComponentType<MutableSet<ItemTag>> =
        RegistryUtil.registerDataComponentType(
            DataComponentType.builder<MutableSet<ItemTag>>()
                .persistent(ItemTag.CODEC.setOf())
                .build(),
            "item_tags"
        )
}