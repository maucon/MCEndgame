package de.fuballer.mcendgame.client.mixin.dual_wield;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import de.fuballer.mcendgame.client.accessor.PlayerDualWieldAccessor;
import de.fuballer.mcendgame.main.util.extension.EntityExtension;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.component.SwingAnimation;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Minecraft.class)
public class MinecraftDualWieldMixin {
    @Shadow
    @Nullable
    public LocalPlayer player;

    @Unique
    int lastDualWieldHitTickCount = 0;

    /**
     * 26.3 splits the swing into {@code swing(InteractionHand, SwingAnimation, boolean)} and
     * {@code startAttack} hardcodes {@link InteractionHand#MAIN_HAND}. Rewriting only the animation
     * therefore animated the main hand with the offhand's animation, so the hand and its animation
     * both have to be swapped together here.
     */
    @WrapOperation(
            method = "startAttack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/player/LocalPlayer;swing(Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/item/component/SwingAnimation;Z)Z"
            )
    )
    private boolean mcendgame$setDualWieldSwingHand(
            LocalPlayer instance,
            InteractionHand hand,
            SwingAnimation attackAnimation,
            boolean fromClient,
            Operation<Boolean> original
    ) {
        if (player == null) return original.call(instance, hand, attackAnimation, fromClient);
        if (!EntityExtension.INSTANCE.isDualWielding(player)) return original.call(instance, hand, attackAnimation, fromClient);

        var accessor = (PlayerDualWieldAccessor) player;

        var newHand = InteractionHand.MAIN_HAND;
        var tickCount = player.tickCount;
        if (lastDualWieldHitTickCount > 0
                && tickCount - lastDualWieldHitTickCount < 60
                && accessor.mcendgame$getDualWieldHand() == InteractionHand.MAIN_HAND
        ) newHand = InteractionHand.OFF_HAND;

        accessor.mcendgame$setDualWieldHand(newHand);
        lastDualWieldHitTickCount = tickCount;

        if (newHand == hand) return original.call(instance, hand, attackAnimation, fromClient);

        return original.call(instance, newHand, instance.getItemInHand(newHand).getAttackAnimation(), fromClient);
    }
}
