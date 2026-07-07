package de.fuballer.mcendgame.main.component.entity.custom.attack

import de.fuballer.mcendgame.main.component.entity.custom.attack.damage.DelayedAttackDamage
import de.fuballer.mcendgame.main.component.entity.custom.attack.data.AttackAnimationData
import de.fuballer.mcendgame.main.component.entity.custom.attack.trigger_condition.TriggerCondition
import de.fuballer.mcendgame.main.component.entity.custom.sound.DelayedSoundData
import de.fuballer.mcendgame.main.util.extension.mixin.WindChargeEntityMixinExtension.setExplosionPower
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.projectile.WindChargeEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.Vec3d
import software.bernie.geckolib.animatable.GeoEntity
import kotlin.math.cos
import kotlin.math.sin

class WindBurstAttack<T>(
    animationData: AttackAnimationData,
    totalDuration: Int,
    cooldown: Int,
    trigger: TriggerCondition,
    damage: List<DelayedAttackDamage>,
    leapType: LeapType,
    private val projectileCount: (distance: Double) -> Int,
    private val projectileSpeed: () -> Float,
    private val projectileDirectionSpread: (distance: Double) -> Float,
    private val projectileExplosionPower: Float,
    sounds: List<DelayedSoundData> = listOf(),
    blockMovementDuration: Int = 0,
) : LeapAttack<T>(animationData, totalDuration, cooldown, trigger, damage, leapType, sounds, blockMovementDuration) where T : MobEntity, T : GeoEntity {
    constructor(
        animationData: AttackAnimationData,
        totalDuration: Int,
        cooldown: Int,
        trigger: TriggerCondition,
        damage: DelayedAttackDamage?,
        leapType: LeapType,
        projectileCount: (distance: Double) -> Int,
        projectileSpeed: () -> Float,
        projectileDirectionSpread: (distance: Double) -> Float,
        projectileExplosionPower: Float,
        sounds: List<DelayedSoundData> = listOf(),
        blockMovementDuration: Int = 0,
    ) : this(
        animationData,
        totalDuration,
        cooldown,
        trigger,
        if (damage != null) listOf(damage) else listOf(),
        leapType,
        projectileCount,
        projectileSpeed,
        projectileDirectionSpread,
        projectileExplosionPower,
        sounds,
        blockMovementDuration,
    )

    override fun start(
        attacker: T,
        target: LivingEntity?
    ) {
        super.start(attacker, target)

        val existingTarget = target ?: return
        shootWindBursts(attacker, existingTarget)
    }

    private fun shootWindBursts(
        attacker: T,
        target: LivingEntity,
    ) {
        val serverWorld = attacker.entityWorld as? ServerWorld ?: return

        val spawnPos = Vec3d(
            attacker.x - (attacker.width + 0.5) * 0.5 * sin(attacker.bodyYaw * (Math.PI / 180.0)),
            (attacker.y + attacker.eyeY) / 2,
            attacker.z + (attacker.width + 0.5) * 0.5 * cos(attacker.bodyYaw * (Math.PI / 180.0))
        )
        val distanceVec = Vec3d(
            target.x - spawnPos.x,
            target.y + target.height / 2 - spawnPos.y,
            target.z - spawnPos.z,
        )
        val distance = distanceVec.length()

        repeat(projectileCount(distance)) {
            val windCharge = WindChargeEntity(EntityType.WIND_CHARGE, serverWorld)
            windCharge.setExplosionPower(projectileExplosionPower)
            windCharge.owner = attacker
            windCharge.setPosition(spawnPos)
            windCharge.setVelocity(
                distanceVec.x,
                distanceVec.y,
                distanceVec.z,
                projectileSpeed(),
                projectileDirectionSpread(distance)
            )

            serverWorld.spawnEntity(windCharge)
        }
    }
}