package de.fuballer.mcendgame.main.mixin.wolf_entity;

import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Wolf.class)
public class WolfCompanionInteractMixin {
    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    void interactMob(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        var wolf = (Wolf) (Object) this;
        if (!EntityMixinExtension.INSTANCE.isCompanion(wolf)) return;
        cir.setReturnValue(InteractionResult.PASS);
    }
}
