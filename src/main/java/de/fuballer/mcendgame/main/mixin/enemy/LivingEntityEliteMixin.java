package de.fuballer.mcendgame.main.mixin.enemy;

import de.fuballer.mcendgame.main.accessor.LivingEntityEliteAccessor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
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

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void writeNBT(ValueOutput view, CallbackInfo ci) {
        if (!isElite) return;
        view.putBoolean(ELITE_NBT, true);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void readNBT(ValueInput view, CallbackInfo ci) {
        isElite = view.getBooleanOr(ELITE_NBT, false);
    }
}
