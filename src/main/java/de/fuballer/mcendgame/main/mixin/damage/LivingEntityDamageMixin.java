package de.fuballer.mcendgame.main.mixin.damage;

import de.fuballer.mcendgame.main.component.damage.DamageService;
import de.fuballer.mcendgame.main.component.damage.dealing.ExtendedDamageSource;
import de.fuballer.mcendgame.main.messaging.misc.LivingEntityDamagedEvent;
import de.fuballer.mcendgame.main.mixin.access.EntityAccessMixin;
import de.maucon.mauconframework.event.EventGateway;
import it.unimi.dsi.fastutil.doubles.DoubleDoubleImmutablePair;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlocksAttacksComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.WitchEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.stat.Stats;
import net.minecraft.world.event.GameEvent;
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
    protected float lastDamageTaken;
    @Shadow
    protected int despawnCounter;
    @Shadow
    private long lastDamageTime;
    @Shadow
    private @Nullable DamageSource lastDamageSource;

    @Shadow
    protected abstract void becomeAngry(DamageSource damageSource);

    @Shadow
    protected abstract PlayerEntity setAttackingPlayer(DamageSource damageSource);

    @Shadow
    protected abstract boolean tryUseDeathProtector(DamageSource source);

    @Shadow
    protected abstract SoundEvent getDeathSound();

    @Shadow
    protected abstract void playThornsSound(DamageSource damageSource);

    @Shadow
    protected abstract void playHurtSound(DamageSource damageSource);

    @Shadow
    protected abstract void applyDamage(ServerWorld world, DamageSource source, float amount);

    @Inject(at = @At("HEAD"), method = "damage", cancellable = true)
    protected void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        var value = callDamage(world, source, amount);
        cir.setReturnValue(value);
    }

    @Unique
    private boolean callDamage(ServerWorld world, DamageSource source, float amount) {
        LivingEntity this_ = (LivingEntity) (Object) this;

        var vanillaMoreDamage = new LinkedList<Double>();
        var vanillaMoreDamageTaken = new LinkedList<Double>();

        Entity entity;
        boolean bl3;
        boolean bl;
        if (this_.isInvulnerableTo(source)) {
            return false;
        }
        if (this_.isDead()) {
            return false;
        }
        if (source.isIn(DamageTypeTags.IS_FIRE) && this_.hasStatusEffect(StatusEffects.FIRE_RESISTANCE)) {
            return false;
        }
        if (this_.isSleeping()) {
            this_.wakeUp();
        }
        this.despawnCounter = 0;
        if (amount < 0.0f) {
            amount = 0.0f;
        }
        float f = amount;

        float blockedAmount = this_.getDamageBlockedAmount(source, amount);
        amount -= blockedAmount;
        boolean bl2 = bl = blockedAmount > 0.0f;
        ///////////////////////////////////////////////////////////////////////////////////
        var shieldBlocked = bl;
        ///////////////////////////////////////////////////////////////////////////////////

        if (source.isIn(DamageTypeTags.IS_FREEZING) && this_.getType().isIn(EntityTypeTags.FREEZE_HURTS_EXTRA_TYPES)) {
            amount *= 5.0f;
            ///////////////////////////////////////////////////////////////////////////////////
            vanillaMoreDamage.add(5.0);
            ///////////////////////////////////////////////////////////////////////////////////
        }
        if (source.isIn(DamageTypeTags.DAMAGES_HELMET) && !this_.getEquippedStack(EquipmentSlot.HEAD).isEmpty()) {
            this_.damageHelmet(source, amount);
            amount *= 0.75f;
            ///////////////////////////////////////////////////////////////////////////////////
            vanillaMoreDamage.add(0.75);
            ///////////////////////////////////////////////////////////////////////////////////
        }
        ///////////////////////////////////////////////////////////////////////////////////
        if (this_ instanceof WitchEntity) { // from WitchEntity::modifyAppliedDamage
            if (source.getAttacker() == this_) {
                vanillaMoreDamageTaken.add(-1d);
            } else if (source.isIn(DamageTypeTags.WITCH_RESISTANT_TO)) {
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
        if ((float) this_.timeUntilRegen > 10.0f && !source.isIn(DamageTypeTags.BYPASSES_COOLDOWN)) {
            if (amount <= this.lastDamageTaken) {
                return false;
            }
            this.applyDamage(world, source, amount - this.lastDamageTaken);
            this.lastDamageTaken = amount;
            bl22 = false;
        } else {
            this.lastDamageTaken = amount;
            this_.timeUntilRegen = 20;
            this.applyDamage(world, source, amount);
            this_.hurtTime = this_.maxHurtTime = 10;
        }

        this.becomeAngry(source);
        this.setAttackingPlayer(source);

        if (bl22) {
            BlocksAttacksComponent blocksAttacksComponent = this_.getActiveItem().get(DataComponentTypes.BLOCKS_ATTACKS);
            if (bl && blocksAttacksComponent != null) {
                blocksAttacksComponent.playBlockSound(world, this_);
            } else {
                world.sendEntityDamage(this_, source);
            }
            if (!(source.isIn(DamageTypeTags.NO_IMPACT) || bl && !(amount > 0.0f))) {
                ((EntityAccessMixin) this_).invokeScheduleVelocityUpdate();
            }
            if (!source.isIn(DamageTypeTags.NO_KNOCKBACK)) {
                double d = 0.0;
                double e = 0.0;
                Entity entity2 = source.getSource();
                if (entity2 instanceof ProjectileEntity projectileEntity) {
                    DoubleDoubleImmutablePair doubleDoubleImmutablePair = projectileEntity.getKnockback(this_, source);
                    d = -doubleDoubleImmutablePair.leftDouble();
                    e = -doubleDoubleImmutablePair.rightDouble();
                } else if (source.getPosition() != null) {
                    d = source.getPosition().getX() - this_.getX();
                    e = source.getPosition().getZ() - this_.getZ();
                }
                this_.takeKnockback(0.4f, d, e);
                if (!bl) {
                    this_.tiltScreen(d, e);
                }
            }
        }
        if (this_.isDead()) {
            if (!this.tryUseDeathProtector(source)) {
                if (bl22) {
                    this_.playSound(this.getDeathSound());
                    this.playThornsSound(source);
                }
                this_.onDeath(source);
            }
        } else if (bl22) {
            this.playHurtSound(source);
            this.playThornsSound(source);
        }
        boolean bl4 = bl3 = !bl || amount > 0.0f;
        if (bl3) {
            this.lastDamageSource = source;
            this.lastDamageTime = this_.getEntityWorld().getTime();
            for (StatusEffectInstance statusEffectInstance : this_.getStatusEffects()) {
                statusEffectInstance.onEntityDamage(this_, source, amount);
            }
        }
        if ((entity = this_) instanceof ServerPlayerEntity) {
            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) entity;
            Criteria.ENTITY_HURT_PLAYER.trigger(serverPlayerEntity, source, f, amount, bl);
            if (blockedAmount > 0.0f && blockedAmount < 3.4028235E37f) {
                serverPlayerEntity.increaseStat(Stats.DAMAGE_BLOCKED_BY_SHIELD, Math.round(blockedAmount * 10.0f));
            }
        }
        if ((entity = source.getAttacker()) instanceof ServerPlayerEntity) {
            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) entity;
            Criteria.PLAYER_HURT_ENTITY.trigger(serverPlayerEntity, this_, source, f, amount, bl);
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
    @Inject(at = @At("HEAD"), method = "applyDamage", cancellable = true)
    protected void applyDamage(
            DamageSource source,
            float amount,
            CallbackInfo ci
    ) {
        LivingEntity this_ = (LivingEntity) (Object) this;

        // region original
        Entity entity;
        if (this_.isInvulnerableTo(source)) {
            return;
        }

        if (!source.isIn(DamageTypeTags.BYPASSES_ARMOR)) {
            this_.damageArmor(source, amount); // TODO should really be only attack damage part
        }

        // amount = this.applyArmorToDamage(source, amount);
        // float finalDamageAfterMitigation = amount = this.modifyAppliedDamage(source, amount);
        ///////////////////////////////////////////////////////////////////////////////////
        var finalDamageAfterMitigation = amount;
        ///////////////////////////////////////////////////////////////////////////////////

        amount = Math.max(amount - this_.getAbsorptionAmount(), 0.0f);
        this_.setAbsorptionAmount(this_.getAbsorptionAmount() - (finalDamageAfterMitigation - amount));
        float healthDamage = finalDamageAfterMitigation - amount;
        if (healthDamage > 0.0f && healthDamage < 3.4028235E37f && (entity = source.getAttacker()) instanceof ServerPlayerEntity) {
            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) entity;
            serverPlayerEntity.increaseStat(Stats.DAMAGE_DEALT_ABSORBED, Math.round(healthDamage * 10.0f));
        }
        if (amount == 0.0f) {
            return;
        }
        this_.getDamageTracker().onDamage(source, amount);
        this_.setHealth(this_.getHealth() - amount);
        this_.setAbsorptionAmount(this_.getAbsorptionAmount() - amount);
        this_.emitGameEvent(GameEvent.ENTITY_DAMAGE);
        // endregion

        ci.cancel();
    }
}
