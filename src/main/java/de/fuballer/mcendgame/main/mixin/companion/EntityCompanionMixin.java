package de.fuballer.mcendgame.main.mixin.companion;

import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityCompanionMixin {
    @Inject(method = "canUsePortal", at = @At("HEAD"), cancellable = true)
    void canUsePortals(boolean ignorePassenger, CallbackInfoReturnable<Boolean> cir) {
        var entity = (Entity) (Object) this;
        if (!(entity instanceof LivingEntity livingEntity)) return;
        if (!EntityMixinExtension.INSTANCE.isCompanion(livingEntity)) return;

        cir.setReturnValue(false);
    }
}
