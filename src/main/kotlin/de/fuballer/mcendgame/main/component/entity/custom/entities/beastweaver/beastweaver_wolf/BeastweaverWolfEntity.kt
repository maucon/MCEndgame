package de.fuballer.mcendgame.main.component.entity.custom.entities.beastweaver.beastweaver_wolf

import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.goal.*
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
import net.minecraft.world.entity.animal.wolf.Wolf
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class BeastweaverWolfEntity(
    type: EntityType<out BeastweaverWolfEntity>,
    level: Level,
) : Wolf(type, level) {
    var maxDuration = 500 + (random.nextDouble() * 50).toInt()
    private var dieWithoutTarget = true

    companion object {
        fun createAttributes(): AttributeSupplier.Builder =
            createAnimalAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.35)
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.ATTACK_DAMAGE, 4.0)
                .add(Attributes.FOLLOW_RANGE, 48.0)
    }

    override fun registerGoals() {
        goalSelector.addGoal(1, FloatGoal(this))
        goalSelector.addGoal(2, LeapAtTargetGoal(this, 0.4F))
        goalSelector.addGoal(3, MeleeAttackGoal(this, 1.0, true))
        goalSelector.addGoal(4, FollowOwnerGoal(this, 1.0, 10.0f, 2.0f))
        goalSelector.addGoal(5, WaterAvoidingRandomStrollGoal(this, 1.0))
        goalSelector.addGoal(6, LookAtPlayerGoal(this, Player::class.java, 8.0f))
        goalSelector.addGoal(6, RandomLookAroundGoal(this))

        targetSelector.addGoal(4, NearestAttackableTargetGoal(this, Player::class.java, 10, false, false, null))
    }

    override fun baseTick() {
        super.baseTick()

        val serverLevel = level() as? ServerLevel ?: return
        if (
            tickCount > maxDuration
            || (dieWithoutTarget && (target?.isAlive != true))
            || owner?.isAlive != true
        ) {
            owner = null
            kill(serverLevel)
        }
    }

    override fun isInvulnerableTo(level: ServerLevel, source: DamageSource): Boolean {
        if (source.entity == owner) return true
        return super.isInvulnerableTo(level, source)
    }

    override fun mobInteract(player: Player, hand: InteractionHand): InteractionResult {
        return InteractionResult.PASS
    }

    override fun shouldTryTeleportToOwner() = false

    override fun isFood(itemStack: ItemStack) = false

    fun setDieWithoutTarget(dieWithoutTarget: Boolean) {
        this.dieWithoutTarget = dieWithoutTarget
    }
}