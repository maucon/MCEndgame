package de.fuballer.mcendgame.main.mixin.command;

import de.fuballer.mcendgame.main.component.item.custom.UniqueAttributesItemInterface;
import de.fuballer.mcendgame.main.component.item.custom.aspect.AspectItem;
import de.fuballer.mcendgame.main.component.item.custom.totem.TotemItem;
import net.minecraft.commands.arguments.item.ItemInput;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemInput.class)
public class ItemInputMixin {
    @Shadow
    @Final
    private Holder<Item> item;

    @Inject(
            method = "createItemStack",
            at = @At("HEAD"),
            cancellable = true
    )
    void mcendgame$createCustomStack(
            int count,
            CallbackInfoReturnable<ItemStack> cir
    ) {
        var itemClass = item.value();
        if (itemClass instanceof UniqueAttributesItemInterface
                || itemClass instanceof TotemItem
                || itemClass instanceof AspectItem
        ) {
            cir.setReturnValue(itemClass.getDefaultInstance());
        }
    }
}
