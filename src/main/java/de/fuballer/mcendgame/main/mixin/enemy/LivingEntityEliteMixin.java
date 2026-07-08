package de.fuballer.mcendgame.main.mixin.enemy;

import de.fuballer.mcendgame.main.accessor.LivingEntityEliteAccessor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityEliteMixin implements LivingEntityEliteAccessor {
    @Unique
    private static final String ELITE_NBT = "isElite";
    @Unique
    private boolean isElite = false;

    @Override
    public boolean mcendgame$isElite() {
        return isElite;
    }

    @Override
    public void mcendgame$setElite(boolean isElite) {
        this.isElite = isElite;
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void writeNBT(NbtCompound nbt, CallbackInfo ci) {
        if (!isElite) return;
        nbt.putBoolean(ELITE_NBT, true);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void readNBT(NbtCompound nbt, CallbackInfo ci) {
        isElite = nbt.getBoolean(ELITE_NBT);
    }
}
