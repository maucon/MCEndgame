package de.fuballer.mcendgame.main.mixin.damage;

import de.fuballer.mcendgame.main.component.damage.DifficultyScaling;
import de.fuballer.mcendgame.main.component.damage.dealing.ExtendedDamageSource;
import de.fuballer.mcendgame.main.mixin.access.PlayerAccessMixin;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerDamageCalculationMixin extends LivingEntity {
    @Shadow
    public abstract Abilities getAbilities();

    protected PlayerDamageCalculationMixin(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Inject(at = @At("HEAD"), method = "hurtServer", cancellable = true)
    protected void damage(ServerLevel level, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (this.isInvulnerableTo(level, source)) {
            cir.setReturnValue(false);
            return;
        }
        if (this.getAbilities().invulnerable && !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            cir.setReturnValue(false);
            return;
        }

        this.noActionTime = 0;
        if (this.isDeadOrDying()) {
            cir.setReturnValue(false);
            return;
        }

        ((PlayerAccessMixin) this).invokeRemoveEntitiesOnShoulder();

        // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
        var difficultyScaling = DifficultyScaling.NONE;
        // ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

        if (source.scalesWithDifficulty()) {
            if (level.getDifficulty() == Difficulty.PEACEFUL) {
                cir.setReturnValue(false);
                return;
            }

            if (level.getDifficulty() == Difficulty.EASY) {
                amount = Math.min(amount / 2.0F + 1.0F, amount);
                // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
                difficultyScaling = DifficultyScaling.EASY;
                // ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
            }

            if (level.getDifficulty() == Difficulty.HARD) {
                amount = amount * 3.0F / 2.0F;
                // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
                difficultyScaling = DifficultyScaling.HARD;
                // ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
            }
        }

        // return dmg == 0.0F ? false : super.damage(world, source, dmg);
        // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
        var extendedDamageSource = source instanceof ExtendedDamageSource
                ? (ExtendedDamageSource) source
                : new ExtendedDamageSource(source);

        extendedDamageSource.getDamageCalculationConfig().difficultyScaling(difficultyScaling);
        cir.setReturnValue(super.hurtServer(level, extendedDamageSource, amount));
        // ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
    }

    /**
     * As we calculate all damage increases and also reductions and mitigations in the
     * LivingEntityDamageMixin#damage method we need to remove any kind of mitigation
     * of this method, except for absorption dmg.
     */
    @Inject(at = @At("HEAD"), method = "actuallyHurt", cancellable = true)
    protected void applyDamage(
            ServerLevel level,
            DamageSource source,
            float dmg,
            CallbackInfo ci
    ) {
        Player this_ = (Player) (Object) this;

        if (this.isInvulnerableTo(level, source)) {
            return;
        }

        if (!source.is(DamageTypeTags.BYPASSES_ARMOR)) {
            ((LivingEntity) this_).hurtArmor(source, dmg); // TODO should really be only attack damage part
        }

        // dmg = this.applyArmorToDamage(source, dmg);
        // float finalDamageAfterMitigation = dmg = this.modifyAppliedDamage(source, dmg);
        // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
        var finalDamageAfterMitigation = dmg;
        // ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

        dmg = Math.max(dmg - this.getAbsorptionAmount(), 0.0f);
        this.setAbsorptionAmount(this.getAbsorptionAmount() - (finalDamageAfterMitigation - dmg));
        float absorbedDamage = finalDamageAfterMitigation - dmg;
        if (absorbedDamage > 0.0f && absorbedDamage < 3.4028235E37f) {
            this_.awardStat(Stats.DAMAGE_ABSORBED, Math.round(absorbedDamage * 10.0f));
        }
        if (dmg == 0.0f) {
            return;
        }
        this_.causeFoodExhaustion(source.getFoodExhaustion());
        this.getCombatTracker().recordDamage(source, dmg);
        this.setHealth(this.getHealth() - dmg);
        if (dmg < 3.4028235E37f) {
            this_.awardStat(Stats.DAMAGE_TAKEN, Math.round(dmg * 10.0f));
        }
        this.gameEvent(GameEvent.ENTITY_DAMAGE);

        ci.cancel();
    }
}
