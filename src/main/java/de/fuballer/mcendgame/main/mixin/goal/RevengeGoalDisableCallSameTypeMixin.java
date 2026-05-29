package de.fuballer.mcendgame.main.mixin.goal;

import de.fuballer.mcendgame.main.util.extension.WorldExtension;
import net.minecraft.entity.ai.goal.RevengeGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RevengeGoal.class)
public abstract class RevengeGoalDisableCallSameTypeMixin {
    @Inject(
            method = "callSameTypeForRevenge",
            at = @At("HEAD"),
            cancellable = true
    )
    void cancelCallSameTypeForRevenge(CallbackInfo ci) {
        var accessor = (TrackTargetGoalMobAccessor) this;
        var mob = accessor.mcendgame$getMob();
        var world = mob.getEntityWorld();
        if (!WorldExtension.INSTANCE.isDungeonWorld(world)) return;
        ci.cancel();
    }
}
