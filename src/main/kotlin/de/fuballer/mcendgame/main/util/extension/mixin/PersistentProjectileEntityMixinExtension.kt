package de.fuballer.mcendgame.main.util.extension.mixin

import de.fuballer.mcendgame.main.accessor.PersistentProjectileEntityDamageAccessor
import de.fuballer.mcendgame.main.accessor.PersistentProjectileEntityPierceLevelAccessor
import net.minecraft.world.entity.projectile.arrow.AbstractArrow

object PersistentProjectileEntityMixinExtension {
    fun AbstractArrow.setPierceLevel(level: Byte) = (this as PersistentProjectileEntityPierceLevelAccessor).`mcendgame$callSetPierceLevel`(level)

    fun AbstractArrow.getDamage() = (this as PersistentProjectileEntityDamageAccessor).`mcendgame$getDamage`()
}