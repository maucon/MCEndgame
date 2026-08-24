package de.fuballer.mcendgame.main.mixin.attribute;

import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RangedAttribute.class)
public class RangedAttributeMixin {
    @Mutable
    @Shadow
    @Final
    private double maxValue;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void setAttributeMax(
            String descriptionId,
            double defaultValue,
            double minValue,
            double maxValue,
            CallbackInfo ci
    ) {
        switch (descriptionId) {
            case "attribute.name.armor",
                 "attribute.name.armor_toughness":
                this.maxValue = 2048.0;
                break;
            case "attribute.name.max_health":
                this.maxValue = Double.MAX_VALUE;
                break;
        }
    }
}
