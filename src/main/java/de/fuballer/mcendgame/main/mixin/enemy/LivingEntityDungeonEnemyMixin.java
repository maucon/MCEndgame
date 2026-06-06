package de.fuballer.mcendgame.main.mixin.enemy;

import de.fuballer.mcendgame.main.accessor.LivingEntityDungeonEnemyAccessor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
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

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void writeNBT(ValueOutput view, CallbackInfo ci) {
        if (isDungeonEnemy) view.putBoolean(DUNGEON_ENEMY_NBT, true);
        if (dropsAspectOfGhosts) view.putBoolean(DROPS_ASPECT_OF_GHOSTS_NBT, true);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void readNBT(ValueInput view, CallbackInfo ci) {
        isDungeonEnemy = view.getBooleanOr(DUNGEON_ENEMY_NBT, false);
        dropsAspectOfGhosts = view.getBooleanOr(DROPS_ASPECT_OF_GHOSTS_NBT, false);
    }
}
