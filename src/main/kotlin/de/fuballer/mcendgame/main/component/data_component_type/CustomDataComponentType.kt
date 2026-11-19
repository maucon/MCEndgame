package de.fuballer.mcendgame.main.component.data_component_type

import com.mojang.serialization.Codec
import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute
import de.fuballer.mcendgame.main.util.extension.CodecExtension.setOf
import de.fuballer.mcendgame.main.util.minecraft.RegistryUtil
import de.maucon.mauconframework.di.annotation.Injectable
import net.minecraft.core.component.DataComponentType
import net.minecraft.resources.Identifier

@Injectable
object CustomDataComponentType {
    val CUSTOM_ATTRIBUTE: DataComponentType<List<CustomAttribute>> =
        RegistryUtil.registerDataComponentType(
            DataComponentType.builder<List<CustomAttribute>>()
                .persistent(CustomAttribute.CODEC.listOf())
                .build(),
            "custom_attributes"
        )

    val TOTEM_TIER: DataComponentType<Int> =
        RegistryUtil.registerDataComponentType(
            DataComponentType.builder<Int>()
                .persistent(Codec.INT)
                .build(),
            "totem_tier"
        )

    val ABSORBED_BOSS_TYPES: DataComponentType<MutableSet<Identifier>> =
        RegistryUtil.registerDataComponentType(
            DataComponentType.builder<MutableSet<Identifier>>()
                .persistent(Identifier.CODEC.setOf())
                .build(),
            "absorbed_boss_types"
        )
}