package de.fuballer.mcendgame.main.mixin.corruption;

import com.llamalad7.mixinextras.sugar.Local;
import de.fuballer.mcendgame.main.messaging.misc.CanSmithCommand;
import de.maucon.mauconframework.command.CommandGateway;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SmithingRecipe;
import net.minecraft.world.item.crafting.SmithingRecipeInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Optional;

@Mixin(SmithingMenu.class)
public class SmithingMenuCorruptionUnmodifiableMixin {
    @ModifyVariable(
            method = "createResult",
            at = @At(value = "STORE"),
            ordinal = 0
    )
    Optional<RecipeHolder<SmithingRecipe>> updateResult(
            Optional<RecipeHolder<SmithingRecipe>> original,
            @Local SmithingRecipeInput smithingRecipeInput
    ) {
        if (original.isEmpty()) return original;

        var canSmithCommand = new CanSmithCommand(smithingRecipeInput, true);
        var cmd = CommandGateway.INSTANCE.apply(canSmithCommand);
        if (!cmd.getCanSmith()) return Optional.empty();

        return original;
    }
}
