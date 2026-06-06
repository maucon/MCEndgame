package de.fuballer.mcendgame.main.mixin.entity;

import de.fuballer.mcendgame.main.accessor.EntityForcedGlowColorAccessor;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityForcedGlowColorMixin implements EntityForcedGlowColorAccessor {
    @Shadow
    @Final
    protected SynchedEntityData entityData;
    @Unique
    private static final EntityDataAccessor<Integer> FORCED_GLOW_COLOR = SynchedEntityData.defineId(Entity.class, EntityDataSerializers.INT);

    @ModifyVariable(
            method = "<init>",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;defineSynchedData(Lnet/minecraft/network/syncher/SynchedEntityData$Builder;)V", shift = At.Shift.AFTER)
    )
    private SynchedEntityData.Builder modifyBuilder(SynchedEntityData.Builder builder) {
        builder.define(FORCED_GLOW_COLOR, -1);
        return builder;
    }

    @Inject(method = "getTeamColor", at = @At("HEAD"), cancellable = true)
    void getTeamColorValue(CallbackInfoReturnable<Integer> cir) {
        var forcedColor = entityData.get(FORCED_GLOW_COLOR);
        if (forcedColor == -1) return;
        cir.setReturnValue(forcedColor);
    }

    @Override
    public void mcendgame$setForcedGlowColor(int color) {
        entityData.set(FORCED_GLOW_COLOR, color);
    }
}