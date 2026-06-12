package de.fuballer.mcendgame.main.component.entity.custom.entities.skeleton_mage

import com.geckolib.animatable.GeoEntity
import com.geckolib.animatable.instance.AnimatableInstanceCache
import com.geckolib.animatable.manager.AnimatableManager
import com.geckolib.animation.AnimationController
import com.geckolib.constant.DefaultAnimations
import com.geckolib.util.GeckoLibUtil
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.monster.Enemy
import net.minecraft.world.entity.monster.skeleton.Skeleton
import net.minecraft.world.level.Level

class SkeletonMageEntity(
    type: EntityType<out SkeletonMageEntity>,
    world: Level,
) : Skeleton(type, world), GeoEntity, Enemy {
    companion object {
        fun createAttributes(): AttributeSupplier.Builder {
            return createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.25)
        }
    }

    private val cache: AnimatableInstanceCache = GeckoLibUtil.createInstanceCache(this)
    override fun getAnimatableInstanceCache() = cache

    override fun registerControllers(controllers: AnimatableManager.ControllerRegistrar) {
        controllers.add(
            AnimationController<SkeletonMageEntity>("Walk/Idle", 0)
            { test -> test.setAndContinue(if (test.isMoving) DefaultAnimations.WALK else DefaultAnimations.IDLE) },
        )
    }
}