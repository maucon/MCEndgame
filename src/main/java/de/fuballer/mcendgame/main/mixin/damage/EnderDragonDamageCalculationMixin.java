package de.fuballer.mcendgame.main.mixin.damage;

import de.fuballer.mcendgame.main.component.damage.dealing.ExtendedDamageSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.enderdragon.EnderDragonPart;
import net.minecraft.world.entity.boss.enderdragon.phases.EnderDragonPhase;
import net.minecraft.world.entity.boss.enderdragon.phases.EnderDragonPhaseManager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnderDragon.class)
public abstract class EnderDragonDamageCalculationMixin extends LivingEntity {
    protected EnderDragonDamageCalculationMixin(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Shadow
    private float sittingDamageReceived;
    @Final
    @Shadow
    public EnderDragonPart head;
    @Final
    @Shadow
    private EnderDragonPhaseManager phaseManager;

    @Inject(at = @At("HEAD"), method = "hurt(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/boss/enderdragon/EnderDragonPart;Lnet/minecraft/world/damagesource/DamageSource;F)Z", cancellable = true)
    protected void damagePart(ServerLevel world, EnderDragonPart part, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (this.phaseManager.getCurrentPhase().getPhase() == EnderDragonPhase.DYING) {
            cir.setReturnValue(false);
        } else {
            amount = this.phaseManager.getCurrentPhase().onHurt(source, amount);

            //////////////////////////////////////////////////////////////////////////////////////
            boolean damageReduction = false;
            //////////////////////////////////////////////////////////////////////////////////////

            if (part != this.head) {
                amount = amount / 4.0F + Math.min(amount, 1.0F);

                //////////////////////////////////////////////////////////////////////////////////////
                damageReduction = true;
                //////////////////////////////////////////////////////////////////////////////////////
            }

            if (amount < 0.01F) {
                cir.setReturnValue(false);
            } else {
                if (source.getEntity() instanceof Player || source.is(DamageTypeTags.ALWAYS_HURTS_ENDER_DRAGONS)) {
                    float f = this.getHealth();
                    // parentDamage(world, source, amount);

                    //////////////////////////////////////////////////////////////////////////////////////
                    var extendedSource = source instanceof ExtendedDamageSource
                            ? (ExtendedDamageSource) source
                            : new ExtendedDamageSource(source);

                    extendedSource.getDamageCalculationConfig().enderDragonDamageReduction(damageReduction);
                    super.hurtServer(world, extendedSource, amount);
                    //////////////////////////////////////////////////////////////////////////////////////

                    if (this.isDeadOrDying() && !this.phaseManager.getCurrentPhase().isSitting()) {
                        this.setHealth(1.0F);
                        this.phaseManager.setPhase(EnderDragonPhase.DYING);
                    }

                    if (this.phaseManager.getCurrentPhase().isSitting()) {
                        this.sittingDamageReceived = this.sittingDamageReceived + f - this.getHealth();
                        if (this.sittingDamageReceived > 0.25F * this.getMaxHealth()) {
                            this.sittingDamageReceived = 0.0F;
                            this.phaseManager.setPhase(EnderDragonPhase.TAKEOFF);
                        }
                    }
                }

                cir.setReturnValue(true);
            }
        }
    }
}
