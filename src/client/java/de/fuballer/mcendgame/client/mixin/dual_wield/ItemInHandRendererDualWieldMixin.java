package de.fuballer.mcendgame.client.mixin.dual_wield;

import com.llamalad7.mixinextras.sugar.Local;
import de.fuballer.mcendgame.client.accessor.PlayerDualWieldAccessor;
import de.fuballer.mcendgame.main.util.extension.EntityExtension;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemInHandRenderer.class)
public class ItemInHandRendererDualWieldMixin {
    @Shadow
    private ItemStack offHandItem;

    @Shadow
    private ItemStack mainHandItem;

    @ModifyVariable(
            method = "tick",
            at = @At(value = "STORE"),
            name = "mainHandTargetHeight"
    )
    private float mcendgame$modifyDualWieldMainHandTargetHeight(
            float mainHandTargetHeight,
            @Local(name = "nextMainHand") ItemStack nextMainHand,
            @Local(name = "player") LocalPlayer player
    ) {
        if (mainHandItem != nextMainHand) return mainHandTargetHeight;
        if (!EntityExtension.INSTANCE.isDualWielding(player)) return mainHandTargetHeight;
        if (((PlayerDualWieldAccessor) player).mcendgame$getDualWieldHand() == InteractionHand.MAIN_HAND) return mainHandTargetHeight;

        return 1F;
    }

    @ModifyVariable(
            method = "tick",
            at = @At(value = "STORE"),
            name = "offHandTargetHeight"
    )
    private float mcendgame$modifyDualWieldOffHandTargetHeight(
            float offHandTargetHeight,
            @Local(name = "nextOffHand") ItemStack nextOffHand,
            @Local(name = "player") LocalPlayer player,
            @Local(name = "attackAnim") float attackAnim
    ) {
        if (offHandItem != nextOffHand) return offHandTargetHeight;
        if (!EntityExtension.INSTANCE.isDualWielding(player)) return offHandTargetHeight;
        if (((PlayerDualWieldAccessor) player).mcendgame$getDualWieldHand() == InteractionHand.MAIN_HAND) return offHandTargetHeight;

        return attackAnim * attackAnim * attackAnim;
    }
}
