package de.fuballer.mcendgame.main.mixin.living_entity;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LivingEntity.class)
public interface LivingEntityAccessor {
    @Invoker("getHitbox")
    AABB mcendgame$invokeGetHitbox();
}
