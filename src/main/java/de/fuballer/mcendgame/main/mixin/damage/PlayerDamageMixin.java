package de.fuballer.mcendgame.main.mixin.damage;

import de.fuballer.mcendgame.main.component.damage.DifficultyScaling;
import de.fuballer.mcendgame.main.component.damage.new1.DamageService;
import de.fuballer.mcendgame.main.component.damage.new1.DamageSourceDraft;
import de.fuballer.mcendgame.main.mixin.access.PlayerAccessMixin;
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
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
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerDamageMixin extends LivingEntity {
    @Shadow
    public abstract Abilities getAbilities();

    protected PlayerDamageMixin(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Inject(at = @At("HEAD"), method = "hurtServer", cancellable = true)
    protected void hurtServer(ServerLevel level, DamageSource source, float damage, CallbackInfoReturnable<Boolean> cir) {
        var value = callHurtServer(level, source, damage);
        cir.setReturnValue(value);
    }

    @Unique
    protected boolean callHurtServer(ServerLevel level, DamageSource source, float damage) {
        Player this_ = (Player) (Object) this;

        if (this.isInvulnerableTo(level, source)) {
            return false;
        } else if (this_.getAbilities().invulnerable && !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return false;
        } else {
            this.noActionTime = 0;
            if (this.isDeadOrDying()) {
                return false;
            } else {
                ((PlayerAccessMixin) this).invokeRemoveEntitiesOnShoulder();

                //↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
                var difficultyScaling = DifficultyScaling.NONE;
                //↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

                if (source.scalesWithDifficulty()) {
                    if (level.getDifficulty() == Difficulty.PEACEFUL) {
                        damage = 0.0F;

                    }

                    if (level.getDifficulty() == Difficulty.EASY) {
                        damage = Math.min(damage / 2.0F + 1.0F, damage);
                        //↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
                        difficultyScaling = DifficultyScaling.EASY;
                        //↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
                    }

                    if (level.getDifficulty() == Difficulty.HARD) {
                        damage = damage * 3.0F / 2.0F;
                        //↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
                        difficultyScaling = DifficultyScaling.HARD;
                        //↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
                    }
                }

                //return damage == 0.0F ? false : super.hurtServer(level, source, damage);
                // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
                var draftSource = source instanceof DamageSourceDraft d ? d : new DamageSourceDraft(source);
                draftSource.getVanillaDamageContext().setDifficultyScaling(difficultyScaling);
                return super.hurtServer(level, draftSource, damage);
                // ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
            }
        }

    }

    @Inject(at = @At("HEAD"), method = "actuallyHurt", cancellable = true)
    protected void applyDamage(
            ServerLevel level,
            DamageSource source,
            float dmg,
            CallbackInfo ci
    ) {
        Player this_ = (Player) (Object) this;

        if (!this.isInvulnerableTo(level, source)) {
            //↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
            var damageReductionResult = DamageService.INSTANCE.calculateIncomingDamage(this_, source, dmg);
            float reducedTotal = damageReductionResult.getDamage();

            EntityMixinExtension.INSTANCE.setLastHitWasApplied(this_, true);

            var damageToApply = reducedTotal;
            if (EntityMixinExtension.INSTANCE.isInInvulnerabilityFrames(this_)) {
                boolean isStronger = reducedTotal > this.lastHurt;
                EntityMixinExtension.INSTANCE.setLastHitWasApplied(this_, isStronger);
                if (!isStronger) return;
                damageToApply = reducedTotal - this.lastHurt;
            }
            this.lastHurt = reducedTotal;
            dmg = damageToApply;

            // from LivingEntity.getDamageAfterArmorAbsorb
            if (!source.is(DamageTypeTags.BYPASSES_ARMOR)) {
                ((LivingEntity) this_).hurtArmor(source, dmg); // TODO only ATTACK_DAMAGE part?
            }

            var damageResisted = damageReductionResult.getResistedDamage(); // TODO why
            // from LivingEntity.getDamageAfterMagicAbsorb
            if (this_ instanceof ServerPlayer) {
                ((ServerPlayer) this_).awardStat(Stats.DAMAGE_RESISTED, Math.round(damageResisted * 10.0F));
            } else if (source.getEntity() instanceof ServerPlayer) {
                ((ServerPlayer) source.getEntity()).awardStat(Stats.DAMAGE_DEALT_RESISTED, Math.round(damageResisted * 10.0F));
            }
            //↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
            //dmg = this.getDamageAfterArmorAbsorb(source, dmg);
            //dmg = this.getDamageAfterMagicAbsorb(source, dmg);
            float originalDamage = dmg;
            dmg = Math.max(dmg - this.getAbsorptionAmount(), 0.0F);
            this.setAbsorptionAmount(this.getAbsorptionAmount() - (originalDamage - dmg));
            float absorbedDamage = originalDamage - dmg;
            if (absorbedDamage > 0.0F && absorbedDamage < 3.4028235E37F) {
                this_.awardStat(Stats.DAMAGE_ABSORBED, Math.round(absorbedDamage * 10.0F));
            }

            if (dmg != 0.0F) {
                this_.causeFoodExhaustion(source.getFoodExhaustion());
                this.getCombatTracker().recordDamage(source, dmg);
                this.setHealth(this.getHealth() - dmg);
                if (dmg < 3.4028235E37F) {
                    this_.awardStat(Stats.DAMAGE_TAKEN, Math.round(dmg * 10.0F));
                }

                this.gameEvent(GameEvent.ENTITY_DAMAGE);
            }
        }

        ci.cancel();
    }
}
