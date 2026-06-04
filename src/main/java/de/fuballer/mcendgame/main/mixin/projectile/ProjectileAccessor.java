package de.fuballer.mcendgame.main.mixin.projectile;

import net.minecraft.world.entity.projectile.Projectile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Projectile.class)
public interface ProjectileAccessor {
    @Accessor("leftOwner")
    void mcendgame$setLeftOwner(boolean leftOwner);
}
