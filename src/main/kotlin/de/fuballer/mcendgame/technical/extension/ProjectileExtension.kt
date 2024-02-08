package de.fuballer.mcendgame.technical.extension

import de.fuballer.mcendgame.technical.persistent_data.TypeKeys
import de.fuballer.mcendgame.util.PersistentDataUtil
import org.bukkit.entity.Projectile

object ProjectileExtension {
    fun Projectile.setBaseDamage(value: Double) {
        PersistentDataUtil.setValue(this, TypeKeys.PROJECTILE_BASE_DAMAGE, value)
    }

    fun Projectile.getBaseDamage() = PersistentDataUtil.getValue(this, TypeKeys.PROJECTILE_BASE_DAMAGE)
}