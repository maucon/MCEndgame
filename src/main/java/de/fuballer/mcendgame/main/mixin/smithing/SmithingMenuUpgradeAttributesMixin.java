package de.fuballer.mcendgame.main.mixin.smithing;

import com.llamalad7.mixinextras.sugar.Local;
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SmithingRecipe;
import net.minecraft.world.item.crafting.SmithingRecipeInput;
import net.minecraft.world.item.crafting.SmithingTransformRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Optional;

@Mixin(SmithingMenu.class)
public class SmithingMenuUpgradeAttributesMixin {
    @Inject(
            method = "createResult",
            at = @At(value = "INVOKE", target = "Ljava/util/Optional;ifPresentOrElse(Ljava/util/function/Consumer;Ljava/lang/Runnable;)V"),
            cancellable = true)
    void updateResult(
            CallbackInfo ci,
            @Local(name = "input") SmithingRecipeInput input,
            @Local(name = "foundRecipe") Optional<RecipeHolder<SmithingRecipe>> foundRecipe
    ) {
        if (foundRecipe.isEmpty()) return;
        var recipeEntry = foundRecipe.get();
        if (!(recipeEntry.value() instanceof SmithingTransformRecipe recipe)) return;

        var inputStack = input.base();
        var attributes = CustomAttributesExtensions.INSTANCE.getCustomAttributes(inputStack);
        if (attributes.isEmpty()) return;

        var inputCopy = inputStack.copy();
        CustomAttributesExtensions.INSTANCE.updateCustomAttributes(inputCopy, new ArrayList<>());
        var slot = attributes.getFirst().getSlot();

        var template = input.template();
        var recipeInput = new SmithingRecipeInput(template, inputCopy, input.addition());

        var resultStack = recipe.assemble(recipeInput);
        CustomAttributesExtensions.INSTANCE.setCustomAttributes(resultStack, attributes, slot);

        var accessor = (ItemCombinerMenuAccessor) this;
        var output = accessor.getResultSlots();
        output.setRecipeUsed(recipeEntry);
        output.setItem(0, resultStack);

        ci.cancel();
    }
}
