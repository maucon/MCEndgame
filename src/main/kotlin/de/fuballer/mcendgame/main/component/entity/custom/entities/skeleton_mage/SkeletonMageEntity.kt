package de.fuballer.mcendgame.main.component.entity.custom.entities.skeleton_mage

import de.fuballer.mcendgame.main.component.entity.custom.CustomEntities
import de.fuballer.mcendgame.main.component.entity.custom.entities.spell_fireball.SpellFireballEntity
import de.fuballer.mcendgame.main.component.entity.custom.goals.KeepDistanceToTargetGoal
import de.fuballer.mcendgame.main.component.entity.custom.goals.NoMovementProjectileAttackGoal
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.goal.*
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
import net.minecraft.world.entity.animal.golem.IronGolem
import net.minecraft.world.entity.animal.turtle.Turtle
import net.minecraft.world.entity.animal.wolf.Wolf
import net.minecraft.world.entity.monster.Enemy
import net.minecraft.world.entity.monster.skeleton.Skeleton
import net.minecraft.world.entity.npc.villager.Villager
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sin

class SkeletonMageEntity(
    type: EntityType<out SkeletonMageEntity>,
    world: Level,
) : Skeleton(type, world), Enemy {
    companion object {
        fun createAttributes(): AttributeSupplier.Builder {
            return createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.25)
        }
    }

    override fun registerGoals() {
        goalSelector.addGoal(2, RestrictSunGoal(this))
        goalSelector.addGoal(3, FleeSunGoal(this, 1.0))
        goalSelector.addGoal(3, AvoidEntityGoal(this, Wolf::class.java, 6.0F, 1.0, 1.2))
        goalSelector.addGoal(4, NoMovementProjectileAttackGoal(this, 40, 20F, 40))
        goalSelector.addGoal(5, KeepDistanceToTargetGoal(this, 1.0, 10F, 15F))
        goalSelector.addGoal(6, WaterAvoidingRandomStrollGoal(this, 1.0))
        goalSelector.addGoal(7, LookAtPlayerGoal(this, Player::class.java, 8.0F))
        goalSelector.addGoal(7, RandomLookAroundGoal(this))
        targetSelector.addGoal(1, HurtByTargetGoal(this))
        targetSelector.addGoal(2, NearestAttackableTargetGoal(this, Player::class.java, true))
        targetSelector.addGoal(2, NearestAttackableTargetGoal(this, Villager::class.java, true))
        targetSelector.addGoal(3, NearestAttackableTargetGoal(this, IronGolem::class.java, true))
        targetSelector.addGoal(3, NearestAttackableTargetGoal(this, Turtle::class.java, 10, true, false, Turtle.BABY_ON_LAND_SELECTOR))
    }

    override fun reassessWeaponGoal() {}

    override fun performRangedAttack(target: LivingEntity, power: Float) {
        val serverLevel = level() as? ServerLevel ?: return

        val shootPos = getShootPos()
        val distance = Vec3(
            target.x - shootPos.x,
            target.y + target.bbHeight / 2 - shootPos.y,
            target.z - shootPos.z,
        )
        val velocity = getFireballVelocity(distance)

        val projectile = SpellFireballEntity(CustomEntities.SPELL_FIREBALL, serverLevel)
        projectile.setPos(shootPos)
        projectile.owner = this

        val itemStack = ItemStack(Items.AIR)
        Projectile.spawnProjectileUsingShoot(
            projectile,
            serverLevel,
            itemStack,
            velocity.x,
            velocity.y,
            velocity.z,
            velocity.length().toFloat(),
            1.5F,
        )

        playShootFireballSound()
        swing(InteractionHand.MAIN_HAND)
    }

    private fun getFireballVelocity(
        distance: Vec3,
    ): Vec3 {
        val ticks = getFireballTravelTicks(distance.length())
        val horizontalDistance = distance.horizontalDistance()

        val tickInertia = SpellFireballEntity.INERTIA.pow(ticks)
        val denominator = 100 * (1 - tickInertia)
        if (denominator < 1e-8) return distance.normalize()

        val horizontalVelocity = horizontalDistance / denominator
        val gravityTimes100 = SpellFireballEntity.GRAVITY * 100
        val yVelocity = (distance.y + gravityTimes100 * ticks) / denominator - gravityTimes100

        val horizontalVelocityVec = distance.multiply(1.0, 0.0, 1.0).normalize().multiply(horizontalVelocity, 1.0, horizontalVelocity)
        val velocityVec = Vec3(horizontalVelocityVec.x, yVelocity, horizontalVelocityVec.z)
        return velocityVec
    }

    private fun getFireballTravelTicks(distance: Double) = max(1, (distance * 4).pow(0.8).toInt())

    private fun getShootPos() = Vec3(
        x - (bbWidth + 1.0) * 0.5 * sin(yBodyRot * (Math.PI / 180.0)),
        eyeY - 0.1,
        z + (bbWidth + 1.0) * 0.5 * cos(yBodyRot * (Math.PI / 180.0))
    )

    private fun playShootFireballSound() {
        playSound(SoundEvents.FIRECHARGE_USE, 0.75f, 0.75F + random.nextFloat() * 0.25F)
    }
}