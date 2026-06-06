package de.fuballer.mcendgame.main.mixin.player;

import de.fuballer.mcendgame.main.accessor.PlayerEntityInsideDungeonAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerInsideDungeonMixin implements PlayerEntityInsideDungeonAccessor {
    @Unique
    private static final String IS_INSIDE_DUNGEON_NBT = "isInsideDungeon";

    @Unique
    private boolean isInsideDungeon = false;

    @Override
    public boolean mcendgame$isInsideDungeon() {
        return isInsideDungeon;
    }

    @Override
    public void mcendgame$setInsideDungeon(boolean isInsideDungeon) {
        this.isInsideDungeon = isInsideDungeon;
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void writeNBT(ValueOutput view, CallbackInfo ci) {
        if (!isInsideDungeon) return;
        view.putBoolean(IS_INSIDE_DUNGEON_NBT, true);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void readNBT(ValueInput view, CallbackInfo ci) {
        isInsideDungeon = view.getBooleanOr(IS_INSIDE_DUNGEON_NBT, false);
    }
}
