package de.fuballer.mcendgame.main.mixin.living_entity;

import de.fuballer.mcendgame.main.messaging.misc.LivingEntityHealthRecoveryCommand;
import de.maucon.mauconframework.command.CommandGateway;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public class LivingEntityHealMixin {
    @ModifyVariable(method = "heal(F)V", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    float heal(float amount) {
        var entity = (LivingEntity) (Object) this;

        var command = new LivingEntityHealthRecoveryCommand(entity, amount);
        var cmd = CommandGateway.INSTANCE.apply(command);

        return cmd.getFinalAmount();
    }
}
