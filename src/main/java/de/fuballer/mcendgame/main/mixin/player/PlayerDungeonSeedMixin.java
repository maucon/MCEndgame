package de.fuballer.mcendgame.main.mixin.player;

import de.fuballer.mcendgame.main.accessor.PlayerEntityDungeonSeedAccessor;
import de.fuballer.mcendgame.main.component.dungeon.seed.PlayerDungeonSeed;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerDungeonSeedMixin implements PlayerEntityDungeonSeedAccessor {
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

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void writeNBT(ValueOutput view, CallbackInfo ci) {
        if (dungeonSeed == null) return;
        PlayerDungeonSeed.Companion.write(dungeonSeed, view);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void readNBT(ValueInput view, CallbackInfo ci) {
        dungeonSeed = PlayerDungeonSeed.Companion.read(view);
    }
}
