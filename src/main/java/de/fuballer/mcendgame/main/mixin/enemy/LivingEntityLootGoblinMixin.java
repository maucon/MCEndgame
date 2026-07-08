package de.fuballer.mcendgame.main.mixin.enemy;

import de.fuballer.mcendgame.main.accessor.LivingEntityLootGoblinAccessor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityLootGoblinMixin implements LivingEntityLootGoblinAccessor {
    @Unique
    private static final String LOOT_GOBLIN_NBT = "isLootGoblin";
    @Unique
    private boolean isLootGoblin = false;

    @Override
    public boolean mcendgame$isLootGoblin() {
        return isLootGoblin;
    }

    @Override
    public void mcendgame$setLootGoblin(boolean isLootGoblin) {
        this.isLootGoblin = isLootGoblin;
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void writeNBT(NbtCompound nbt, CallbackInfo ci) {
        if (!isLootGoblin) return;
        nbt.putBoolean(LOOT_GOBLIN_NBT, true);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void readNBT(NbtCompound nbt, CallbackInfo ci) {
        isLootGoblin = nbt.getBoolean(LOOT_GOBLIN_NBT);
    }
}
