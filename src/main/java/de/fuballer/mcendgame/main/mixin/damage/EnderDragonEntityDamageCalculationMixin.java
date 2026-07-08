package de.fuballer.mcendgame.main.mixin.damage;

import de.fuballer.mcendgame.main.component.damage.dealing.ExtendedDamageSource;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.entity.boss.dragon.phase.PhaseManager;
import net.minecraft.entity.boss.dragon.phase.PhaseType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnderDragonEntity.class)
public abstract class EnderDragonEntityDamageCalculationMixin extends LivingEntity {
    protected EnderDragonEntityDamageCalculationMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Shadow
    private float damageDuringSitting;
    @Final
    @Shadow
    public EnderDragonPart head;
    @Final
    @Shadow
    private PhaseManager phaseManager;

    @Inject(at = @At("HEAD"), method = "damagePart", cancellable = true)
    protected void damagePart(EnderDragonPart part, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (this.phaseManager.getCurrent().getType() == PhaseType.DYING) {
            cir.setReturnValue(false);
        } else {
            amount = this.phaseManager.getCurrent().modifyDamageTaken(source, amount);

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
                if (source.getAttacker() instanceof PlayerEntity || source.isIn(DamageTypeTags.ALWAYS_HURTS_ENDER_DRAGONS)) {
                    float f = this.getHealth();
                    // parentDamage(world, source, amount);

                    //////////////////////////////////////////////////////////////////////////////////////
                    var extendedSource = source instanceof ExtendedDamageSource
                            ? (ExtendedDamageSource) source
                            : new ExtendedDamageSource(source);

                    extendedSource.getDamageCalculationConfig().enderDragonDamageReduction(damageReduction);
                    super.damage(extendedSource, amount);
                    //////////////////////////////////////////////////////////////////////////////////////

                    if (this.isDead() && !this.phaseManager.getCurrent().isSittingOrHovering()) {
                        this.setHealth(1.0F);
                        this.phaseManager.setPhase(PhaseType.DYING);
                    }

                    if (this.phaseManager.getCurrent().isSittingOrHovering()) {
                        this.damageDuringSitting = this.damageDuringSitting + f - this.getHealth();
                        if (this.damageDuringSitting > 0.25F * this.getMaxHealth()) {
                            this.damageDuringSitting = 0.0F;
                            this.phaseManager.setPhase(PhaseType.TAKEOFF);
                        }
                    }
                }

                cir.setReturnValue(true);
            }
        }
    }
}
