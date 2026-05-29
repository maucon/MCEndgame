package de.fuballer.mcendgame.main.mixin.item;

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions;
import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.CreativeInventoryActionC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerCreativeCopyMixin {
    @Shadow
    @Final
    private ServerPlayerEntity player;

    @ModifyVariable(
            method = "onCreativeInventoryAction",
            at = @At("HEAD"),
            ordinal = 0,
            argsOnly = true
    )
    CreativeInventoryActionC2SPacket rerollAttributeIdsOnCreativeCopy(CreativeInventoryActionC2SPacket packet) {
        var stack = packet.stack();
        var attributes = CustomAttributesExtensions.INSTANCE.getCustomAttributes(stack);
        if (attributes.isEmpty()) return packet;

        var incomingAttributeIds = attributes.stream().map(CustomAttribute::getId).collect(Collectors.toSet());
        var inventory = player.getInventory();
        for (int i = 0; i < inventory.size(); i++) {
            if (i == packet.slot()) continue;

            var inventoryAttributeIds = getAttributeIds(inventory.getStack(i));
            var hasOverlap = inventoryAttributeIds.stream().anyMatch(incomingAttributeIds::contains);
            if (!hasOverlap) continue;

            var rerolledStack = stack.copy();
            CustomAttributesExtensions.INSTANCE.rerollCustomAttributeIds(rerolledStack);
            return new CreativeInventoryActionC2SPacket(packet.slot(), rerolledStack);
        }

        return packet;
    }

    private Set<UUID> getAttributeIds(ItemStack stack) {
        return CustomAttributesExtensions.INSTANCE
                .getCustomAttributes(stack)
                .stream()
                .map(CustomAttribute::getId)
                .collect(Collectors.toSet());
    }
}
