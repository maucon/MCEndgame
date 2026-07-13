package de.fuballer.mcendgame.main.component.entity.custom.attack

import com.geckolib.animatable.GeoEntity
import de.fuballer.mcendgame.main.component.entity.custom.attack.data.AttackAnimationData
import de.fuballer.mcendgame.main.component.entity.custom.attack.data.DelayedAttackData
import de.fuballer.mcendgame.main.component.entity.custom.attack.trigger_condition.TriggerCondition
import de.fuballer.mcendgame.main.util.extension.mixin.WindChargeEntityMixinExtension.setExplosionPower
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.entity.projectile.hurtingprojectile.windcharge.WindCharge
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.phys.Vec3
import kotlin.math.cos
import kotlin.math.sin

class WindBurstAttack<T>(
    animationData: AttackAnimationData,
    totalDuration: Int,
    cooldown: Int,
    trigger: TriggerCondition,
    data: List<DelayedAttackData>,
    leapType: LeapType,
    private val projectileCount: (distance: Double) -> Int,
    private val projectileSpeed: () -> Float,
    private val projectileDirectionSpread: (distance: Double) -> Float,
    private val projectileExplosionPower: Float,
    blockMovementDuration: Int = 0,
) : LeapAttack<T>(animationData, totalDuration, cooldown, trigger, data, leapType, blockMovementDuration) where T : Mob, T : GeoEntity {

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
        val serverWorld = attacker.level() as? ServerLevel ?: return

        val spawnPos = Vec3(
            attacker.x - (attacker.bbWidth + 0.5) * 0.5 * sin(attacker.yBodyRot * (Math.PI / 180.0)),
            (attacker.y + attacker.eyeY) / 2,
            attacker.z + (attacker.bbWidth + 0.5) * 0.5 * cos(attacker.yBodyRot * (Math.PI / 180.0))
        )
        val distanceVec = Vec3(
            target.x - spawnPos.x,
            target.y + target.bbHeight / 2 - spawnPos.y,
            target.z - spawnPos.z,
        )
        val distance = distanceVec.length()

        repeat(projectileCount(distance)) {
            val windCharge = WindCharge(EntityType.WIND_CHARGE, serverWorld)
            windCharge.setExplosionPower(projectileExplosionPower)
            windCharge.owner = attacker
            windCharge.setPos(spawnPos)

            val itemStack = ItemStack(Items.AIR)
            Projectile.spawnProjectile(windCharge, serverWorld, itemStack)
            { entity: Projectile ->
                entity.shoot(distanceVec.x, distanceVec.y, distanceVec.z, projectileSpeed(), projectileDirectionSpread(distance))
            }
        }
    }
}