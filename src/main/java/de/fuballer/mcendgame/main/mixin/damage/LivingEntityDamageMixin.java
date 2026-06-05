package de.fuballer.mcendgame.main.mixin.damage;

import de.fuballer.mcendgame.main.component.damage.DamageService;
import de.fuballer.mcendgame.main.component.damage.dealing.ExtendedDamageSource;
import de.fuballer.mcendgame.main.messaging.misc.LivingEntityDamagedEvent;
import de.fuballer.mcendgame.main.mixin.access.EntityAccessMixin;
import de.maucon.mauconframework.event.EventGateway;
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
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.component.BlocksAttacks;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.LinkedList;

@Mixin(LivingEntity.class)
public abstract class LivingEntityDamageMixin {
    @Shadow
    protected float lastHurt;
    @Shadow
    protected int noActionTime;
    @Shadow
    private long lastDamageStamp;
    @Shadow
    private @Nullable DamageSource lastDamageSource;

    @Shadow
    protected abstract void resolveMobResponsibleForDamage(DamageSource damageSource);

    @Shadow
    protected abstract Player resolvePlayerResponsibleForDamage(DamageSource damageSource);

    @Shadow
    protected abstract boolean checkTotemDeathProtection(DamageSource source);

    @Shadow
    protected abstract SoundEvent getDeathSound();

    @Shadow
    protected abstract void playSecondaryHurtSound(DamageSource damageSource);

    @Shadow
    protected abstract void playHurtSound(DamageSource damageSource);

    @Shadow
    protected abstract void actuallyHurt(ServerLevel world, DamageSource source, float amount);

    @Inject(at = @At("HEAD"), method = "hurtServer", cancellable = true)
    protected void damage(ServerLevel world, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        var value = callDamage(world, source, amount);
        cir.setReturnValue(value);
    }

    @Unique
    private boolean callDamage(ServerLevel world, DamageSource source, float amount) {
        LivingEntity this_ = (LivingEntity) (Object) this;

        var vanillaMoreDamage = new LinkedList<Double>();
        var vanillaMoreDamageTaken = new LinkedList<Double>();

        Entity entity;
        boolean bl3;
        boolean bl;
        if (this_.isInvulnerableTo(world, source)) {
            return false;
        }
        if (this_.isDeadOrDying()) {
            return false;
        }
        if (source.is(DamageTypeTags.IS_FIRE) && this_.hasEffect(MobEffects.FIRE_RESISTANCE)) {
            return false;
        }
        if (this_.isSleeping()) {
            this_.stopSleeping();
        }
        this.noActionTime = 0;
        if (amount < 0.0f) {
            amount = 0.0f;
        }
        float f = amount;

        float blockedAmount = this_.applyItemBlocking(world, source, amount);
        amount -= blockedAmount;
        boolean bl2 = bl = blockedAmount > 0.0f;
        ///////////////////////////////////////////////////////////////////////////////////
        var shieldBlocked = bl;
        ///////////////////////////////////////////////////////////////////////////////////

        if (source.is(DamageTypeTags.IS_FREEZING) && this_.is(EntityTypeTags.FREEZE_HURTS_EXTRA_TYPES)) {
            amount *= 5.0f;
            ///////////////////////////////////////////////////////////////////////////////////
            vanillaMoreDamage.add(5.0);
            ///////////////////////////////////////////////////////////////////////////////////
        }
        if (source.is(DamageTypeTags.DAMAGES_HELMET) && !this_.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
            this_.hurtHelmet(source, amount);
            amount *= 0.75f;
            ///////////////////////////////////////////////////////////////////////////////////
            vanillaMoreDamage.add(0.75);
            ///////////////////////////////////////////////////////////////////////////////////
        }
        ///////////////////////////////////////////////////////////////////////////////////
        if (this_ instanceof Witch) { // from WitchEntity::modifyAppliedDamage
            if (source.getEntity() == this_) {
                vanillaMoreDamageTaken.add(-1d);
            } else if (source.is(DamageTypeTags.WITCH_RESISTANT_TO)) {
                vanillaMoreDamageTaken.add(-0.85);
            }
        }
        ///////////////////////////////////////////////////////////////////////////////////
        if (Float.isNaN(amount) || Float.isInfinite(amount)) {
            amount = Float.MAX_VALUE;
        }

        ///////////////////////////////////////////////////////////////////////////////////
        var extendedSource = source instanceof ExtendedDamageSource
                ? (ExtendedDamageSource) source
                : new ExtendedDamageSource(source);

        var damageCalculationConfig = extendedSource.getDamageCalculationConfig();

        damageCalculationConfig.getVanillaMoreDamage().addAll(vanillaMoreDamage);
        damageCalculationConfig.getVanillaMoreDamageTaken().addAll(vanillaMoreDamageTaken);
        damageCalculationConfig.setShieldBlocked(shieldBlocked);

        var result = DamageService.INSTANCE.calculateFinalDamage(this_, world, extendedSource, amount);
        if (!result.isApplying()) {
            return false;
        }
        amount = result.getAmount();
        ///////////////////////////////////////////////////////////////////////////////////

        boolean bl22 = true;
        if ((float) this_.invulnerableTime > 10.0f && !source.is(DamageTypeTags.BYPASSES_COOLDOWN)) {
            if (amount <= this.lastHurt) {
                return false;
            }
            this.actuallyHurt(world, source, amount - this.lastHurt);
            this.lastHurt = amount;
            bl22 = false;
        } else {
            this.lastHurt = amount;
            this_.invulnerableTime = 20;
            this.actuallyHurt(world, source, amount);
            this_.hurtTime = this_.hurtDuration = 10;
        }

        this.resolveMobResponsibleForDamage(source);
        this.resolvePlayerResponsibleForDamage(source);

        if (bl22) {
            BlocksAttacks blocksAttacksComponent = this_.getUseItem().get(DataComponents.BLOCKS_ATTACKS);
            if (bl && blocksAttacksComponent != null) {
                blocksAttacksComponent.onBlocked(world, this_);
            } else {
                world.broadcastDamageEvent(this_, source);
            }
            if (!(source.is(DamageTypeTags.NO_IMPACT) || bl && !(amount > 0.0f))) {
                ((EntityAccessMixin) this_).invokeMarkHurt();
            }
            if (!source.is(DamageTypeTags.NO_KNOCKBACK)) {
                double d = 0.0;
                double e = 0.0;
                Entity entity2 = source.getDirectEntity();
                if (entity2 instanceof Projectile projectileEntity) {
                    DoubleDoubleImmutablePair doubleDoubleImmutablePair = projectileEntity.calculateHorizontalHurtKnockbackDirection(this_, source);
                    d = -doubleDoubleImmutablePair.leftDouble();
                    e = -doubleDoubleImmutablePair.rightDouble();
                } else if (source.getSourcePosition() != null) {
                    d = source.getSourcePosition().x() - this_.getX();
                    e = source.getSourcePosition().z() - this_.getZ();
                }
                this_.knockback(0.4f, d, e);
                if (!bl) {
                    this_.indicateDamage(d, e);
                }
            }
        }
        if (this_.isDeadOrDying()) {
            if (!this.checkTotemDeathProtection(source)) {
                if (bl22) {
                    this_.makeSound(this.getDeathSound());
                    this.playSecondaryHurtSound(source);
                }
                this_.die(source);
            }
        } else if (bl22) {
            this.playHurtSound(source);
            this.playSecondaryHurtSound(source);
        }
        boolean bl4 = bl3 = !bl || amount > 0.0f;
        if (bl3) {
            this.lastDamageSource = source;
            this.lastDamageStamp = this_.level().getGameTime();
            for (MobEffectInstance statusEffectInstance : this_.getActiveEffects()) {
                statusEffectInstance.onMobHurt(world, this_, source, amount);
            }
        }
        if ((entity = this_) instanceof ServerPlayer) {
            ServerPlayer serverPlayerEntity = (ServerPlayer) entity;
            CriteriaTriggers.ENTITY_HURT_PLAYER.trigger(serverPlayerEntity, source, f, amount, bl);
            if (blockedAmount > 0.0f && blockedAmount < 3.4028235E37f) {
                serverPlayerEntity.awardStat(Stats.DAMAGE_BLOCKED_BY_SHIELD, Math.round(blockedAmount * 10.0f));
            }
        }
        if ((entity = source.getEntity()) instanceof ServerPlayer) {
            ServerPlayer serverPlayerEntity = (ServerPlayer) entity;
            CriteriaTriggers.PLAYER_HURT_ENTITY.trigger(serverPlayerEntity, this_, source, f, amount, bl);
        }

        ///////////////////////////////////////////////////////////////////////////////////
        var event = new LivingEntityDamagedEvent(this_, extendedSource, amount);
        EventGateway.INSTANCE.publish(event);
        ///////////////////////////////////////////////////////////////////////////////////

        return bl3;
    }

    /**
     * As we calculate all damage increases and also reductions and mitigations in the *damage* method
     * we need to remove any kind of mitigation of this method, except for absorption amount.
     */
    @Inject(at = @At("HEAD"), method = "actuallyHurt", cancellable = true)
    protected void applyDamage(
            ServerLevel world,
            DamageSource source,
            float amount,
            CallbackInfo ci
    ) {
        LivingEntity this_ = (LivingEntity) (Object) this;

        // region original
        Entity entity;
        if (this_.isInvulnerableTo(world, source)) {
            return;
        }

        // amount = this.applyArmorToDamage(source, amount);
        // float finalDamageAfterMitigation = amount = this.modifyAppliedDamage(source, amount);
        ///////////////////////////////////////////////////////////////////////////////////
        var finalDamageAfterMitigation = amount;
        ///////////////////////////////////////////////////////////////////////////////////

        amount = Math.max(amount - this_.getAbsorptionAmount(), 0.0f);
        this_.setAbsorptionAmount(this_.getAbsorptionAmount() - (finalDamageAfterMitigation - amount));
        float healthDamage = finalDamageAfterMitigation - amount;
        if (healthDamage > 0.0f && healthDamage < 3.4028235E37f && (entity = source.getEntity()) instanceof ServerPlayer) {
            ServerPlayer serverPlayerEntity = (ServerPlayer) entity;
            serverPlayerEntity.awardStat(Stats.DAMAGE_DEALT_ABSORBED, Math.round(healthDamage * 10.0f));
        }
        if (amount == 0.0f) {
            return;
        }
        this_.getCombatTracker().recordDamage(source, amount);
        this_.setHealth(this_.getHealth() - amount);
        this_.setAbsorptionAmount(this_.getAbsorptionAmount() - amount);
        this_.gameEvent(GameEvent.ENTITY_DAMAGE);
        // endregion

        ci.cancel();
    }
}
