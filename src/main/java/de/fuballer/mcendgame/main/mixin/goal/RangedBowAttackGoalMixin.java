package de.fuballer.mcendgame.main.mixin.goal;

import de.fuballer.mcendgame.main.component.custom_attribute.effects.BowPullUtil;
import de.fuballer.mcendgame.main.util.extension.EntityExtension;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.goal.RangedBowAttackGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.BowItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RangedBowAttackGoal.class)
public class RangedBowAttackGoalMixin {
    @Inject(method = "isHoldingBow", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/Monster;isHolding(Lnet/minecraft/world/item/Item;)Z"), cancellable = true)
    void isHoldingBow(CallbackInfoReturnable<Boolean> cir) {
        var stack = accessActor().getMainHandItem();
        cir.setReturnValue(stack.getItem() instanceof BowItem);
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/Monster;startUsingItem(Lnet/minecraft/world/InteractionHand;)V"))
    void redirectSetCurrentHand(Monster instance, InteractionHand hand) {
        var actor = accessActor();
        var newHand = actor.getMainHandItem().getItem() instanceof BowItem ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
        actor.startUsingItem(newHand);
    }

    @ModifyConstant(method = "tick", constant = @Constant(intValue = 20))
    int modifyBowPullTickThreshold(int constant) {
        var additionalTicks = EntityExtension.INSTANCE.getAdditionalBowPullTicks(accessActor());
        return constant + additionalTicks;
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/BowItem;getPowerForTime(I)F"))
    float getPullProgress(int useTicks) {
        var fullPullTicks = EntityExtension.INSTANCE.getBowFullPullTicks(accessActor());
        return BowPullUtil.INSTANCE.getPullProgress(useTicks, fullPullTicks);
    }

    @Unique
    private Monster accessActor() {
        var accessor = (RangedBowAttackGoalAccessor) this;
        return accessor.getMob();
    }
}