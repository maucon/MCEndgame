package de.fuballer.mcendgame.main.mixin.living_entity;

import de.fuballer.mcendgame.main.accessor.LivingEntityAuraAccessor;
import de.fuballer.mcendgame.main.component.custom_attribute.effects.data.AuraStatusEffect;
import de.fuballer.mcendgame.main.util.extension.EntityExtension;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

@Mixin(LivingEntity.class)
public class LivingEntityAuraMixin implements LivingEntityAuraAccessor {
    @Unique
    private static final String ALLY_AURA_STATUS_EFFECTS_NBT = "allyAuraStatusEffects";
    @Unique
    private static final String ENEMY_AURA_STATUS_EFFECTS_NBT = "enemyAuraStatusEffects";

    @Unique
    private final HashMap<Holder<MobEffect>, AuraStatusEffect> allyAuraStatusEffects = new HashMap<>();

    @Unique
    private final HashMap<Holder<MobEffect>, AuraStatusEffect> enemyAuraStatusEffects = new HashMap<>();

    @Override
    public void mcendgame$addAllyAuraStatusEffect(AuraStatusEffect effect) {
        var type = effect.getType();
        var amplifier = effect.getAmplifier();
        if (allyAuraStatusEffects.containsKey(type) && allyAuraStatusEffects.get(type).getAmplifier() >= amplifier) return;
        allyAuraStatusEffects.put(type, effect);
    }

    @Override
    public void mcendgame$addEnemyAuraStatusEffect(AuraStatusEffect effect) {
        var type = effect.getType();
        var amplifier = effect.getAmplifier();
        if (enemyAuraStatusEffects.containsKey(type) && enemyAuraStatusEffects.get(type).getAmplifier() >= amplifier) return;
        enemyAuraStatusEffects.put(type, effect);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    void tick(CallbackInfo ci) {
        var entity = (LivingEntity) (Object) this;
        var world = entity.level();
        if (world.isClientSide()) return;
        if (entity.tickCount % 10 != 0) return;

        applyAuraStatusEffects(entity, world, allyAuraStatusEffects, true);
        applyAuraStatusEffects(entity, world, enemyAuraStatusEffects, false);
    }

    @Unique
    void applyAuraStatusEffects(
            LivingEntity entity,
            Level world,
            HashMap<Holder<MobEffect>, AuraStatusEffect> effects,
            boolean ally
    ) {
        if (effects.isEmpty()) return;

        var rangeSortedEffects = effects.values().stream().sorted(Comparator.comparingInt(AuraStatusEffect::getRange)).toList();
        var prevRange = -1;
        var affectedEntities = new ArrayList<LivingEntity>();

        for (AuraStatusEffect effect : rangeSortedEffects) {
            var range = effect.getRange();
            if (range != prevRange) {
                prevRange = range;

                affectedEntities = (ArrayList<LivingEntity>) world.getEntities(
                        EntityTypeTest.forClass(LivingEntity.class),
                        entity.getBoundingBox().inflate(range),
                        nearbyEntity ->
                                ally ? EntityExtension.INSTANCE.isAlly(entity, nearbyEntity)
                                        : EntityExtension.INSTANCE.isEnemy(entity, nearbyEntity)
                );
            }

            for (LivingEntity affectedEntity : affectedEntities) {
                var effectInstance = effect.getInstance();
                EntityExtension.INSTANCE.applyPeriodicEffectIfTicksPassed(affectedEntity, effectInstance, 80, entity);
            }
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    void writeNBT(ValueOutput output, CallbackInfo ci) {
        if (!allyAuraStatusEffects.isEmpty()) {
            output.store(ALLY_AURA_STATUS_EFFECTS_NBT, AuraStatusEffect.Companion.getCODEC().listOf(), List.copyOf(allyAuraStatusEffects.values()));
        }
        if (!enemyAuraStatusEffects.isEmpty()) {
            output.store(ENEMY_AURA_STATUS_EFFECTS_NBT, AuraStatusEffect.Companion.getCODEC().listOf(), List.copyOf(enemyAuraStatusEffects.values()));
        }
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    void readNBT(ValueInput input, CallbackInfo ci) {
        List<AuraStatusEffect> allyEffects = input.read(ALLY_AURA_STATUS_EFFECTS_NBT, AuraStatusEffect.Companion.getCODEC().listOf()).orElse(List.of());
        allyAuraStatusEffects.clear();
        for (AuraStatusEffect auraStatusEffect : allyEffects) {
            allyAuraStatusEffects.put(auraStatusEffect.getType(), auraStatusEffect);
        }

        List<AuraStatusEffect> enemyEffects = input.read(ENEMY_AURA_STATUS_EFFECTS_NBT, AuraStatusEffect.Companion.getCODEC().listOf()).orElse(List.of());
        enemyAuraStatusEffects.clear();
        for (AuraStatusEffect auraStatusEffect : enemyEffects) {
            enemyAuraStatusEffects.put(auraStatusEffect.getType(), auraStatusEffect);
        }
    }
}
