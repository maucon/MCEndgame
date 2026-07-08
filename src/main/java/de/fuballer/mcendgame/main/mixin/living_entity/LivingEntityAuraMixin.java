package de.fuballer.mcendgame.main.mixin.living_entity;

import de.fuballer.mcendgame.main.accessor.LivingEntityAuraAccessor;
import de.fuballer.mcendgame.main.component.custom_attribute.effects.data.AuraStatusEffect;
import de.fuballer.mcendgame.main.util.extension.EntityExtension;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.TypeFilter;
import net.minecraft.world.World;
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
    private final HashMap<RegistryEntry<StatusEffect>, AuraStatusEffect> allyAuraStatusEffects = new HashMap<>();

    @Unique
    private final HashMap<RegistryEntry<StatusEffect>, AuraStatusEffect> enemyAuraStatusEffects = new HashMap<>();

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
        var world = entity.getEntityWorld();
        if (world.isClient()) return;
        if (entity.age % 10 != 0) return;

        applyAuraStatusEffects(entity, world, allyAuraStatusEffects, true);
        applyAuraStatusEffects(entity, world, enemyAuraStatusEffects, false);
    }

    @Unique
    void applyAuraStatusEffects(
            LivingEntity entity,
            World world,
            HashMap<RegistryEntry<StatusEffect>, AuraStatusEffect> effects,
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

                affectedEntities = (ArrayList<LivingEntity>) world.getEntitiesByType(
                        TypeFilter.instanceOf(LivingEntity.class),
                        entity.getBoundingBox().expand(range),
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

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    void writeNBT(NbtCompound nbt, CallbackInfo ci) {
        if (!allyAuraStatusEffects.isEmpty()) {
            nbt.put(
                    ALLY_AURA_STATUS_EFFECTS_NBT,
                    AuraStatusEffect.Companion.getLIST_CODEC()
                            .encodeStart(NbtOps.INSTANCE, List.copyOf(allyAuraStatusEffects.values()))
                            .getOrThrow()
            );
        }
        if (!enemyAuraStatusEffects.isEmpty()) {
            nbt.put(
                    ENEMY_AURA_STATUS_EFFECTS_NBT,
                    AuraStatusEffect.Companion.getLIST_CODEC()
                            .encodeStart(NbtOps.INSTANCE, List.copyOf(enemyAuraStatusEffects.values()))
                            .getOrThrow()
            );
        }
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    void readNBT(NbtCompound nbt, CallbackInfo ci) {
        List<AuraStatusEffect> allyEffects = AuraStatusEffect.Companion.getLIST_CODEC()
                .parse(NbtOps.INSTANCE, nbt.get(ALLY_AURA_STATUS_EFFECTS_NBT))
                .result()
                .orElse(List.of());
        allyAuraStatusEffects.clear();
        for (AuraStatusEffect auraStatusEffect : allyEffects) {
            allyAuraStatusEffects.put(auraStatusEffect.getType(), auraStatusEffect);
        }

        List<AuraStatusEffect> enemyEffects = AuraStatusEffect.Companion.getLIST_CODEC()
                .parse(NbtOps.INSTANCE, nbt.get(ENEMY_AURA_STATUS_EFFECTS_NBT))
                .result()
                .orElse(List.of());
        enemyAuraStatusEffects.clear();
        for (AuraStatusEffect auraStatusEffect : enemyEffects) {
            enemyAuraStatusEffects.put(auraStatusEffect.getType(), auraStatusEffect);
        }
    }
}
