package de.fuballer.mcendgame.main.mixin.event;

import de.fuballer.mcendgame.main.messaging.misc.LivingEntityDeathEvent;
import de.fuballer.mcendgame.main.messaging.misc.LivingEntityDropCommand;
import de.maucon.mauconframework.command.CommandGateway;
import de.maucon.mauconframework.event.EventGateway;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityEventsMixin {
    @Shadow
    protected int playerHitTimer;

    @Shadow
    protected abstract void dropEquipment(ServerWorld world, DamageSource source, boolean causedByPlayer);

    @Shadow
    protected abstract void dropXp(Entity attacker);

    @Shadow
    protected abstract void dropInventory();

    @Shadow
    protected abstract boolean shouldDropLoot();

    @Shadow
    protected abstract void dropLoot(DamageSource damageSource, boolean causedByPlayer);

    @Inject(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;updatePostDeath()V"))
    private void baseTick(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (entity.isDead() && entity.deathTime == 0) {
            var event = new LivingEntityDeathEvent(entity);
            EventGateway.INSTANCE.publish(event);
        }
    }

    @Inject(method = "drop", at = @At(value = "HEAD"), cancellable = true)
    private void drop(ServerWorld world, DamageSource damageSource, CallbackInfo ci) {
        var livingEntity = (LivingEntity) (Object) this;
        boolean causedByPlayer = this.playerHitTimer > 0;

        var cmd = new LivingEntityDropCommand(livingEntity, causedByPlayer);
        CommandGateway.INSTANCE.apply(cmd);

        if (this.shouldDropLoot() && world.getGameRules().getBoolean(GameRules.DO_MOB_LOOT)) {
            if (cmd.getDropLoot()) this.dropLoot(damageSource, causedByPlayer);
            if (cmd.getDropEquipment()) this.dropEquipment(world, damageSource, causedByPlayer);
        }

        if (cmd.getDropInventory()) this.dropInventory();
        if (cmd.getDropExperience()) this.dropXp(damageSource.getAttacker());

        ci.cancel();
    }
}
