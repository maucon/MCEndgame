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
    /**
     * 1.21/26.2 drove the stray conversion from {@code Skeleton.tick()} itself and kept the "is
     * converting" flag in synced entity data. 26.3 moved all of that into {@link ConversionTracker},
     * which {@code Skeleton.tick()} runs as its very first statement.
     * <p>
     * Only the tracker tick may be skipped here: cancelling the injection point would also skip the
     * {@code super.tick()} that follows it, leaving the skeleton without AI, movement or attacks.
     */
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
