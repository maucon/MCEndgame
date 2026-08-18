package de.fuballer.mcendgame.main.mixin.damage;

import de.fuballer.mcendgame.main.component.damage.new1.DamageService;
import de.fuballer.mcendgame.main.component.damage.new1.DamageSourceDraft;
import de.fuballer.mcendgame.main.mixin.access.EntityAccessMixin;
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension;
import it.unimi.dsi.fastutil.doubles.DoubleDoubleImmutablePair;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.Stats;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BlocksAttacks;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityDamageMixin {
    private static final Logger log = LoggerFactory.getLogger(LivingEntityDamageMixin.class);
    @Unique
    private boolean lastHitWasApplied = true;

    @Shadow
    protected float lastHurt;
    @Shadow
    protected int noActionTime;
    @Shadow
    private long lastDamageStamp;
    @Shadow
    private @Nullable DamageSource lastDamageSource;

    @Shadow
    protected abstract void resolveMobResponsibleForDamage(DamageSource source);

    @Shadow
    protected abstract Player resolvePlayerResponsibleForDamage(DamageSource source);

    @Shadow
    protected abstract boolean checkTotemDeathProtection(DamageSource killingDamage);

    @Shadow
    protected abstract SoundEvent getDeathSound();

    @Shadow
    protected abstract void playSecondaryHurtSound(DamageSource source);

    @Shadow
    protected abstract void playHurtSound(DamageSource source);

    @Shadow
    protected abstract void actuallyHurt(ServerLevel level, DamageSource source, float dmg);

    @Inject(at = @At("HEAD"), method = "hurtServer", cancellable = true)
    protected void hurtServer(ServerLevel level, DamageSource source, float damage, CallbackInfoReturnable<Boolean> cir) {
        var value = callHurtServer(level, source, damage);
        cir.setReturnValue(value);
    }

    @Unique
    private boolean callHurtServer(ServerLevel level, DamageSource source, float damage) {
        LivingEntity this_ = (LivingEntity) (Object) this;

        //↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
        // Make source a DamageSourceDraft if not already
        var draftSource = source instanceof DamageSourceDraft d ? d : new DamageSourceDraft(source);
        //↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

        if (this_.isInvulnerableTo(level, source)) {
            return false;
        } else if (this_.isDeadOrDying()) {
            return false;
        } else if (source.is(DamageTypeTags.IS_FIRE) && this_.hasEffect(MobEffects.FIRE_RESISTANCE)) {
            return false;
        } else {
            if (this_.isSleeping()) {
                this_.stopSleeping();
            }

            this.noActionTime = 0;
            if (damage < 0.0F) {
                damage = 0.0F;
            }

            float originalDamage = damage;
            ItemStack itemInUse = this_.getUseItem();
            float damageBlocked = this_.applyItemBlocking(level, source, damage);
            damage -= damageBlocked;
            boolean blocked = damageBlocked > 0.0F;
            //↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
            draftSource.getVanillaDamageContext().setBlocked(blocked);
            //↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
            if (source.is(DamageTypeTags.IS_FREEZING) && this_.is(EntityTypeTags.FREEZE_HURTS_EXTRA_TYPES)) {
                damage *= 5.0F;
                //↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
                draftSource.getVanillaDamageContext().addVictimMoreDamageTaken(4);
                //↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
            }

            if (source.is(DamageTypeTags.DAMAGES_HELMET) && !this_.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
                this_.hurtHelmet(source, damage);
                damage *= 0.75F;
                //↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
                draftSource.getVanillaDamageContext().addVictimMoreDamageTaken(-0.25);
                //↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
            }

            if (Float.isNaN(damage) || Float.isInfinite(damage)) {
                damage = Float.MAX_VALUE;
            }

            //↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
            var finalSource = DamageService.INSTANCE.createDamageSourceResult(this_, level, draftSource, damage);
            source = finalSource;

            if (!finalSource.isDamageApplying()) {
                return false;
            }

            damage = finalSource.getRawDamage();

            boolean withinIFrames = (float) this_.invulnerableTime > 10.0F && !source.is(DamageTypeTags.BYPASSES_COOLDOWN);
            EntityMixinExtension.INSTANCE.setInInvulnerabilityFrames(this_, withinIFrames);

            this.actuallyHurt(level, finalSource, damage); // use DamageSourceResult

            if (withinIFrames && !EntityMixinExtension.INSTANCE.getLastHitWasApplied(this_)) {
                // hit was fully absorbed by the "bigger hit wins"
                return false;
            }
            boolean tookFullDamage = !withinIFrames;
            if (!withinIFrames) {
                this_.invulnerableTime = 20;
                this_.hurtDuration = 10;
                this_.hurtTime = this_.hurtDuration;
            }
            //↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

            //boolean tookFullDamage = true;
            //if ((float) this_.invulnerableTime > 10.0F && !source.is(DamageTypeTags.BYPASSES_COOLDOWN)) {
            //    if (damage <= this.lastHurt) {
            //        return false;
            //    }
            //
            //    this.actuallyHurt(level, source, damage - this.lastHurt);
            //    this.lastHurt = damage;
            //    tookFullDamage = false;
            //} else {
            //    this.lastHurt = damage;
            //    this_.invulnerableTime = 20;
            //    this.actuallyHurt(level, source, damage);
            //    this_.hurtDuration = 10;
            //    this_.hurtTime = this_.hurtDuration;
            //}

            this.resolveMobResponsibleForDamage(source);
            this.resolvePlayerResponsibleForDamage(source);
            if (tookFullDamage) {
                BlocksAttacks blocksAttacks = (BlocksAttacks) itemInUse.get(DataComponents.BLOCKS_ATTACKS);
                if (blocked && blocksAttacks != null) {
                    blocksAttacks.onBlocked(level, this_);
                } else {
                    level.broadcastDamageEvent(this_, source);
                }

                if (!source.is(DamageTypeTags.NO_IMPACT) && (!blocked || damage > 0.0F)) {
                    ((EntityAccessMixin) this_).invokeMarkHurt();
                }

                if (!source.is(DamageTypeTags.NO_KNOCKBACK)) {
                    double xd = (double) 0.0F;
                    double zd = (double) 0.0F;
                    Entity var15 = source.getDirectEntity();
                    if (var15 instanceof Projectile) {
                        Projectile projectile = (Projectile) var15;
                        DoubleDoubleImmutablePair knockbackDirection = projectile.calculateHorizontalHurtKnockbackDirection(this_, source);
                        xd = -knockbackDirection.leftDouble();
                        zd = -knockbackDirection.rightDouble();
                    } else if (source.getSourcePosition() != null) {
                        xd = source.getSourcePosition().x() - this_.getX();
                        zd = source.getSourcePosition().z() - this_.getZ();
                    }

                    this_.knockback((double) 0.4F, xd, zd);
                    if (!blocked) {
                        this_.indicateDamage(xd, zd);
                    }
                }
            }

            if (this_.isDeadOrDying()) {
                if (!this.checkTotemDeathProtection(source)) {
                    if (tookFullDamage) {
                        this_.makeSound(this.getDeathSound());
                        this.playSecondaryHurtSound(source);
                    }

                    this_.die(source);
                }
            } else if (tookFullDamage) {
                this.playHurtSound(source);
                this.playSecondaryHurtSound(source);
            }

            boolean success = !blocked || damage > 0.0F;
            if (success) {
                this.lastDamageSource = source;
                this.lastDamageStamp = this_.level().getGameTime();

                for (MobEffectInstance effect : this_.getActiveEffects()) {
                    effect.onMobHurt(level, this_, source, damage);
                }
            }

            if (this_ instanceof ServerPlayer) {
                ServerPlayer serverPlayer = (ServerPlayer) this_;
                CriteriaTriggers.ENTITY_HURT_PLAYER.trigger(serverPlayer, source, originalDamage, damage, blocked);
                if (damageBlocked > 0.0F && damageBlocked < 3.4028235E37F) {
                    serverPlayer.awardStat(Stats.DAMAGE_BLOCKED_BY_SHIELD, Math.round(damageBlocked * 10.0F));
                }
            }

            Entity var21 = source.getEntity();
            if (var21 instanceof ServerPlayer) {
                ServerPlayer sourcePlayer = (ServerPlayer) var21;
                CriteriaTriggers.PLAYER_HURT_ENTITY.trigger(sourcePlayer, this_, source, originalDamage, damage, blocked);
            }

            return success;
        }
    }

    @Inject(at = @At("HEAD"), method = "actuallyHurt", cancellable = true)
    protected void applyDamage(ServerLevel level, DamageSource source, float dmg, CallbackInfo ci) {
        callApplyDamage(level, source, dmg);
        ci.cancel();
    }

    @Unique
    protected void callApplyDamage(ServerLevel level, DamageSource source, float dmg) {
        LivingEntity this_ = (LivingEntity) (Object) this;

        if (!this_.isInvulnerableTo(level, source)) {
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

            System.out.println(reducedTotal);
            System.out.println(dmg + " : " + damageToApply);
            dmg = damageToApply;


            // from LivingEntity.getDamageAfterArmorAbsorb
            if (!source.is(DamageTypeTags.BYPASSES_ARMOR)) {
                this_.hurtArmor(source, dmg); // TODO only ATTACK_DAMAGE part?
            }

            var damageResisted = damageReductionResult.getResistedDamage(); // TODO why
            // from LivingEntity.getDamageAfterMagicAbsorb
            if (this_ instanceof ServerPlayer) {
                ((ServerPlayer) this_).awardStat(Stats.DAMAGE_RESISTED, Math.round(damageResisted * 10.0F));
            } else if (source.getEntity() instanceof ServerPlayer) {
                ((ServerPlayer) source.getEntity()).awardStat(Stats.DAMAGE_DEALT_RESISTED, Math.round(damageResisted * 10.0F));
            }
            //↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
            //dmg = this_.getDamageAfterArmorAbsorb(source, dmg);
            //dmg = this_.getDamageAfterMagicAbsorb(source, dmg);
            float originalDamage = dmg;
            dmg = Math.max(dmg - this_.getAbsorptionAmount(), 0.0F);
            this_.setAbsorptionAmount(this_.getAbsorptionAmount() - (originalDamage - dmg));
            float absorbedDamage = originalDamage - dmg;
            if (absorbedDamage > 0.0F && absorbedDamage < 3.4028235E37F) {
                Entity var7 = source.getEntity();
                if (var7 instanceof ServerPlayer) {
                    ServerPlayer serverPlayer = (ServerPlayer) var7;
                    serverPlayer.awardStat(Stats.DAMAGE_DEALT_ABSORBED, Math.round(absorbedDamage * 10.0F));
                }
            }

            if (dmg != 0.0F) {
                this_.getCombatTracker().recordDamage(source, dmg);
                this_.setHealth(this_.getHealth() - dmg);
                this_.setAbsorptionAmount(this_.getAbsorptionAmount() - dmg);
                this_.gameEvent(GameEvent.ENTITY_DAMAGE);
            }
        }
    }
}
