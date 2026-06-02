package de.fuballer.mcendgame.main.component.entity.custom.goals.predicates

import de.fuballer.mcendgame.main.component.entity.custom.entities.training_dummy.TrainingDummyEntity
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.isDungeonEnemy
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.TargetPredicate
import net.minecraft.server.world.ServerWorld

class ShouldBeAttackedByCompanionsPredicate : TargetPredicate.EntityPredicate {
    override fun test(target: LivingEntity, world: ServerWorld) = target.isDungeonEnemy() && target !is TrainingDummyEntity
}