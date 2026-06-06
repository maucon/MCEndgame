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
import net.minecraft.world.item.ItemStack;
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

        // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
        var vanillaMoreDamage = new LinkedList<Double>();
        var vanillaMoreDamageTaken = new LinkedList<Double>();
        // ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

        Entity entity;
        boolean success;
        boolean blocked;
        if (this_.isInvulnerableTo(level, source)) {
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
        if (damage < 0.0f) {
            damage = 0.0f;
        }
        float originalDamage = damage;
        ItemStack itemInUse = this_.getUseItem();
        float damageBlocked = this_.applyItemBlocking(level, source, damage);
        damage -= damageBlocked;
        boolean bl = blocked = damageBlocked > 0.0f;
        // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
        var shieldBlocked = blocked;
        // ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

        if (source.is(DamageTypeTags.IS_FREEZING) && this_.is(EntityTypeTags.FREEZE_HURTS_EXTRA_TYPES)) {
            damage *= 5.0f;
            // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
            vanillaMoreDamage.add(5.0);
            // ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
        }
        if (source.is(DamageTypeTags.DAMAGES_HELMET) && !this_.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
            this_.hurtHelmet(source, damage);
            damage *= 0.75f;
            // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
            vanillaMoreDamage.add(0.75);
            // ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
        }
        // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
        if (this_ instanceof Witch) { // from WitchEntity::modifyAppliedDamage
            if (source.getEntity() == this_) {
                vanillaMoreDamageTaken.add(-1d);
            } else if (source.is(DamageTypeTags.WITCH_RESISTANT_TO)) {
                vanillaMoreDamageTaken.add(-0.85);
            }
        }
        // ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
        if (Float.isNaN(damage) || Float.isInfinite(damage)) {
            damage = Float.MAX_VALUE;
        }

        // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
        var extendedSource = source instanceof ExtendedDamageSource
                ? (ExtendedDamageSource) source
                : new ExtendedDamageSource(source);

        var damageCalculationConfig = extendedSource.getDamageCalculationConfig();

        damageCalculationConfig.getVanillaMoreDamage().addAll(vanillaMoreDamage);
        damageCalculationConfig.getVanillaMoreDamageTaken().addAll(vanillaMoreDamageTaken);
        damageCalculationConfig.setShieldBlocked(shieldBlocked);

        var result = DamageService.INSTANCE.calculateFinalDamage(this_, level, extendedSource, damage);
        if (!result.isApplying()) {
            return false;
        }
        damage = result.getAmount();
        // ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

        boolean tookFullDamage = true;
        if ((float) this_.invulnerableTime > 10.0f && !source.is(DamageTypeTags.BYPASSES_COOLDOWN)) {
            if (damage <= this.lastHurt) {
                return false;
            }
            this.actuallyHurt(level, source, damage - this.lastHurt);
            this.lastHurt = damage;
            tookFullDamage = false;
        } else {
            this.lastHurt = damage;
            this_.invulnerableTime = 20;
            this.actuallyHurt(level, source, damage);
            this_.hurtTime = this_.hurtDuration = 10;
        }

        this.resolveMobResponsibleForDamage(source);
        this.resolvePlayerResponsibleForDamage(source);

        if (tookFullDamage) {
            BlocksAttacks blocksAttacks = itemInUse.get(DataComponents.BLOCKS_ATTACKS);
            if (blocked && blocksAttacks != null) {
                blocksAttacks.onBlocked(level, this_);
            } else {
                level.broadcastDamageEvent(this_, source);
            }
            if (!(source.is(DamageTypeTags.NO_IMPACT) || blocked && !(damage > 0.0f))) {
                ((EntityAccessMixin) this_).invokeMarkHurt();
            }
            if (!source.is(DamageTypeTags.NO_KNOCKBACK)) {
                double xd = 0.0;
                double zd = 0.0;
                Entity entity2 = source.getDirectEntity();
                if (entity2 instanceof Projectile projectileEntity) {
                    DoubleDoubleImmutablePair knockbackDirection = projectileEntity.calculateHorizontalHurtKnockbackDirection(this_, source);
                    xd = -knockbackDirection.leftDouble();
                    zd = -knockbackDirection.rightDouble();
                } else if (source.getSourcePosition() != null) {
                    xd = source.getSourcePosition().x() - this_.getX();
                    zd = source.getSourcePosition().z() - this_.getZ();
                }
                this_.knockback(0.4f, xd, zd);
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
        boolean bl2 = success = !blocked || damage > 0.0f;
        if (success) {
            this.lastDamageSource = source;
            this.lastDamageStamp = this_.level().getGameTime();
            for (MobEffectInstance effect : this_.getActiveEffects()) {
                effect.onMobHurt(level, this_, source, damage);
            }
        }
        if ((entity = this_) instanceof ServerPlayer) {
            ServerPlayer serverPlayerEntity = (ServerPlayer) entity;
            CriteriaTriggers.ENTITY_HURT_PLAYER.trigger(serverPlayerEntity, source, originalDamage, damage, blocked);
            if (damageBlocked > 0.0f && damageBlocked < 3.4028235E37f) {
                serverPlayerEntity.awardStat(Stats.DAMAGE_BLOCKED_BY_SHIELD, Math.round(damageBlocked * 10.0f));
            }
        }
        if ((entity = source.getEntity()) instanceof ServerPlayer) {
            ServerPlayer sourcePlayer = (ServerPlayer) entity;
            CriteriaTriggers.PLAYER_HURT_ENTITY.trigger(sourcePlayer, this_, source, originalDamage, damage, blocked);
        }

        // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
        var event = new LivingEntityDamagedEvent(this_, extendedSource, damage);
        EventGateway.INSTANCE.publish(event);
        // ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

        return success;
    }

    /**
     * As we calculate all damage increases and also reductions and mitigations in the *damage* method
     * we need to remove any kind of mitigation of this method, except for absorption dmg.
     */
    @Inject(at = @At("HEAD"), method = "actuallyHurt", cancellable = true)
    protected void applyDamage(
            ServerLevel level,
            DamageSource source,
            float dmg,
            CallbackInfo ci
    ) {
        LivingEntity this_ = (LivingEntity) (Object) this;

        Entity entity;
        if (this_.isInvulnerableTo(level, source)) {
            return;
        }

        // dmg = this.applyArmorToDamage(source, dmg);
        // float originalDamage = dmg = this.modifyAppliedDamage(source, dmg);
        // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
        var finalDamageAfterMitigation = dmg;
        // ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

        dmg = Math.max(dmg - this_.getAbsorptionAmount(), 0.0f);
        this_.setAbsorptionAmount(this_.getAbsorptionAmount() - (finalDamageAfterMitigation - dmg));
        float absorbedDamage = finalDamageAfterMitigation - dmg;
        if (absorbedDamage > 0.0f && absorbedDamage < 3.4028235E37f && (entity = source.getEntity()) instanceof ServerPlayer) {
            ServerPlayer serverPlayerEntity = (ServerPlayer) entity;
            serverPlayerEntity.awardStat(Stats.DAMAGE_DEALT_ABSORBED, Math.round(absorbedDamage * 10.0f));
        }
        if (dmg == 0.0f) {
            return;
        }
        this_.getCombatTracker().recordDamage(source, dmg);
        this_.setHealth(this_.getHealth() - dmg);
        this_.setAbsorptionAmount(this_.getAbsorptionAmount() - dmg);
        this_.gameEvent(GameEvent.ENTITY_DAMAGE);

        ci.cancel();
    }
}
