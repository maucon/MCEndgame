package de.fuballer.mcendgame.client.mixin.dual_wield;

import de.fuballer.mcendgame.client.accessor.PlayerDualWieldAccessor;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Player.class)
public class PlayerDualWieldMixin implements PlayerDualWieldAccessor {
    @Unique
    InteractionHand dualWieldHand = InteractionHand.MAIN_HAND;

    @Override
    public void mcendgame$setDualWieldHand(InteractionHand dualWieldHand) {
        this.dualWieldHand = dualWieldHand;
    }

    @Override
    public InteractionHand mcendgame$getDualWieldHand() {
        return dualWieldHand;
    }
}
