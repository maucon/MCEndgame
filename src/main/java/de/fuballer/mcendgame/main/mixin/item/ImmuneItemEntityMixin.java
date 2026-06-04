package de.fuballer.mcendgame.main.mixin.item;

import de.fuballer.mcendgame.main.messaging.misc.ItemEntityDamageCommand;
import de.maucon.mauconframework.command.CommandGateway;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemEntity.class)
public class ImmuneItemEntityMixin {
    @Inject(method = "hurtServer", at = @At("HEAD"), cancellable = true)
    void damage(ServerLevel world, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        var entity = (ItemEntity) (Object) this;
        var itemEntityDamagedCommand = ItemEntityDamageCommand.Companion.of(entity, source);
        var cmd = CommandGateway.INSTANCE.apply(itemEntityDamagedCommand);

        if (!cmd.getIgnoresDamage()) return;

        cir.setReturnValue(false);
    }
}
