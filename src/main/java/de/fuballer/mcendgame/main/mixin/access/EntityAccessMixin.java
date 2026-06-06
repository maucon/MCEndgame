package de.fuballer.mcendgame.main.mixin.access;

import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Entity.class)
public interface EntityAccessMixin {
    @Invoker("markHurt")
    void invokeMarkHurt();
}
