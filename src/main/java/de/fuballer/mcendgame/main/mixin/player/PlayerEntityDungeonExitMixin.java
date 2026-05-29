package de.fuballer.mcendgame.main.mixin.player;

import de.fuballer.mcendgame.main.accessor.PlayerEntityDungeonExitAccessor;
import de.fuballer.mcendgame.main.component.portal.teleport.TeleportLocation;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(PlayerEntity.class)
public class PlayerEntityDungeonExitMixin implements PlayerEntityDungeonExitAccessor {
    @Unique
    private TeleportLocation dungeonExitLocation;

    @Override
    public TeleportLocation mcendgame$getDungeonExitLocation() {
        return dungeonExitLocation;
    }

    @Override
    public void mcendgame$setDungeonExitLocation(TeleportLocation location) {
        this.dungeonExitLocation = location;
    }
}
