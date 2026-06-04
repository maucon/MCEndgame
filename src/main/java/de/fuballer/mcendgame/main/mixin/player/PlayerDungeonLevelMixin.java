package de.fuballer.mcendgame.main.mixin.player;

import de.fuballer.mcendgame.main.accessor.PlayerEntityDungeonLevelAccessor;
import de.fuballer.mcendgame.main.component.dungeon.level.PlayerDungeonLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerDungeonLevelMixin implements PlayerEntityDungeonLevelAccessor {
    @Unique
    private PlayerDungeonLevel playerDungeonLevel = new PlayerDungeonLevel();

    @Override
    public PlayerDungeonLevel mcendgame$getDungeonLevel() {
        return playerDungeonLevel;
    }

    @Override
    public void mcendgame$setDungeonLevel(PlayerDungeonLevel dungeonLevel) {
        this.playerDungeonLevel = dungeonLevel;
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void writeData(ValueOutput view, CallbackInfo ci) {
        PlayerDungeonLevel.Companion.write(playerDungeonLevel, view);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void readData(ValueInput view, CallbackInfo ci) {
        playerDungeonLevel = PlayerDungeonLevel.Companion.read(view);
    }
}
