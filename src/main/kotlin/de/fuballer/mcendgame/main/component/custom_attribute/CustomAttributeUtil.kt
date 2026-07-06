package de.fuballer.mcendgame.main.component.custom_attribute

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.getAllCustomAttributes
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.player.PlayerEntity
import kotlin.math.abs

private const val ISOLATED_RADIUS = 5.0
private const val ISOLATED_RADIUS_SQUARED = ISOLATED_RADIUS * ISOLATED_RADIUS
private val ISOLATED_ATTRIBUTE_TYPES = listOf(
    CustomAttributeTypes.MORE_DAMAGE_AGAINST_ISOLATED,
)

object CustomAttributeUtil {
    fun LivingEntity.isLowHealth() = health <= maxHealth / 2.0

    fun LivingEntity.isHighHealth() = !isLowHealth()

    fun LivingEntity.isFullHealth() = abs(health - maxHealth) < 0.1

    fun LivingEntity.canSeeIsolated() = getAllCustomAttributes().keys.any { it in ISOLATED_ATTRIBUTE_TYPES }

    fun LivingEntity.isIsolated(attacker: LivingEntity) =
        entityWorld.getOtherEntities(this, boundingBox.expand(ISOLATED_RADIUS))
        { it != attacker && (it is MobEntity || it is PlayerEntity) && it.squaredDistanceTo(this) <= ISOLATED_RADIUS_SQUARED } // TODO 1.21.1
            .isEmpty()
}