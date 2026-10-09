package de.fuballer.mcendgame.client.mixin.dual_wield;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import de.fuballer.mcendgame.client.accessor.PlayerDualWieldAccessor;
import de.fuballer.mcendgame.main.util.extension.EntityExtension;
import net.minecraft.client.player.FirstPersonHandsAndItems;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FirstPersonHandsAndItems.class)
public class ItemInHandRendererDualWieldMixin {
    private static final float FIRST_PERSON_HAND_SWAP_HEIGHT = 1F;

    @Shadow
    private ItemStack mainHandItem;

    @Shadow
    private ItemStack offHandItem;

    @ModifyExpressionValue(
            method = "tick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getItemSwapScale(F)F")
    )
    private float mcendgame$modifyDualWieldHandSwapScale(float swapScale, LocalPlayer player) {
        // The off hand is the one that animates while dual wielding, so the main hand height keeps its normal value.
        if (mainHandItem != player.getMainHandItem()) return swapScale;
        if (offHandItem != player.getOffhandItem()) return swapScale;
        if (!EntityExtension.INSTANCE.isDualWielding(player)) return swapScale;
        if (((PlayerDualWieldAccessor) player).mcendgame$getDualWieldHand() == InteractionHand.MAIN_HAND) return swapScale;

        return FIRST_PERSON_HAND_SWAP_HEIGHT;
    }
}
