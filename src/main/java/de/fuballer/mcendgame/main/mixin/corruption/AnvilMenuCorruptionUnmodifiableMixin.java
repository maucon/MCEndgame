package de.fuballer.mcendgame.main.mixin.corruption;

import de.fuballer.mcendgame.main.messaging.misc.CanAnvilForgeCommand;
import de.maucon.mauconframework.command.CommandGateway;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilMenu.class)
public class AnvilMenuCorruptionUnmodifiableMixin {
    @Shadow
    @Final
    private DataSlot cost;

    @Inject(method = "createResult", at = @At("HEAD"), cancellable = true)
    void on(CallbackInfo ci) {
        var accessor = (ItemCombinerMenuOutputAccessorMixin) this;

        var input = accessor.getInputSlots();
        var stack0 = input.getItem(0);
        var stack1 = input.getItem(1);

        var anvilInputCommand = new CanAnvilForgeCommand(stack0, stack1, true);
        var cmd = CommandGateway.INSTANCE.apply(anvilInputCommand);
        if (cmd.getCanForge()) return;

        var output = accessor.getResultSlots();
        output.setItem(0, ItemStack.EMPTY);
        cost.set(0);

        ci.cancel();
    }
}
