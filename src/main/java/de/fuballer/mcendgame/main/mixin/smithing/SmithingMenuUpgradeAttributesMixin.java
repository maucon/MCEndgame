package de.fuballer.mcendgame.main.mixin.smithing;

import com.llamalad7.mixinextras.sugar.Local;
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SmithingRecipe;
import net.minecraft.world.item.crafting.SmithingRecipeInput;
import net.minecraft.world.item.crafting.SmithingTransformRecipe;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Optional;

@Mixin(SmithingMenu.class)
public class SmithingMenuUpgradeAttributesMixin {
    @Shadow
    @Final
    private Level level;

    @Inject(
            method = "createResult",
            at = @At(value = "INVOKE", target = "Ljava/util/Optional;ifPresentOrElse(Ljava/util/function/Consumer;Ljava/lang/Runnable;)V"),
            cancellable = true)
    void updateResult(
            CallbackInfo ci,
            @Local SmithingRecipeInput originalRecipeInput,
            @Local Optional<RecipeHolder<SmithingRecipe>> optional
    ) {
        if (optional.isEmpty()) return;
        var recipeEntry = optional.get();
        if (!(recipeEntry.value() instanceof SmithingTransformRecipe recipe)) return;

        var inputStack = originalRecipeInput.base();
        var attributes = CustomAttributesExtensions.INSTANCE.getCustomAttributes(inputStack);
        if (attributes.isEmpty()) return;

        var inputCopy = inputStack.copy();
        CustomAttributesExtensions.INSTANCE.updateCustomAttributes(inputCopy, new ArrayList<>());
        var slot = attributes.getFirst().getSlot();

        var template = originalRecipeInput.template();
        var recipeInput = new SmithingRecipeInput(template, inputCopy, originalRecipeInput.addition());

        var resultStack = recipe.assemble(recipeInput, level.registryAccess());
        CustomAttributesExtensions.INSTANCE.setCustomAttributes(resultStack, attributes, slot);

        var accessor = (ItemCombinerMenuAccessor) this;
        var output = accessor.getResultSlots();
        output.setRecipeUsed(recipeEntry);
        output.setItem(0, resultStack);

        ci.cancel();
    }
}
