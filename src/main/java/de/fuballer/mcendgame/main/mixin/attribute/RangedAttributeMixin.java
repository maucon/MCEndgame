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
    private void setArmorMax(
            String descriptionId,
            double defaultValue,
            double minValue,
            double maxValue,
            CallbackInfo ci
    ) {
        if (!descriptionId.equals("attribute.name.armor")) return;
        this.maxValue = 2048.0;
    }
}
