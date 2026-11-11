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

    // ---------------------------------------------------------------------------
    // region Death drop control
    //
    // dropAllDeathLoot() performs four vanilla drop calls. The drop command is
    // resolved once at HEAD (so listeners can set per-death flags), then each
    // call is wrapped and skipped when its flag is disabled.
    // ---------------------------------------------------------------------------

    /**
     * Resolves the drop flags once per death, before any drop call runs.
     * The resulting command is stored and read by the wrappers below.
     */
    @Inject(method = "dropAllDeathLoot", at = @At("HEAD"))
    private void mcendgame$prepareDropCommand(ServerLevel level, DamageSource source, CallbackInfo ci) {
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
    private void mcendgame$dropFromLootTable(LivingEntity self, ServerLevel level, DamageSource source, boolean playerKilled, Operation<Void> operation) {
        var cmd = this.mcendgame$dropCommand;
        if (cmd == null || cmd.getDropLoot()) {
            operation.call(self, level, source, playerKilled);
        }
    }

    @WrapOperation(
            method = "dropAllDeathLoot",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;dropCustomDeathLoot(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;Z)V"
            )
    )
    private void mcendgame$dropCustomDeathLoot(LivingEntity self, ServerLevel level, DamageSource source, boolean killedByPlayer, Operation<Void> operation) {
        var cmd = this.mcendgame$dropCommand;
        if (cmd == null || cmd.getDropEquipment()) {
            operation.call(self, level, source, killedByPlayer);
        }
    }

    @WrapOperation(
            method = "dropAllDeathLoot",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;dropEquipment(Lnet/minecraft/server/level/ServerLevel;)V"
            )
    )
    private void mcendgame$dropEquipment(LivingEntity self, ServerLevel level, Operation<Void> operation) {
        var cmd = this.mcendgame$dropCommand;
        if (cmd == null || cmd.getDropInventory()) {
            operation.call(self, level);
        }
    }

    @WrapOperation(
            method = "dropAllDeathLoot",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;dropExperience(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/Entity;)V"
            )
    )
    private void mcendgame$dropExperience(LivingEntity self, ServerLevel level, Entity killer, Operation<Void> operation) {
        var cmd = this.mcendgame$dropCommand;
        if (cmd == null || cmd.getDropExperience()) {
            operation.call(self, level, killer);
        }
    }
    // endregion
}
