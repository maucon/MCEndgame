package de.fuballer.mcendgame.main.component.entity.custom.entities.skeleton_mage

import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.monster.Enemy
import net.minecraft.world.entity.monster.skeleton.Skeleton
import net.minecraft.world.level.Level

class SkeletonMageEntity(
    type: EntityType<out SkeletonMageEntity>,
    world: Level,
) : Skeleton(type, world), Enemy {
    companion object {
        fun createAttributes(): AttributeSupplier.Builder {
            return createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.25)
        }
    }
}