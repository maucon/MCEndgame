package de.fuballer.mcendgame.main.mixin.damage;

import de.fuballer.mcendgame.main.component.damage.new1.DamageSourceDraft;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.armadillo.Armadillo;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Armadillo.class)
public abstract class ArmadilloDamageCalculationMixin extends LivingEntity {
    protected ArmadilloDamageCalculationMixin(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Shadow
    public abstract boolean isScared();

    @Inject(at = @At("HEAD"), method = "hurtServer", cancellable = true)
    protected void applyDamage(ServerLevel level, DamageSource source, float damage, CallbackInfoReturnable<Boolean> cir) {
        var draftSource = source instanceof DamageSourceDraft d ? d : new DamageSourceDraft(source);
        if (isScared()) {
            draftSource.getVanillaDamageContext().setCustomDamageReduction(
                    dmg -> (dmg - 1.0F) / 2.0F // Taken from Armadillo::hurtServer
            );
        }
        // skip damage reduction here, since we do it custom
        var returnValue = super.hurtServer(level, draftSource, damage);
        cir.setReturnValue(returnValue);
    }
}
