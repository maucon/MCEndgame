package de.fuballer.mcendgame.main.mixin.projectile;

import net.minecraft.entity.projectile.ProjectileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ProjectileEntity.class)
public interface ProjectileEntityAccessor {
    @Accessor("leftOwner")
    void mcendgame$setLeftOwner(boolean leftOwner);
}
