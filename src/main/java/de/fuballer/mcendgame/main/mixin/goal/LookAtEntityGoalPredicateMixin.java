package de.fuballer.mcendgame.main.mixin.goal;

import de.fuballer.mcendgame.main.messaging.misc.LookAtEntityGoalCommand;
import de.maucon.mauconframework.command.CommandGateway;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LookAtEntityGoal.class)
public class LookAtEntityGoalPredicateMixin {
    @Final
    @Shadow
    protected MobEntity mob;

    @Final
    @Shadow
    @Mutable
    protected TargetPredicate targetPredicate;

    @Inject(
            method = "<init>(Lnet/minecraft/entity/mob/MobEntity;Ljava/lang/Class;FFZ)V",
            at = @At("TAIL")
    )
    private void injectPredicate(
            MobEntity mob,
            Class<? extends LivingEntity> targetType,
            float range,
            float chance,
            boolean lookForward,
            CallbackInfo ci
    ) {
        TargetPredicate basePredicate = this.targetPredicate.copy();

        this.targetPredicate.setPredicate((entity) ->
                basePredicate.test(mob, entity) && canLookAt(mob, entity)
        );
    }

    @Unique
    private boolean canLookAt(MobEntity mob, LivingEntity target) {
        var command = new LookAtEntityGoalCommand(mob, target, true);
        var cmd = CommandGateway.INSTANCE.apply(command);
        return cmd.getCanLookAt();
    }
}
