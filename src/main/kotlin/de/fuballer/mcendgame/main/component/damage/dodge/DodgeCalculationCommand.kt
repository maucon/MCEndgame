package de.fuballer.mcendgame.main.component.damage.dodge

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.getAllCustomAttributes
import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttributeType
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.arrow.AbstractArrow
import kotlin.random.Random

data class DodgeCalculationCommand(
    val damaged: LivingEntity,
    val damagedAttributes: Map<CustomAttributeType, List<CustomAttribute>>,
    val damagerAttributes: Map<CustomAttributeType, List<CustomAttribute>>,
    val source: DamageSource,
    val isProjectile: Boolean,
    val dodgeChances: MutableList<Double> = mutableListOf(),
    var canBeDodged: Boolean = true,
    val randomRoll: Double = Random.nextDouble(),
) {
    companion object {
        fun of(
            damaged: LivingEntity,
            source: DamageSource,
        ) = DodgeCalculationCommand(
            damaged,
            damaged.getAllCustomAttributes(),
            (source.entity as? LivingEntity)?.getAllCustomAttributes() ?: mapOf(),
            source,
            source.directEntity is AbstractArrow
        )
    }

    fun isDodging() = canBeDodged && randomRoll > dodgeChances.fold(1.0) { a, b -> a * (1 - b) }
}