package de.fuballer.mcendgame.main.mixin.living_entity;

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions;
import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttributeType;
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes;
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityAttributeWhileWitheredMixin {
    @Unique
    private static final Identifier increaseAttributeModifierIdentifier = IdentifierUtil.INSTANCE.defaultJava("increased_while_withered");
    @Unique
    private static final Identifier flatAttributeModifierIdentifier = IdentifierUtil.INSTANCE.defaultJava("flat_while_withered");

    @Inject(method = "tick", at = @At("HEAD"))
    void tick(CallbackInfo ci) {
        var entity = (LivingEntity) (Object) this;
        if (entity.level().isClientSide()) return;

        if (entity.tickCount % 10 != 0) return;

        var isWithered = entity.hasEffect(MobEffects.WITHER);
        tickAttributeWhileWithered(
                isWithered,
                Attributes.ATTACK_DAMAGE,
                CustomAttributeTypes.INSTANCE.getINCREASED_ATTACK_DAMAGE_WHILE_WITHERED(),
                entity,
                increaseAttributeModifierIdentifier,
                AttributeModifier.Operation.ADD_MULTIPLIED_BASE
        );
        tickAttributeWhileWithered(
                isWithered,
                Attributes.ARMOR,
                CustomAttributeTypes.INSTANCE.getARMOR_WHILE_WITHERED(),
                entity,
                flatAttributeModifierIdentifier,
                AttributeModifier.Operation.ADD_VALUE
        );
    }

    @Unique
    private void tickAttributeWhileWithered(
            boolean isWithered,
            Holder<Attribute> vanillaAttribute,
            CustomAttributeType customAttribute,
            LivingEntity entity,
            Identifier identifier,
            AttributeModifier.Operation operation
    ) {
        var attributeInstance = entity.getAttribute(vanillaAttribute);
        if (attributeInstance == null) return;

        if (!isWithered) {
            attributeInstance.removeModifier(identifier);
            return;
        }

        var allAttributes = CustomAttributesExtensions.INSTANCE.getAllCustomAttributes(entity);
        var attributes = allAttributes.get(customAttribute);
        if (attributes == null || attributes.isEmpty()) {
            attributeInstance.removeModifier(identifier);
            return;
        }

        var sum = attributes.stream()
                .mapToDouble(it -> CustomAttributesExtensions.INSTANCE.asDoubleRoll(it.getRolls().getFirst()).getValue())
                .sum();

        var existingModifier = attributeInstance.getModifier(identifier);
        if (existingModifier != null && Math.abs(existingModifier.amount() - sum) < 0.001) return;

        var modifier = new AttributeModifier(
                identifier,
                sum,
                operation
        );

        attributeInstance.removeModifier(identifier);
        attributeInstance.addTransientModifier(modifier);
    }
}
