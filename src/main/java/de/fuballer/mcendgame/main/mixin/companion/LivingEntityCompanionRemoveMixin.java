package de.fuballer.mcendgame.main.mixin.companion;

import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityCompanionRemoveMixin {
    @Inject(method = "baseTick", at = @At("HEAD"), cancellable = true)
    void remove(CallbackInfo ci) {
        var entity = (LivingEntity) (Object) this;
        if (entity.tickCount % 20 != 0) return;
        if (!EntityMixinExtension.INSTANCE.isCompanion(entity)) return;
        if (!(entity instanceof OwnableEntity ownableEntity)) return;
        var owner = ownableEntity.getOwner();
        if (owner != null && owner.isAlive()) return;
        entity.discard();
        ci.cancel();
    }
}
