package de.fuballer.mcendgame.main.mixin.item;

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions;
import net.minecraft.network.packet.c2s.play.CreativeInventoryActionC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerCreativeCopyMixin {
    @Shadow
    @Final
    private ServerPlayerEntity player;

    @Inject(
            method = "onCreativeInventoryAction",
            at = @At("HEAD")
    )
    void rerollAttributeIdsOnCreativeCopy(CreativeInventoryActionC2SPacket packet, CallbackInfo ci) {
        var stack = packet.stack();
        var attributes = CustomAttributesExtensions.INSTANCE.getCustomAttributes(stack);
        if (attributes.isEmpty()) return;

        var incomingAttributeIds = attributes.stream().map(it -> it.getId()).toList();
        var inventory = player.getInventory();
        for (int i = 0; i < inventory.size(); i++) {
            if (i == packet.slot()) continue;

            var inventoryAttributeIds = CustomAttributesExtensions.INSTANCE
                    .getCustomAttributes(inventory.getStack(i))
                    .stream()
                    .map(it -> it.getId())
                    .toList();
            if (!incomingAttributeIds.equals(inventoryAttributeIds)) continue;

            CustomAttributesExtensions.INSTANCE.rerollCustomAttributeIds(stack);
            return;
        }
    }
}
