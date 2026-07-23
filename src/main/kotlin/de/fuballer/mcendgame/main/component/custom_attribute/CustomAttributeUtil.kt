package de.fuballer.mcendgame.main.component.custom_attribute

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.getAllCustomAttributes
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import net.minecraft.world.entity.Avatar
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob
import kotlin.math.abs

private const val ISOLATED_RADIUS = 5.0
private const val ISOLATED_RADIUS_SQUARED = ISOLATED_RADIUS * ISOLATED_RADIUS
private val ISOLATED_ATTRIBUTE_TYPES = listOf(
    CustomAttributeTypes.MORE_DAMAGE_AGAINST_ISOLATED,
)

object CustomAttributeUtil {
    fun LivingEntity.isLowHealth() = health <= maxHealth / 2.0

    fun LivingEntity.isHighHealth() = !isLowHealth()

    fun LivingEntity.isFullHealth() = abs(health - maxHealth) < 0.01 // with error margin

    fun LivingEntity.canSeeIsolated() = getAllCustomAttributes().keys.any { it in ISOLATED_ATTRIBUTE_TYPES }

    fun LivingEntity.isIsolated(attacker: LivingEntity) =
        level().getEntities(this, boundingBox.inflate(ISOLATED_RADIUS))
        { it != attacker && (it is Mob || it is Avatar) && it.distanceToSqr(this) <= ISOLATED_RADIUS_SQUARED }
            .isEmpty()
}