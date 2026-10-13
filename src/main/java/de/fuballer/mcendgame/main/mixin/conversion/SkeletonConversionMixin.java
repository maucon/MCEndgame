package de.fuballer.mcendgame.main.mixin.conversion;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import de.fuballer.mcendgame.main.messaging.misc.EntityConversionCommand;
import de.maucon.mauconframework.command.CommandGateway;
import net.minecraft.world.entity.ConversionTracker;
import net.minecraft.world.entity.monster.skeleton.Skeleton;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Skeleton.class)
public class SkeletonConversionMixin {
    @WrapOperation(
            method = "tick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ConversionTracker;tick()V")
    )
    private void mcendgame$skipDisallowedConversion(ConversionTracker<?> tracker, Operation<Void> original) {
        var entity = (Skeleton) (Object) this;
        var entityConversionCommand = EntityConversionCommand.Companion.of(entity);
        var cmd = CommandGateway.INSTANCE.apply(entityConversionCommand);

        if (!cmd.getCanConvert()) return;

        original.call(tracker);
    }
}
