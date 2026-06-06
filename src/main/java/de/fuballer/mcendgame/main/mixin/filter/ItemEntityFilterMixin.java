package de.fuballer.mcendgame.main.mixin.filter;

import de.fuballer.mcendgame.main.component.item_filter.PlayerItemPickupCommand;
import de.maucon.mauconframework.command.CommandGateway;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public class ItemEntityFilterMixin {
    @Inject(method = "playerTouch", at = @At("HEAD"), cancellable = true)
    void applyFilter(Player player, CallbackInfo ci) {
        var itemEntity = (ItemEntity) (Object) this;
        var itemStack = itemEntity.getItem();
        var item = itemStack.getItem();

        var command = new PlayerItemPickupCommand(player, item);
        CommandGateway.INSTANCE.apply(command);

        if (command.isCancelled()){
            ci.cancel();
        }
    }
}
