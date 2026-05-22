package de.fuballer.mcendgame.main.component.entity.custom.entities.training_dummy

import net.minecraft.entity.EntityType
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.mob.MobEntity
import net.minecraft.world.World

class TrainingDummyEntity(
    type: EntityType<out TrainingDummyEntity>,
    world: World,
) : MobEntity(type, world) {
    companion object {
        fun createAttributes(): DefaultAttributeContainer.Builder {
            return createLivingAttributes()
                .add(EntityAttributes.FOLLOW_RANGE, 35.0)
                .add(EntityAttributes.MOVEMENT_SPEED, 0.0)
                .add(EntityAttributes.ATTACK_DAMAGE, 3.0)
                .add(EntityAttributes.ATTACK_KNOCKBACK, 0.3)
                .add(EntityAttributes.ARMOR, 0.0)
                .add(EntityAttributes.KNOCKBACK_RESISTANCE, 0.8)
                .add(EntityAttributes.MOVEMENT_EFFICIENCY, 0.85)
                .add(EntityAttributes.SAFE_FALL_DISTANCE, 10.0)
                .add(EntityAttributes.FALL_DAMAGE_MULTIPLIER, 0.1)
                .add(EntityAttributes.SCALE, 1.0)
        }
    }
}