package de.fuballer.mcendgame.main.mixin.player;

import de.fuballer.mcendgame.main.accessor.PlayerEntityDungeonSeedAccessor;
import de.fuballer.mcendgame.main.component.dungeon.seed.PlayerDungeonSeed;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityDungeonSeedMixin implements PlayerEntityDungeonSeedAccessor {
    @Unique
    @Nullable
    private PlayerDungeonSeed dungeonSeed = null;

    @Override
    @Nullable
    public PlayerDungeonSeed mcendgame$getDungeonSeed() {
        return dungeonSeed;
    }

    @Override
    public void mcendgame$setDungeonSeed(@Nullable PlayerDungeonSeed dungeonSeed) {
        this.dungeonSeed = dungeonSeed;
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void writeData(NbtCompound nbt, CallbackInfo ci) {
        if (dungeonSeed == null) return;
        PlayerDungeonSeed.Companion.write(dungeonSeed, nbt);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void readData(NbtCompound nbt, CallbackInfo ci) {
        dungeonSeed = PlayerDungeonSeed.Companion.read(nbt);
    }
}
