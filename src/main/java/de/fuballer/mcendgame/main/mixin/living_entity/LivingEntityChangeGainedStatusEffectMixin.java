package de.fuballer.mcendgame.main.mixin.living_entity;

import de.fuballer.mcendgame.main.messaging.misc.GainStatusEffectCommand;
import de.maucon.mauconframework.command.CommandGateway;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public class LivingEntityChangeGainedStatusEffectMixin {
    @ModifyVariable(
            method = "addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z",
            at = @At("HEAD"),
            argsOnly = true
    )
    private MobEffectInstance modifyGainedStatusEffect(MobEffectInstance originalEffect) {
        var entity = (LivingEntity) (Object) this;
        var command = new GainStatusEffectCommand(entity, originalEffect);
        var cmd = CommandGateway.INSTANCE.apply(command);
        return cmd.getEffect();
    }
}
