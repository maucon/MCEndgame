package de.fuballer.mcendgame.main.mixin.item;

import de.fuballer.mcendgame.main.messaging.misc.DamageItemStackCommand;
import de.maucon.mauconframework.command.CommandGateway;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemStack.class)
public class ItemStackDamageMixin {
    @ModifyVariable(
            method = "processDurabilityChange",
            at = @At("HEAD"),
            ordinal = 0,
            argsOnly = true
    )
    int getBaseDamage(int baseDamage, int bD, ServerLevel serverWorld) { // bD duplicates baseDamage to match method signature for mixin injection
        var command = new DamageItemStackCommand(baseDamage, serverWorld);
        var cmd = CommandGateway.INSTANCE.apply(command);
        return cmd.getDamage();
    }
}
