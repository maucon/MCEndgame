package de.fuballer.mcendgame.main.mixin.living_entity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LivingEntity.class)
public interface LivingEntityAccessor {
    @Invoker("getHitbox")
    Box mcendgame$invokeGetHitbox();
}
