package de.fuballer.mcendgame.main.mixin.corruption;

import com.llamalad7.mixinextras.sugar.Local;
import de.fuballer.mcendgame.main.messaging.misc.CraftingResultCommand;
import de.maucon.mauconframework.command.CommandGateway;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.level.block.CrafterBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(CrafterBlock.class)
public class CrafterBlockCorruptionUnmodifiableMixin {
    @ModifyVariable(
            method = "dispenseFrom",
            at = @At(value = "STORE"),
            ordinal = 0
    )
    ItemStack on(
            ItemStack originalStack,
            @Local CraftingInput recipeInput
    ) {
        var recipeCommand = new CraftingResultCommand(recipeInput, originalStack);
        var cmd = CommandGateway.INSTANCE.apply(recipeCommand);
        return cmd.getResult();
    }
}