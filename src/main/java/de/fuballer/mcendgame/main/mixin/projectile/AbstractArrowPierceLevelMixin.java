package de.fuballer.mcendgame.main.mixin.projectile;

import de.fuballer.mcendgame.main.accessor.PersistentProjectileEntityPierceLevelAccessor;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractArrow.class)
public abstract class AbstractArrowPierceLevelMixin implements PersistentProjectileEntityPierceLevelAccessor {
    @Invoker("setPierceLevel")
    public abstract void mcendgame$callSetPierceLevel(byte level);
}
