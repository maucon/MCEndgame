package de.fuballer.mcendgame.main.util.extension.mixin

import de.fuballer.mcendgame.main.accessor.WindChargeEntityExplosionPowerAccessor
import net.minecraft.world.entity.projectile.hurtingprojectile.windcharge.WindCharge

object WindChargeEntityMixinExtension {
    fun WindCharge.setExplosionPower(power: Float) {
        val accessor = this as WindChargeEntityExplosionPowerAccessor
        accessor.`mcendgame$setExplosionPower`(power)
    }
}