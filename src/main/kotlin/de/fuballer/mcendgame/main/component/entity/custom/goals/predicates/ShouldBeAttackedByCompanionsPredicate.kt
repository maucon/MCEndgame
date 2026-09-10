package de.fuballer.mcendgame.main.component.entity.custom.goals.predicates

import de.fuballer.mcendgame.main.component.entity.custom.entities.training_dummy.TrainingDummyEntity
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.isDungeonEnemy
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.targeting.TargetingConditions
import net.minecraft.world.entity.player.Player

class ShouldBeAttackedByCompanionsPredicate(
    val isDungeonEnemy: Boolean,
) : TargetingConditions.Selector {
    override fun test(target: LivingEntity, world: ServerLevel) = if (isDungeonEnemy) target is Player
    else target.isDungeonEnemy() && target !is TrainingDummyEntity
}