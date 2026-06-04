package de.fuballer.mcendgame.main.mixin.event;

import de.fuballer.mcendgame.main.messaging.misc.LivingEntityDeathEvent;
import de.fuballer.mcendgame.main.messaging.misc.LivingEntityDropCommand;
import de.maucon.mauconframework.command.CommandGateway;
import de.maucon.mauconframework.event.EventGateway;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.gamerules.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityEventsMixin {
    @Shadow
    protected int lastHurtByPlayerMemoryTime;

    @Shadow
    protected abstract void dropCustomDeathLoot(ServerLevel world, DamageSource damageSource, boolean bl);

    @Shadow
    protected abstract void dropExperience(ServerLevel world, Entity attacker);

    @Shadow
    protected abstract void dropEquipment(ServerLevel world);

    @Shadow
    protected abstract boolean shouldDropLoot(ServerLevel world);

    @Shadow
    protected abstract void dropFromLootTable(ServerLevel world, DamageSource damageSource, boolean bl);

    @Inject(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;tickDeath()V"))
    private void baseTick(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (entity.isDeadOrDying() && entity.deathTime == 0) {
            var event = new LivingEntityDeathEvent(entity);
            EventGateway.INSTANCE.publish(event);
        }
    }

    @Inject(method = "dropAllDeathLoot", at = @At(value = "HEAD"), cancellable = true)
    private void drop(ServerLevel world, DamageSource damageSource, CallbackInfo ci) {
        var livingEntity = (LivingEntity) (Object) this;
        boolean causedByPlayer = this.lastHurtByPlayerMemoryTime > 0;

        var cmd = new LivingEntityDropCommand(livingEntity, causedByPlayer);
        CommandGateway.INSTANCE.apply(cmd);

        if (this.shouldDropLoot(world) && world.getGameRules().get(GameRules.MOB_DROPS)) {
            if (cmd.getDropLoot()) this.dropFromLootTable(world, damageSource, causedByPlayer);
            if (cmd.getDropEquipment()) this.dropCustomDeathLoot(world, damageSource, causedByPlayer);
        }

        if (cmd.getDropInventory()) this.dropEquipment(world);
        if (cmd.getDropExperience()) this.dropExperience(world, damageSource.getEntity());

        ci.cancel();
    }
}
