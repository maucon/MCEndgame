package de.fuballer.mcendgame.main.mixin.enemy;

import de.fuballer.mcendgame.main.accessor.LivingEntityDungeonEnemyAccessor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityDungeonEnemyMixin implements LivingEntityDungeonEnemyAccessor {
    @Unique
    private static final String DUNGEON_ENEMY_NBT = "isDungeonEnemy";
    @Unique
    private static final String DROPS_ASPECT_OF_GHOSTS_NBT = "dropsAspectOfGhosts";

    @Unique
    private boolean isDungeonEnemy = false;

    @Unique
    private boolean dropsAspectOfGhosts = false;

    @Override
    public boolean mcendgame$isDungeonEnemy() {
        return isDungeonEnemy;
    }

    @Override
    public void mcendgame$setDungeonEnemy(boolean enemy) {
        isDungeonEnemy = enemy;
    }

    @Override
    public boolean mcendgame$dropsAspectOfGhosts() {
        return dropsAspectOfGhosts;
    }

    @Override
    public void mcendgame$setDropsAspectOfGhosts(boolean drops) {
        dropsAspectOfGhosts = drops;
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void writeNBT(NbtCompound nbt, CallbackInfo ci) {
        if (isDungeonEnemy) nbt.putBoolean(DUNGEON_ENEMY_NBT, true);
        if (dropsAspectOfGhosts) nbt.putBoolean(DROPS_ASPECT_OF_GHOSTS_NBT, true);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void readNBT(NbtCompound nbt, CallbackInfo ci) {
        isDungeonEnemy = nbt.getBoolean(DUNGEON_ENEMY_NBT);
        dropsAspectOfGhosts = nbt.getBoolean(DROPS_ASPECT_OF_GHOSTS_NBT);
    }
}
