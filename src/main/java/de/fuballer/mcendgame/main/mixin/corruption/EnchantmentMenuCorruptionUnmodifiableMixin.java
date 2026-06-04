package de.fuballer.mcendgame.main.mixin.corruption;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import de.fuballer.mcendgame.main.messaging.misc.CanEnchantItemCommand;
import de.maucon.mauconframework.command.CommandGateway;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EnchantmentMenu.class)
public class EnchantmentMenuCorruptionUnmodifiableMixin {
    @ModifyExpressionValue(
            method = "slotsChanged",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;isEnchantable()Z"
            )
    )
    boolean modifyIsEnchantable(
            boolean original,
            @Local ItemStack itemStack
    ) {
        if (!original) return false;

        var enchantCommand = new CanEnchantItemCommand(itemStack, true);
        var cmd = CommandGateway.INSTANCE.apply(enchantCommand);
        return cmd.getCanEnchant();
    }
}
