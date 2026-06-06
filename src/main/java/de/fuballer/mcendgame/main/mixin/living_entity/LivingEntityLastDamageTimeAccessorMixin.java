package de.fuballer.mcendgame.main.mixin.living_entity;

import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LivingEntity.class)
public interface LivingEntityLastDamageTimeAccessorMixin {
    @Accessor("lastDamageStamp")
    long getLastDamageStamp();
}
