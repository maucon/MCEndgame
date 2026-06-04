package de.fuballer.mcendgame.main.component.damage.dodge

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.getAllCustomAttributes
import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttributeType
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.arrow.AbstractArrow

data class DodgeCalculationCommand(
    val damaged: LivingEntity,
    val damagedAttributes: Map<CustomAttributeType, List<CustomAttribute>>,
    val type: DamageType,
    val isProjectile: Boolean,
    var isDodging: Boolean = false,
) {
    companion object {
        fun of(
            damaged: LivingEntity,
            source: DamageSource,
        ) = DodgeCalculationCommand(damaged, damaged.getAllCustomAttributes(), source.type(), source.directEntity is AbstractArrow)
    }
}