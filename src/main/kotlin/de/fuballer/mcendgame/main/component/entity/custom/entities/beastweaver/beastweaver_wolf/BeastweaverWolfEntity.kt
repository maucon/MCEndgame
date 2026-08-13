package de.fuballer.mcendgame.main.component.entity.custom.entities.beastweaver.beastweaver_wolf

import de.fuballer.mcendgame.main.component.entity.custom.CustomEntities
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.goal.FloatGoal
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
import net.minecraft.world.entity.animal.wolf.Wolf
import net.minecraft.world.entity.monster.Enemy
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class BeastweaverWolfEntity(
    type: EntityType<out BeastweaverWolfEntity>,
    level: Level,
) : Wolf(type, level), Enemy {
    constructor(level: Level) : this(CustomEntities.BEASTWEAVER_WOLF, level)

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

        targetSelector.addGoal(4, NearestAttackableTargetGoal(this, Player::class.java, 10, false, false, null))
    }

    private val maxDuration = 500 + random.nextDouble() * 50

    override fun baseTick() {
        super.baseTick()

        val serverLevel = level() as? ServerLevel ?: return
        if (
            tickCount > maxDuration
            || target?.isAlive != true
            || owner?.isAlive != true
        ) kill(serverLevel)
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
}