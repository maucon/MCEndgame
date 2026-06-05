package de.fuballer.mcendgame.main.mixin.skeleton;

import de.fuballer.mcendgame.main.util.extension.EntityExtension;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.skeleton.AbstractSkeleton;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractSkeleton.class)
public class AbstractSkeletonBowMixin {
    @Redirect(
            method = "reassessWeaponGoal",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"
            )
    )
    private boolean redirectIsOf(ItemStack instance, Object o) {
        var entity = (AbstractSkeleton) (Object) this;
        var hand = entity.getMainHandItem().getItem() instanceof BowItem ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
        var stack = entity.getItemInHand(hand);
        return stack.getItem() instanceof BowItem;
    }

    @Redirect(
            method = "performRangedAttack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/projectile/ProjectileUtil;getWeaponHoldingHand(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/InteractionHand;")
    )
    InteractionHand redirectHandPossiblyHolding(LivingEntity entity, Item item) {
        return entity.getMainHandItem().getItem() instanceof BowItem ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
    }

    @Inject(method = "getAttackInterval", at = @At("HEAD"), cancellable = true)
    void getRegularAttackInterval(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(40 + getAdditionalAttackIntervalTicks() * 2);
    }

    @Inject(method = "getHardAttackInterval", at = @At("HEAD"), cancellable = true)
    void getHardAttackInterval(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(20 + getAdditionalAttackIntervalTicks());
    }

    @Unique
    private int getAdditionalAttackIntervalTicks() {
        var entity = (AbstractSkeleton) (Object) this;
        return EntityExtension.INSTANCE.getAdditionalBowPullTicks(entity);
    }
}
