package de.fuballer.mcendgame.main.mixin.living_entity;

import de.fuballer.mcendgame.main.accessor.LivingEntityCompanionAccessor;
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
public class LivingEntityCompanionMixin implements LivingEntityCompanionAccessor {
    @Unique
    private static final String COMPANION_NBT = "isCompanion";
    @Unique
    private static final EntityDataAccessor<Boolean> IS_COMPANION = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.BOOLEAN);

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void defineSynchedData(SynchedEntityData.Builder entityData, CallbackInfo ci) {
        entityData.define(IS_COMPANION, false);
    }

    @Override
    public boolean mcendgame$isCompanion() {
        var entity = (LivingEntity) (Object) this;
        return entity.getEntityData().get(IS_COMPANION);
    }

    @Override
    public void mcendgame$setCompanion() {
        var entity = (LivingEntity) (Object) this;
        entity.getEntityData().set(IS_COMPANION, true);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void writeNBT(ValueOutput output, CallbackInfo ci) {
        if (!mcendgame$isCompanion()) return;
        output.putBoolean(COMPANION_NBT, true);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void readNBT(ValueInput input, CallbackInfo ci) {
        if (!input.getBooleanOr(COMPANION_NBT, false)) return;
        mcendgame$setCompanion();
    }
}
