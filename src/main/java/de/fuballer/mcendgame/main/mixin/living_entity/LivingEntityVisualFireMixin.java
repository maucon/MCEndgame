package de.fuballer.mcendgame.main.mixin.living_entity;

import de.fuballer.mcendgame.main.accessor.LivingEntityVisualFireAccessor;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityVisualFireMixin implements LivingEntityVisualFireAccessor {
    @Unique
    private static final String VISUAL_FIRE_NBT = "visualFire";

    @Unique
    private static final EntityDataAccessor<Boolean> VISUAL_FIRE = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.BOOLEAN);

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void initDataTracker(SynchedEntityData.Builder builder, CallbackInfo ci) {
        builder.define(VISUAL_FIRE, false);
    }

    @Override
    public void mcendgame$setVisualFire(boolean fire) {
        LivingEntity entity = (LivingEntity) (Object) this;
        entity.getEntityData().set(VISUAL_FIRE, fire);
    }

    @Override
    public boolean mcendgame$hasVisualFire() {
        LivingEntity entity = (LivingEntity) (Object) this;
        return entity.getEntityData().get(VISUAL_FIRE);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    void writeNBT(ValueOutput view, CallbackInfo ci) {
        if (mcendgame$hasVisualFire()) view.putBoolean(VISUAL_FIRE_NBT, true);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void readNBT(ValueInput view, CallbackInfo ci) {
        mcendgame$setVisualFire(view.getBooleanOr(VISUAL_FIRE_NBT, false));
    }
}
