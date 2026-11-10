package de.fuballer.mcendgame.main.mixin.event;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import de.fuballer.mcendgame.main.messaging.misc.LivingEntityDeathEvent;
import de.fuballer.mcendgame.main.messaging.misc.LivingEntityDropCommand;
import de.maucon.mauconframework.command.CommandGateway;
import de.maucon.mauconframework.event.EventGateway;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
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
    protected abstract void dropFromLootTable(ServerLevel world, DamageSource damageSource, boolean bl);

    @Unique
    private LivingEntityDropCommand mcendgame$dropCommand;

    @Inject(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;tickDeath()V"))
    private void baseTick(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (entity.isDeadOrDying() && entity.deathTime == 0) {
            var event = new LivingEntityDeathEvent(entity);
            EventGateway.INSTANCE.publish(event);
        }
    }

    /**
     * Resolve the drop flags once per death. Unlike the previous implementation we do not cancel
     * {@code dropAllDeathLoot}; the vanilla body keeps running so TAIL injectors from other mods
     * (e.g. Pufferfish's Skills, which awards kill skill XP there) still execute. Only the four
     * vanilla drop calls are wrapped and skipped according to the command flags.
     */
    @Inject(method = "dropAllDeathLoot", at = @At("HEAD"))
    private void mcendgame$prepareDropCommand(ServerLevel world, DamageSource damageSource, CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        boolean causedByPlayer = this.lastHurtByPlayerMemoryTime > 0;

        var cmd = new LivingEntityDropCommand(entity, causedByPlayer);
        CommandGateway.INSTANCE.apply(cmd);

        this.mcendgame$dropCommand = cmd;
    }

    @WrapOperation(
            method = "dropAllDeathLoot",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;dropFromLootTable(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;Z)V"
            )
    )
    private void mcendgame$dropFromLootTable(LivingEntity self, ServerLevel world, DamageSource damageSource, boolean causedByPlayer, Operation<Void> operation) {
        var cmd = this.mcendgame$dropCommand;
        if (cmd == null || cmd.getDropLoot()) {
            operation.call(self, world, damageSource, causedByPlayer);
        }
    }

    @WrapOperation(
            method = "dropAllDeathLoot",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;dropCustomDeathLoot(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;Z)V"
            )
    )
    private void mcendgame$dropCustomDeathLoot(LivingEntity self, ServerLevel world, DamageSource damageSource, boolean causedByPlayer, Operation<Void> operation) {
        var cmd = this.mcendgame$dropCommand;
        if (cmd == null || cmd.getDropEquipment()) {
            operation.call(self, world, damageSource, causedByPlayer);
        }
    }

    @WrapOperation(
            method = "dropAllDeathLoot",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;dropEquipment(Lnet/minecraft/server/level/ServerLevel;)V"
            )
    )
    private void mcendgame$dropEquipment(LivingEntity self, ServerLevel world, Operation<Void> operation) {
        var cmd = this.mcendgame$dropCommand;
        if (cmd == null || cmd.getDropInventory()) {
            operation.call(self, world);
        }
    }

    @WrapOperation(
            method = "dropAllDeathLoot",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;dropExperience(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/Entity;)V"
            )
    )
    private void mcendgame$dropExperience(LivingEntity self, ServerLevel world, Entity attacker, Operation<Void> operation) {
        var cmd = this.mcendgame$dropCommand;
        if (cmd == null || cmd.getDropExperience()) {
            operation.call(self, world, attacker);
        }
    }
}
