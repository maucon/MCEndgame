package de.fuballer.mcendgame.main.mixin.damage;

import de.fuballer.mcendgame.main.component.damage.DifficultyScaling;
import de.fuballer.mcendgame.main.component.damage.dealing.ExtendedDamageSource;
import de.fuballer.mcendgame.main.mixin.access.PlayerEntityAccessMixin;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityDamageCalculationMixin extends LivingEntity {
    @Shadow
    public abstract PlayerAbilities getAbilities();

    protected PlayerEntityDamageCalculationMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(at = @At("HEAD"), method = "damage", cancellable = true)
    protected void damage(ServerWorld world, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (this.isInvulnerableTo(world, source)) {
            cir.setReturnValue(false);
            return;
        }
        if (this.getAbilities().invulnerable && !source.isIn(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            cir.setReturnValue(false);
            return;
        }

        this.despawnCounter = 0;
        if (this.isDead()) {
            cir.setReturnValue(false);
            return;
        }

        ((PlayerEntityAccessMixin) this).invokeDropShoulderEntities();

        ///////////////////////////////////////////////////////////////////////////////////
        var difficultyScaling = DifficultyScaling.NONE;
        ///////////////////////////////////////////////////////////////////////////////////

        if (source.isScaledWithDifficulty()) {
            if (world.getDifficulty() == Difficulty.PEACEFUL) {
                cir.setReturnValue(false);
                return;
            }

            if (world.getDifficulty() == Difficulty.EASY) {
                amount = Math.min(amount / 2.0F + 1.0F, amount);
                ///////////////////////////////////////////////////////////////////////////////////
                difficultyScaling = DifficultyScaling.EASY;
                ///////////////////////////////////////////////////////////////////////////////////
            }

            if (world.getDifficulty() == Difficulty.HARD) {
                amount = amount * 3.0F / 2.0F;
                ///////////////////////////////////////////////////////////////////////////////////
                difficultyScaling = DifficultyScaling.HARD;
                ///////////////////////////////////////////////////////////////////////////////////
            }
        }

        // return amount == 0.0F ? false : super.damage(world, source, amount);
        ///////////////////////////////////////////////////////////////////////////////////
        var extendedDamageSource = source instanceof ExtendedDamageSource
                ? (ExtendedDamageSource) source
                : new ExtendedDamageSource(source);

        extendedDamageSource.getDamageCalculationConfig().difficultyScaling(difficultyScaling);
        cir.setReturnValue(super.damage(world, extendedDamageSource, amount));
        ///////////////////////////////////////////////////////////////////////////////////
    }

    /**
     * As we calculate all damage increases and also reductions and mitigations in the
     * LivingEntityDamageMixin#damage method we need to remove any kind of mitigation
     * of this method, except for absorption amount.
     */
    @Inject(at = @At("HEAD"), method = "applyDamage", cancellable = true)
    protected void applyDamage(
            ServerWorld world,
            DamageSource source,
            float amount,
            CallbackInfo ci
    ) {
        PlayerEntity this_ = (PlayerEntity) (Object) this;

        if (this.isInvulnerableTo(world, source)) {
            return;
        }

        if (!source.isIn(DamageTypeTags.BYPASSES_ARMOR)) {
            ((LivingEntity) this_).damageArmor(source, amount); // TODO should really be only attack damage part
        }

        // amount = this.applyArmorToDamage(source, amount);
        // float finalDamageAfterMitigation = amount = this.modifyAppliedDamage(source, amount);
        ///////////////////////////////////////////////////////////////////////////////////
        var finalDamageAfterMitigation = amount;
        ///////////////////////////////////////////////////////////////////////////////////

        amount = Math.max(amount - this.getAbsorptionAmount(), 0.0f);
        this.setAbsorptionAmount(this.getAbsorptionAmount() - (finalDamageAfterMitigation - amount));
        float g = finalDamageAfterMitigation - amount;
        if (g > 0.0f && g < 3.4028235E37f) {
            this_.increaseStat(Stats.DAMAGE_ABSORBED, Math.round(g * 10.0f));
        }
        if (amount == 0.0f) {
            return;
        }
        this_.addExhaustion(source.getExhaustion());
        this.getDamageTracker().onDamage(source, amount);
        this.setHealth(this.getHealth() - amount);
        if (amount < 3.4028235E37f) {
            this_.increaseStat(Stats.DAMAGE_TAKEN, Math.round(amount * 10.0f));
        }
        this.emitGameEvent(GameEvent.ENTITY_DAMAGE);

        ci.cancel();
    }
}
