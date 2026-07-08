package de.fuballer.mcendgame.main.mixin.living_entity;

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions;
import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttributeType;
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes;
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityAttributeWhilePoisonedMixin {
    @Unique
    private static final Identifier attributeModifierIdentifier = IdentifierUtil.INSTANCE.defaultJava("increased_while_poisoned");

    @Inject(method = "tick", at = @At("HEAD"))
    void tick(CallbackInfo ci) {
        var entity = (LivingEntity) (Object) this;
        if (entity.getEntityWorld().isClient()) return;

        if (entity.age % 10 != 0) return;

        var isPoisoned = entity.hasStatusEffect(StatusEffects.POISON);
        tickAttributeWhilePoisoned(isPoisoned, EntityAttributes.GENERIC_MOVEMENT_SPEED, CustomAttributeTypes.INSTANCE.getINCREASED_MOVEMENT_SPEED_WHILE_POISONED(), entity);
        tickAttributeWhilePoisoned(isPoisoned, EntityAttributes.GENERIC_ATTACK_DAMAGE, CustomAttributeTypes.INSTANCE.getINCREASED_ATTACK_DAMAGE_WHILE_POISONED(), entity);
    }

    @Unique
    private void tickAttributeWhilePoisoned(
            boolean isPoisoned,
            RegistryEntry<EntityAttribute> vanillaAttribute,
            CustomAttributeType customAttribute,
            LivingEntity entity
    ) {
        var attributeInstance = entity.getAttributeInstance(vanillaAttribute);
        if (attributeInstance == null) return;

        if (!isPoisoned) {
            attributeInstance.removeModifier(attributeModifierIdentifier);
            return;
        }

        var allAttributes = CustomAttributesExtensions.INSTANCE.getAllCustomAttributes(entity);
        var attributes = allAttributes.get(customAttribute);
        if (attributes == null || attributes.isEmpty()) {
            attributeInstance.removeModifier(attributeModifierIdentifier);
            return;
        }

        var sum = attributes.stream()
                .mapToDouble(it -> CustomAttributesExtensions.INSTANCE.asDoubleRoll(it.getRolls().getFirst()).getValue())
                .sum();

        var existingModifier = attributeInstance.getModifier(attributeModifierIdentifier);
        if (existingModifier != null && Math.abs(existingModifier.value() - sum) < 0.001) return;

        var modifier = new EntityAttributeModifier(
                attributeModifierIdentifier,
                sum,
                EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
        );

        attributeInstance.removeModifier(attributeModifierIdentifier);
        attributeInstance.addTemporaryModifier(modifier);
    }
}
