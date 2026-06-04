package de.fuballer.mcendgame.client.mixin.living_entity;

import de.fuballer.mcendgame.client.accessor.LivingEntityCapeDataAccessor;
import de.fuballer.mcendgame.main.util.extension.EntityExtension;
import net.minecraft.client.entity.ClientAvatarState;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityCapeDataMixin implements LivingEntityCapeDataAccessor {
    @Unique
    private final ClientAvatarState capeState = new ClientAvatarState();

    @Inject(
            method = "tick",
            at = @At("HEAD")
    )
    void updateCapeState(CallbackInfo ci) {
        var entity = (LivingEntity) (Object) this;
        if (!EntityExtension.INSTANCE.needsCapeData(entity)) return;
        capeState.tick(entity.position(), entity.getDeltaMovement());
    }

    @Override
    public ClientAvatarState mcendgame$getCapeState() {
        return capeState;
    }
}
