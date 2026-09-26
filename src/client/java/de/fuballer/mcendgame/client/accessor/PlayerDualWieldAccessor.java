package de.fuballer.mcendgame.client.accessor;

import net.minecraft.world.InteractionHand;

public interface PlayerDualWieldAccessor {
    void mcendgame$setDualWieldHand(InteractionHand dualWieldHand);

    InteractionHand mcendgame$getDualWieldHand();
}
