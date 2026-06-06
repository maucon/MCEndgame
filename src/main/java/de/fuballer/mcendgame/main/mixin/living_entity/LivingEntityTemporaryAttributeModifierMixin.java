package de.fuballer.mcendgame.main.mixin.living_entity;

import de.fuballer.mcendgame.main.accessor.LivingEntityTemporaryAttributeModifierAccessor;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import kotlin.Pair;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityTemporaryAttributeModifierMixin implements LivingEntityTemporaryAttributeModifierAccessor {
    @Unique
    private static final int checkInterval = 4;
    @Unique
    private final Object2IntOpenHashMap<Pair<Holder<Attribute>, Identifier>> toRemoveModifiers = new Object2IntOpenHashMap<>();

    @Inject(method = "tick", at = @At("HEAD"))
    void tick(CallbackInfo ci) {
        var entity = (LivingEntity) (Object) this;
        if (entity.level().isClientSide()) return;

        if (entity.tickCount % checkInterval != 0) return;

        var iterator = toRemoveModifiers.object2IntEntrySet().iterator();
        while (iterator.hasNext()) {
            var entry = iterator.next();

            var updatedRemainingTicks = entry.getIntValue() - checkInterval;
            if (updatedRemainingTicks > 0) {
                entry.setValue(updatedRemainingTicks);
                continue;
            }

            var modifier = entry.getKey();
            iterator.remove();

            var attributeInstance = entity.getAttribute(modifier.getFirst());
            if (attributeInstance == null) continue;
            attributeInstance.removeModifier(modifier.getSecond());
        }
    }

    @Override
    public void mcendgame$addTemporaryAttributeModifier(
            Holder<Attribute> type,
            Identifier identifier,
            int ticks,
            double value,
            AttributeModifier.Operation operation
    ) {
        var entity = (LivingEntity) (Object) this;
        var attributeInstance = entity.getAttribute(type);
        if (attributeInstance == null) return;

        var key = new Pair<>(type, identifier);
        if (toRemoveModifiers.getOrDefault(key, -1) > ticks) return;
        toRemoveModifiers.put(key, ticks);

        var existingModifier = attributeInstance.getModifier(identifier);
        if (existingModifier != null && Math.abs(existingModifier.amount() - value) < 0.001) return;

        var modifier = new AttributeModifier(identifier, value, operation);

        attributeInstance.removeModifier(identifier);
        attributeInstance.addTransientModifier(modifier);
    }
}