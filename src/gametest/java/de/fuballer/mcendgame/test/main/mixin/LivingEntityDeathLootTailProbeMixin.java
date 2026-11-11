package de.fuballer.mcendgame.test.main.mixin;

import de.fuballer.mcendgame.test.main.CompatibilityProbe;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Probe that stands in for third-party mods which hook the tail of {@code LivingEntity#dropAllDeathLoot}
 * (e.g. Pufferfish's Skills, which awards kill skill XP there). If another mod cancels the method at
 * HEAD, this injector never runs.
 */
@Mixin(LivingEntity.class)
public class LivingEntityDeathLootTailProbeMixin {
    @Inject(method = "dropAllDeathLoot", at = @At("TAIL"))
    private void mcendgame$probeDropAllDeathLootTail(ServerLevel level, DamageSource source, CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (CompatibilityProbe.target == self) {
            CompatibilityProbe.dropAllDeathLootTail.incrementAndGet();
        }
    }
}
