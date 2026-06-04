package de.fuballer.mcendgame.main.mixin.goal;

import de.fuballer.mcendgame.main.messaging.misc.LookAtEntityGoalCommand;
import de.maucon.mauconframework.command.CommandGateway;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LookAtPlayerGoal.class)
public class LookAtPlayerGoalPredicateMixin {
    @Final
    @Shadow
    protected Mob mob;

    @Final
    @Shadow
    @Mutable
    protected TargetingConditions lookAtContext;

    @Inject(
            method = "<init>(Lnet/minecraft/world/entity/Mob;Ljava/lang/Class;FFZ)V",
            at = @At("TAIL")
    )
    private void injectPredicate(
            Mob mob,
            Class<? extends LivingEntity> targetType,
            float range,
            float chance,
            boolean lookForward,
            CallbackInfo ci
    ) {
        TargetingConditions basePredicate = this.lookAtContext.copy();

        this.lookAtContext.selector((entity, world) ->
                basePredicate.test(world, mob, entity) && canLookAt(mob, entity)
        );
    }

    @Unique
    private boolean canLookAt(Mob mob, LivingEntity target) {
        var command = new LookAtEntityGoalCommand(mob, target, true);
        var cmd = CommandGateway.INSTANCE.apply(command);
        return cmd.getCanLookAt();
    }
}
