package de.fuballer.mcendgame.main.mixin.goal;

import de.fuballer.mcendgame.main.util.extension.WorldExtension;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HurtByTargetGoal.class)
public abstract class HurtByTargetGoalDisableCallSameTypeMixin {
    @Inject(
            method = "alertOthers",
            at = @At("HEAD"),
            cancellable = true
    )
    void cancelCallSameTypeForRevenge(CallbackInfo ci) {
        var accessor = (TargetGoalMobAccessor) this;
        var mob = accessor.mcendgame$getMob();
        var world = mob.level();
        if (!WorldExtension.INSTANCE.isDungeonWorld(world)) return;
        ci.cancel();
    }
}
