package de.fuballer.mcendgame.main.util.extension.mixin

import de.fuballer.mcendgame.main.accessor.AbstractArrowDamageAccessor
import de.fuballer.mcendgame.main.accessor.AbstractArrowPierceLevelAccessor
import net.minecraft.world.entity.projectile.arrow.AbstractArrow

object AbstractArrowMixinExtension {
    fun AbstractArrow.setPierceLevel(level: Byte) = (this as AbstractArrowPierceLevelAccessor).`mcendgame$callSetPierceLevel`(level)

    fun AbstractArrow.getDamage() = (this as AbstractArrowDamageAccessor).`mcendgame$getDamage`()

    fun AbstractArrow.getDamageFactor() = (this as AbstractArrowDamageAccessor).`mcendgame$getDamageFactor`()

    fun AbstractArrow.setDamageFactor(factor: Double) {
        (this as AbstractArrowDamageAccessor).`mcendgame$setDamageFactor`(factor)
    }
}