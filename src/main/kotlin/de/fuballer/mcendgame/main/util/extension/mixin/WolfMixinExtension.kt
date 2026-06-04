package de.fuballer.mcendgame.main.util.extension.mixin

import de.fuballer.mcendgame.main.accessor.WolfEntityColorAndVariantAccessor
import net.minecraft.core.Holder
import net.minecraft.world.entity.animal.wolf.Wolf
import net.minecraft.world.entity.animal.wolf.WolfVariant
import net.minecraft.world.item.DyeColor

object WolfMixinExtension {
    fun Wolf.setVariant(variant: Holder<WolfVariant>) {
        val accessor = this as WolfEntityColorAndVariantAccessor
        accessor.`mcendgame$callSetVariant`(variant)
    }

    fun Wolf.setCollarColor(color: DyeColor) {
        val accessor = this as WolfEntityColorAndVariantAccessor
        accessor.`mcendgame$callSetCollarColor`(color)
    }
}