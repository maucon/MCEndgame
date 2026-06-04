package de.fuballer.mcendgame.client.mixin.low_health_ticks;

import de.fuballer.mcendgame.client.accessor.LivingEntityLowHealthTicksAccessor;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityLowHealthTicksMixin implements LivingEntityLowHealthTicksAccessor {
    @Unique
    public int lowHealthTicks20 = 0; // capped at 20

    @Inject(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/ProfilerFiller;pop()V"))
    public void baseTick(
            CallbackInfo ci
    ) {
        LivingEntity entity = (LivingEntity) (Object) this;

        double percentHealth = entity.getHealth() / entity.getMaxHealth();
        boolean lowHealth = percentHealth < 0.5; //TODO settings or util class
        lowHealthTicks20 = Math.clamp(lowHealthTicks20 + (lowHealth ? 1 : -1), 0, 20);
    }

    @Override
    public int mcendgame$getLowHealthTicks20() {
        return lowHealthTicks20;
    }
}
