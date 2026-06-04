package de.fuballer.mcendgame.main.mixin.living_entity;

import de.fuballer.mcendgame.main.accessor.LivingEntityWorldAttributesAccessor;
import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleRoll;
import de.fuballer.mcendgame.main.component.custom_attribute.data.VanillaAttributeType;
import de.fuballer.mcendgame.main.component.world.VanillaTypeWorldAttributeInstance;
import de.fuballer.mcendgame.main.component.world.WorldAttributeAction;
import de.fuballer.mcendgame.main.util.extension.mixin.WorldMixinExtension;
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityWorldAttributesMixin implements LivingEntityWorldAttributesAccessor {
    @Unique
    private int appliedWorldAttributesUpdate = 0;

    @Inject(
            method = "tick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;detectEquipmentUpdates()V")
    )
    void updateWorldAttributes(CallbackInfo ci) {
        var entity = (LivingEntity) (Object) this;
        if (!(entity.level() instanceof ServerLevel world)) return;

        var latestUpdate = WorldMixinExtension.INSTANCE.getAttributeUpdateCount(world);
        if (latestUpdate <= appliedWorldAttributesUpdate) return;

        var history = WorldMixinExtension.INSTANCE.getVanillaTypeAttributesHistory(world, entity);
        for (VanillaTypeWorldAttributeInstance updateInstance : history) {
            if (updateInstance.getUpdate() <= appliedWorldAttributesUpdate) continue;

            var updateAttribute = updateInstance.getAttribute();
            var type = (VanillaAttributeType) updateAttribute.getType();
            var attributeInstance = entity.getAttribute(type.getAttribute());
            if (attributeInstance == null) continue;

            var identifier = IdentifierUtil.INSTANCE.defaultCustomAttribute(updateAttribute);

            if (updateInstance.getAction() == WorldAttributeAction.ADD) {
                if (attributeInstance.hasModifier(identifier)) continue;
                var modifier = new AttributeModifier(
                        identifier,
                        ((DoubleRoll) updateAttribute.getRolls().getFirst()).getValue(),
                        type.getScaleType()
                );
                attributeInstance.addTransientModifier(modifier);
            } else {
                attributeInstance.removeModifier(identifier);
            }
        }

        appliedWorldAttributesUpdate = latestUpdate;
    }

    @Override
    public void mcendgame$resetWorldAttributesUpdate() {
        appliedWorldAttributesUpdate = 0;
    }
}
