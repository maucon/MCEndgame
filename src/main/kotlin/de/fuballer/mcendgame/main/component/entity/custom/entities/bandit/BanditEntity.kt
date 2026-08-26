package de.fuballer.mcendgame.main.component.entity.custom.entities.bandit

import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.PathfinderMob
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.player.PlayerModelType
import net.minecraft.world.level.Level
import kotlin.random.Random

class BanditEntity(
    type: EntityType<BanditEntity>,
    level: Level,
) : PathfinderMob(type, level) {
    companion object {
        fun createAttributes(): AttributeSupplier.Builder {
            return createLivingAttributes()
                .add(Attributes.FOLLOW_RANGE, 32.0)
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.ATTACK_DAMAGE, 4.0)
                .add(Attributes.ATTACK_KNOCKBACK, 0.3)
        }
    }

    val modelType = if (Random.nextBoolean()) PlayerModelType.WIDE else PlayerModelType.SLIM
}