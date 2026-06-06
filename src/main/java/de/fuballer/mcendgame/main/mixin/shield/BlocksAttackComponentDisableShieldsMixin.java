package de.fuballer.mcendgame.main.mixin.shield;

import de.fuballer.mcendgame.main.messaging.misc.ShieldHitEvent;
import de.maucon.mauconframework.event.EventGateway;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BlocksAttacks;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlocksAttacks.class)
public class BlocksAttackComponentDisableShieldsMixin {
    @Inject(
            method = "hurtBlockingItem",
            at = @At("HEAD")
    )
    void disableShields(
            Level world,
            ItemStack stack,
            LivingEntity entity,
            InteractionHand hand,
            float itemDamage,
            CallbackInfo ci
    ) {
        var event = new ShieldHitEvent(entity, stack);
        EventGateway.INSTANCE.publish(event);
    }
}
