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
import de.fuballer.mcendgame.main.util.extension.Vec3dExtension.angleDeg
import de.fuballer.mcendgame.main.util.extension.WorldExtension.isDungeonWorld
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.isDungeonEnemy
import de.maucon.mauconframework.command.CommandGateway
import net.minecraft.component.DataComponentTypes
import net.minecraft.entity.Entity
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.Tameable
import net.minecraft.entity.decoration.ArmorStandEntity
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.mob.Monster
import net.minecraft.entity.passive.AnimalEntity
import net.minecraft.entity.passive.IronGolemEntity
import net.minecraft.entity.passive.VillagerEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Items
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.function.BooleanBiFunction
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import net.minecraft.util.shape.VoxelShapes

object EntityExtension {
    fun LivingEntity.isAlly(entity: Entity): Boolean {
        if (this == entity) return true

        if (this.isOrIsTameableOf(PlayerEntity::class.java) && entity.isOrIsTameableOf(PlayerEntity::class.java)) return true
        if (this.isOrIsTameableOf(Monster::class.java) && entity.isOrIsTameableOf(Monster::class.java)) return true

        return false
    }

    fun LivingEntity.isEnemy(entity: Entity): Boolean {
        if (this == entity) return false

        if (this.isOrIsTameableOf(PlayerEntity::class.java) && entity.isPlayerEnemy()) return true
        if (this.isPlayerEnemy() && entity.isOrIsTameableOf(PlayerEntity::class.java)) return true

        return false
    }

    private fun Entity.isPlayerEnemy(): Boolean {
        if (isOrIsTameableOf(Monster::class.java)) return true
        if (this is TrainingDummyEntity) return true
        return false
    }

    fun Entity.isValidSecondaryTarget(primaryTarget: Entity, attacker: Entity): Boolean {
        if (this == attacker || this == primaryTarget) return false

        if (entityWorld.isDungeonWorld()) {
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

        if (attacker.isOrIsTameableOf(PlayerEntity::class.java)) {
            if (this.isOrIsTameableOf(Monster::class.java)) return true
        }

        if (primaryTarget is ArmorStandEntity) return this is ArmorStandEntity
        if (primaryTarget.isOrIsTameableOf(Monster::class.java)) return this.isOrIsTameableOf(Monster::class.java)
        if (primaryTarget is AnimalEntity) return this is AnimalEntity
        if (primaryTarget is VillagerEntity) return this is VillagerEntity || this is AnimalEntity || this is IronGolemEntity
        if (primaryTarget.isOrIsTameableOf(PlayerEntity::class.java)) return this.isOrIsTameableOf(PlayerEntity::class.java)

        return false
    }

    fun Entity.isOrIsTameableOf(clazz: Class<*>): Boolean {
        if (clazz.isInstance(this)) return true

        val tameable = this as? Tameable ?: return false
        val owner = tameable.owner ?: return false
        return clazz.isInstance(owner)
    }

    fun Entity.centerPos(): Vec3d = pos.add(0.0, height.toDouble(), 0.0)

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

        val eyeBox = Box.of(eyePos, 0.2, 0.2, 0.2)
        return collidesPhasing(eyeBox)
    }

    fun Entity.isBlockPhasing(): Boolean {
        if (this !is LivingEntity) return false
        if (!hasBlockPhasing()) return false
        return collidesPhasing(boundingBox)
    }

    private fun Entity.collidesPhasing(box: Box): Boolean {
        val boxShape = VoxelShapes.cuboid(box)
        val minX = MathHelper.floor(box.minX)
        val minY = MathHelper.floor(box.minY)
        val minZ = MathHelper.floor(box.minZ)
        val maxX = MathHelper.floor(box.maxX)
        val maxY = MathHelper.floor(box.maxY)
        val maxZ = MathHelper.floor(box.maxZ)

        val mutable = BlockPos.Mutable()
        for (x in minX..maxX) {
            for (y in minY..maxY) {
                for (z in minZ..maxZ) {
                    mutable.set(x, y, z)
                    val blockState = entityWorld.getBlockState(mutable)
                    if (blockState.isAir) continue
                    if (blockState.isIn(CustomTags.NO_PHASING_SLOW_AND_FOG)) continue

                    val collisionShape = blockState.getCollisionShape(entityWorld, mutable).offset(mutable.x.toDouble(), mutable.y.toDouble(), mutable.z.toDouble())
                    if (VoxelShapes.matchesAnywhere(collisionShape, boxShape, BooleanBiFunction.AND)) return true
                }
            }
        }
        return false
    }

    fun Entity.isBehind(
        other: Entity,
        maxAngle: Double = 90.0,
    ): Boolean {
        val distanceVec = other.pos.subtract(pos).normalize()
        val damagedRotationVec = other.getRotationVec(1F).normalize()

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

    fun Entity.isInDungeonWorld() = entityWorld.isDungeonWorld()

    fun LivingEntity.applyPeriodicEffectIfTicksPassed(
        effectInstance: StatusEffectInstance,
        ticks: Int = 80,
        source: Entity? = null,
    ) {
        val command = GainStatusEffectCommand(this, effectInstance)
        val actualEffectInstance = CommandGateway.apply(command).effect

        val activeEffect = getStatusEffect(actualEffectInstance.effectType)
        val maxDuration = effectInstance.duration
        if (activeEffect != null
            && activeEffect.amplifier >= effectInstance.amplifier
            && !activeEffect.isDurationBelow(maxDuration - ticks)
        ) return

        addStatusEffect(effectInstance, source)
    }

    fun LivingEntity.setShieldsCooldown(cooldown: Float) {
        val serverWorld = entityWorld as? ServerWorld ?: return

        val shieldStack = Items.SHIELD.defaultStack
        val blocksAttacksComponent = shieldStack.get(DataComponentTypes.BLOCKS_ATTACKS) ?: return
        blocksAttacksComponent.applyShieldCooldown(serverWorld, this, cooldown, shieldStack)
    }

    fun Entity.setAndSyncVelocity(newVelocity: Vec3d) {
        velocity = newVelocity
        velocityDirty = true

        val world = entityWorld as? ServerWorld ?: return
        for (serverPlayerEntity in world.players) {
            if (serverPlayerEntity.squaredDistanceTo(pos) < 4096.0) {
                serverPlayerEntity.networkHandler.sendPacket(EntityVelocityUpdateS2CPacket(id, newVelocity))
            }
        }
    }

    fun LivingEntity.needsCapeData(): Boolean {
        if (getEquippedStack(EquipmentSlot.CHEST)?.item is ItemWithCape) return true
        if (getEquippedStack(EquipmentSlot.HEAD)?.item is ItemWithCape) return true
        if (getEquippedStack(EquipmentSlot.LEGS)?.item is ItemWithCape) return true
        if (getEquippedStack(EquipmentSlot.FEET)?.item is ItemWithCape) return true
        if (getEquippedStack(EquipmentSlot.MAINHAND)?.item is ItemWithCape) return true
        if (getEquippedStack(EquipmentSlot.OFFHAND)?.item is ItemWithCape) return true
        return false
    }
}