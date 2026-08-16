package de.fuballer.mcendgame.main.util.extension

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.asDoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.asIntRoll
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.getAllCustomAttributes
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.hasBlockPhasing
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.entity.custom.entities.training_dummy.TrainingDummyEntity
import de.fuballer.mcendgame.main.component.item.custom.armor.interfaces.ItemWithCape
import de.fuballer.mcendgame.main.component.tags.CustomTags
import de.fuballer.mcendgame.main.messaging.misc.GainStatusEffectCommand
import de.fuballer.mcendgame.main.util.extension.Vec3Extension.angleDeg
import de.fuballer.mcendgame.main.util.extension.WorldExtension.isDungeonWorld
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.isDungeonEnemy
import de.maucon.mauconframework.command.CommandGateway
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.component.DataComponents
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.Mth
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.OwnableEntity
import net.minecraft.world.entity.animal.Animal
import net.minecraft.world.entity.animal.golem.IronGolem
import net.minecraft.world.entity.decoration.ArmorStand
import net.minecraft.world.entity.monster.Enemy
import net.minecraft.world.entity.npc.villager.Villager
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import net.minecraft.world.phys.shapes.BooleanOp
import net.minecraft.world.phys.shapes.Shapes
import kotlin.math.atan2
import kotlin.math.cos


object EntityExtension {
    fun LivingEntity.isAlly(entity: Entity): Boolean {
        if (this == entity) return true

        if (this.isOrIsTameableOf(Player::class.java) && entity.isOrIsTameableOf(Player::class.java)) return true
        if (this.isOrIsTameableOf(Enemy::class.java) && entity.isOrIsTameableOf(Enemy::class.java)) return true

        return false
    }

    fun LivingEntity.isEnemy(entity: Entity): Boolean {
        if (this == entity) return false

        if (this.isOrIsTameableOf(Player::class.java) && entity.isPlayerEnemy()) return true
        if (this.isPlayerEnemy() && entity.isOrIsTameableOf(Player::class.java)) return true

        return false
    }

    private fun Entity.isPlayerEnemy(): Boolean {
        if (isOrIsTameableOf(Enemy::class.java)) return true
        if (this is TrainingDummyEntity) return true
        return false
    }

    fun Entity.isValidSecondaryTarget(primaryTarget: Entity, attacker: Entity): Boolean {
        if (this == attacker || this == primaryTarget) return false

        if (level().isDungeonWorld()) {
            return isValidSecondaryTargetInDungeon(primaryTarget)
        }
        return isValidSecondaryTargetOutsideDungeon(primaryTarget, attacker)
    }

    private fun Entity.isValidSecondaryTargetInDungeon(primaryTarget: Entity): Boolean {
        if (this !is LivingEntity || primaryTarget !is LivingEntity) return false
        return this.isDungeonEnemy() == primaryTarget.isDungeonEnemy()
    }

    private fun Entity.isValidSecondaryTargetOutsideDungeon(primaryTarget: Entity, attacker: Entity): Boolean {
        if (this.type == primaryTarget.type) return true

        if (attacker.isOrIsTameableOf(Player::class.java)) {
            if (this.isOrIsTameableOf(Enemy::class.java)) return true
        }

        if (primaryTarget is ArmorStand) return this is ArmorStand
        if (primaryTarget.isOrIsTameableOf(Enemy::class.java)) return this.isOrIsTameableOf(Enemy::class.java)
        if (primaryTarget is Animal) return this is Animal
        if (primaryTarget is Villager) return this is Villager || this is Animal || this is IronGolem
        if (primaryTarget.isOrIsTameableOf(Player::class.java)) return this.isOrIsTameableOf(Player::class.java)

        return false
    }

    fun Entity.isOrIsTameableOf(clazz: Class<*>): Boolean {
        if (clazz.isInstance(this)) return true

        val tameable = this as? OwnableEntity ?: return false
        val owner = tameable.owner ?: return false
        return clazz.isInstance(owner)
    }

    fun Entity.centerPos(): Vec3 = position().add(0.0, bbHeight.toDouble(), 0.0)

    const val DEFAULT_BOW_FULL_PULL_TICKS = 20
    fun LivingEntity.getBowFullPullTicks(): Int {
        return getAdditionalBowPullTicks() + DEFAULT_BOW_FULL_PULL_TICKS
    }

    fun LivingEntity.getAdditionalBowPullTicks(): Int {
        val attributes = getAllCustomAttributes()[CustomAttributeTypes.BOW_PULL_TICKS] ?: return 0
        return attributes.sumOf { it.rolls[0].asIntRoll().getValue() }
    }

    fun Entity.isBlockPhasingAtEyes(): Boolean {
        if (this !is LivingEntity) return false
        if (!hasBlockPhasing()) return false

        val eyeBox = AABB.ofSize(eyePosition, 0.2, 0.2, 0.2)
        return collidesPhasing(eyeBox)
    }

    fun Entity.isBlockPhasing(): Boolean {
        if (this !is LivingEntity) return false
        if (!hasBlockPhasing()) return false
        return collidesPhasing(boundingBox)
    }

    private fun Entity.collidesPhasing(box: AABB): Boolean {
        val boxShape = Shapes.create(box)
        val minX = Mth.floor(box.minX)
        val minY = Mth.floor(box.minY)
        val minZ = Mth.floor(box.minZ)
        val maxX = Mth.floor(box.maxX)
        val maxY = Mth.floor(box.maxY)
        val maxZ = Mth.floor(box.maxZ)

        val mutable = BlockPos.MutableBlockPos()
        for (x in minX..maxX) {
            for (y in minY..maxY) {
                for (z in minZ..maxZ) {
                    mutable.set(x, y, z)
                    val blockState = level().getBlockState(mutable)
                    if (blockState.isAir) continue
                    if (blockState.`is`(CustomTags.NO_PHASING_SLOW_AND_FOG)) continue

                    val collisionShape = blockState.getCollisionShape(level(), mutable).move(mutable)
                    if (Shapes.joinIsNotEmpty(collisionShape, boxShape, BooleanOp.AND)) return true
                }
            }
        }
        return false
    }

    fun Entity.isBehind(
        other: Entity,
        maxAngle: Double = 90.0,
    ): Boolean {
        val distanceVec = other.position().subtract(position()).normalize()
        val damagedRotationVec = other.getViewVector(1F).normalize()

        val angle = distanceVec.angleDeg(damagedRotationVec)
        return angle < maxAngle
    }

    fun LivingEntity.getTotalCustomAttributeLootMultiplier(): Double {
        val attributes = getAllCustomAttributes()

        val increasedAttributes = attributes[CustomAttributeTypes.DROP_INCREASED_LOOT]
        val increasedFactor = 1.0 + (increasedAttributes?.sumOf { it.rolls[0].asDoubleRoll().getValue() } ?: 0.0)

        val moreAttributes = attributes[CustomAttributeTypes.DROP_MORE_LOOT]
        val moreFactor = moreAttributes?.map { it.rolls[0].asDoubleRoll().getValue() }?.fold(1.0) { a, b -> a * (1 + b) } ?: 1.0

        return increasedFactor * moreFactor
    }

    fun Entity.isInDungeonWorld() = level().isDungeonWorld()

    fun LivingEntity.applyPeriodicEffectIfTicksPassed(
        effectInstance: MobEffectInstance,
        ticks: Int = 80,
        source: Entity? = null,
    ) {
        val command = GainStatusEffectCommand(this, effectInstance)
        val actualEffectInstance = CommandGateway.apply(command).effect

        val activeEffect = getEffect(actualEffectInstance.effect)
        val maxDuration = effectInstance.duration
        if (activeEffect != null
            && activeEffect.amplifier >= effectInstance.amplifier
            && !activeEffect.endsWithin(maxDuration - ticks)
        ) return

        addEffect(effectInstance, source)
    }

    fun LivingEntity.setShieldsCooldown(cooldown: Float) {
        val serverWorld = level() as? ServerLevel ?: return

        val shieldStack = Items.SHIELD.defaultInstance
        val blocksAttacksComponent = shieldStack.get(DataComponents.BLOCKS_ATTACKS) ?: return
        blocksAttacksComponent.disable(serverWorld, this, cooldown, shieldStack)
    }

    fun Entity.setAndSyncVelocity(newVelocity: Vec3) {
        deltaMovement = newVelocity
        needsSync = true

        val world = level() as? ServerLevel ?: return
        for (serverPlayerEntity in world.players()) {
            if (serverPlayerEntity.distanceToSqr(position()) < 4096.0) {
                serverPlayerEntity.connection.send(ClientboundSetEntityMotionPacket(id, newVelocity))
            }
        }
    }

    fun LivingEntity.needsCapeData(): Boolean {
        if (getItemBySlot(EquipmentSlot.CHEST).item is ItemWithCape) return true
        if (getItemBySlot(EquipmentSlot.HEAD).item is ItemWithCape) return true
        if (getItemBySlot(EquipmentSlot.LEGS).item is ItemWithCape) return true
        if (getItemBySlot(EquipmentSlot.FEET).item is ItemWithCape) return true
        if (getItemBySlot(EquipmentSlot.MAINHAND).item is ItemWithCape) return true
        if (getItemBySlot(EquipmentSlot.OFFHAND).item is ItemWithCape) return true
        return false
    }

    fun Entity.rotateToEntity(target: Entity) {
        val dx = target.x - x
        val dz = target.z - z
        val yaw = (atan2(dz, dx) * 180.0 / Math.PI).toFloat() - 90f

        yRot = yaw
        yHeadRot = yaw
    }

    fun Entity.getDistanceToGround(): Double {
        val pos: BlockPos.MutableBlockPos = blockPosition().mutable()
        val level = level()

        while (pos.y > level.minY) {
            pos.move(Direction.DOWN)

            val state: BlockState = level.getBlockState(pos)

            if (state.getCollisionShape(level, pos).isEmpty) continue

            val collisionShapeHeight = state.getCollisionShape(level, pos).max(Direction.Axis.Y)
            return y - (pos.y + collisionShapeHeight)
        }

        return 0.0
    }

    fun Entity.isFacingTowards(
        other: Entity,
        maxAngleDegrees: Double = 90.0
    ): Boolean {
        val look = lookAngle.normalize()
        val toTarget = other.position()
            .subtract(position())
            .normalize()

        val dot = look.dot(toTarget)
        val threshold = cos(Math.toRadians(maxAngleDegrees))

        return dot >= threshold
    }

    fun LivingEntity.getHealthPercentage() = health / maxHealth
}