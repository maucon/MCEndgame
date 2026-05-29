package de.fuballer.mcendgame.main.accessor;

import de.fuballer.mcendgame.main.component.portal.teleport.TeleportLocation;

public interface PlayerEntityDungeonExitAccessor {
    TeleportLocation mcendgame$getDungeonExitLocation();

    void mcendgame$setDungeonExitLocation(TeleportLocation location);
}
