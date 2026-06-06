package de.fuballer.mcendgame.main.mixin.corruption;

import com.llamalad7.mixinextras.sugar.Local;
import de.fuballer.mcendgame.main.messaging.misc.CraftingResultCommand;
import de.maucon.mauconframework.command.CommandGateway;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(CraftingMenu.class)
public class CraftingMenuCorruptionUnmodifiableMixin {
    @ModifyVariable(method = "slotChangedCraftingGrid",
            at = @At(value = "STORE"),
            ordinal = 1
    )
    private static ItemStack updateResult(
            ItemStack originalStack,
            @Local CraftingInput craftingRecipeInput
    ) {
        var recipeCommand = new CraftingResultCommand(craftingRecipeInput, originalStack);
        var cmd = CommandGateway.INSTANCE.apply(recipeCommand);
        return cmd.getResult();
    }
}
