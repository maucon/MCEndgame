package de.fuballer.mcendgame.main.mixin.mob_entity;

import de.fuballer.mcendgame.main.messaging.misc.MobEntityTargetCommand;
import de.maucon.mauconframework.command.CommandGateway;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mob.class)
public class MobTargetCommandMixin {
    @Inject(method = "setTarget", at = @At("HEAD"), cancellable = true)
    void setTarget(LivingEntity target, CallbackInfo ci) {
        var entity = (Mob) (Object) this;
        var mobEntityTargetCommand = MobEntityTargetCommand.Companion.of(entity, target);
        var cmd = CommandGateway.INSTANCE.apply(mobEntityTargetCommand);

        if (cmd.getCanTarget()) return;

        ci.cancel();
    }
}
