package de.fuballer.mcendgame.client.mixin.dual_wield;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
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

    @ModifyExpressionValue(
            method = "startAttack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;getAttackAnimation()Lnet/minecraft/world/item/component/SwingAnimation;"
            )
    )
    private SwingAnimation mcendgame$setDualWieldSwingHand(SwingAnimation original) {
        if (player == null) return original;
        if (!EntityExtension.INSTANCE.isDualWielding(player)) return original;

        var accessor = (PlayerDualWieldAccessor) player;

        var newHand = InteractionHand.MAIN_HAND;
        var tickCount = player.tickCount;
        if (lastDualWieldHitTickCount > 0
                && tickCount - lastDualWieldHitTickCount < 60
                && accessor.mcendgame$getDualWieldHand() == InteractionHand.MAIN_HAND
        ) newHand = InteractionHand.OFF_HAND;

        accessor.mcendgame$setDualWieldHand(newHand);
        lastDualWieldHitTickCount = tickCount;

        return newHand == InteractionHand.MAIN_HAND ? original : player.getOffhandItem().getAttackAnimation();
    }
}
