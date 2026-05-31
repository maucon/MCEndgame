package de.fuballer.mcendgame.client.mixin.living_entity;

import de.fuballer.mcendgame.client.accessor.LivingEntityCapeDataAccessor;
import de.fuballer.mcendgame.main.util.extension.EntityExtension;
import net.minecraft.client.network.ClientPlayerLikeState;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityCapeDataMixin implements LivingEntityCapeDataAccessor {
    @Unique
    private final ClientPlayerLikeState capeState = new ClientPlayerLikeState();

    @Inject(
            method = "tick",
            at = @At("HEAD")
    )
    void updateCapeState(CallbackInfo ci) {
        var entity = (LivingEntity) (Object) this;
        if (!EntityExtension.INSTANCE.needsCapeData(entity)) return;
        capeState.tick(entity.getEntityPos(), entity.getVelocity());
    }

    @Override
    public ClientPlayerLikeState mcendgame$getCapeState() {
        return capeState;
    }
}
