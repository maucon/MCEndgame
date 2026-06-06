package de.fuballer.mcendgame.main.mixin.phasing;

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions;
import de.fuballer.mcendgame.main.util.extension.EntityExtension;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(Entity.class)
public class EntityBlockPhasingMixin {
    @Shadow
    protected Vec3 stuckSpeedMultiplier;
    @Unique
    private final Vec3 blockPhasingMovementMultiplier = new Vec3(0.5, 1.0, 0.5);

    @ModifyVariable(
            method = "move",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/entity/Entity;stuckSpeedMultiplier:Lnet/minecraft/world/phys/Vec3;",
                    ordinal = 0,
                    opcode = Opcodes.GETFIELD
            ),
            ordinal = 0,
            argsOnly = true
    )
    Vec3 slowMovementWhenBlockPhasing(Vec3 movement) {
        var entity = (Entity) (Object) this;
        if (!(entity instanceof LivingEntity livingEntity)) return movement;
        if (!CustomAttributesExtensions.INSTANCE.hasBlockPhasing(livingEntity)) return movement;
        if (!EntityExtension.INSTANCE.isBlockPhasing(livingEntity)) return movement;
        return movement.multiply(blockPhasingMovementMultiplier);
    }

    @Inject(
            method = "move",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/entity/Entity;stuckSpeedMultiplier:Lnet/minecraft/world/phys/Vec3;",
                    ordinal = 0,
                    opcode = Opcodes.GETFIELD
            )
    )
    void ignoreMovementMultiplierWhenBlockPhasing(MoverType type, Vec3 movement, CallbackInfo ci) {
        var entity = (Entity) (Object) this;
        if (!(entity instanceof LivingEntity livingEntity)) return;
        if (!CustomAttributesExtensions.INSTANCE.hasBlockPhasing(livingEntity)) return;
        stuckSpeedMultiplier = Vec3.ZERO;
    }
}
