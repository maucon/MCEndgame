package de.fuballer.mcendgame.main.mixin.living_entity;

import de.fuballer.mcendgame.main.accessor.LivingEntityWebbedAccessor;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityWebbedMixin implements LivingEntityWebbedAccessor {
    @Unique
    private static final EntityDataAccessor<Boolean> IS_WEBBED =
            SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.BOOLEAN);

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void initDataTracker(SynchedEntityData.Builder builder, CallbackInfo ci) {
        builder.define(IS_WEBBED, false);
    }

    @Override
    public void mcendgame$setWebbed(boolean webbed) {
        LivingEntity entity = (LivingEntity) (Object) this;
        entity.getEntityData().set(IS_WEBBED, webbed);
    }

    @Override
    public boolean mcendgame$isWebbed() {
        LivingEntity entity = (LivingEntity) (Object) this;
        return entity.getEntityData().get(IS_WEBBED);
    }
}
