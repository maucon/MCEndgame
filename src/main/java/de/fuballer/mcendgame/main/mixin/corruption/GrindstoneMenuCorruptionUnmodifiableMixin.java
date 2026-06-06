package de.fuballer.mcendgame.main.mixin.corruption;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import de.fuballer.mcendgame.main.messaging.misc.GrindstoneOutputCommand;
import de.maucon.mauconframework.command.CommandGateway;
import net.minecraft.world.inventory.GrindstoneMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GrindstoneMenu.class)
public class GrindstoneMenuCorruptionUnmodifiableMixin {
    @ModifyReturnValue(method = "computeResult", at = @At("RETURN"))
    ItemStack getOutputStack(
            ItemStack originalOutput,
            ItemStack firstInput,
            ItemStack secondInput
    ) {
        var canUseGrindstoneCommand = new GrindstoneOutputCommand(firstInput, secondInput, originalOutput);
        var cmd = CommandGateway.INSTANCE.apply(canUseGrindstoneCommand);
        return cmd.getOutput();
    }
}
