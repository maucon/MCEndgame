package de.fuballer.mcendgame.main.mixin.projectile;

import de.fuballer.mcendgame.main.accessor.PersistentProjectileEntityDamageAccessor;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AbstractArrow.class)
public class AbstractArrowDamageMixin implements PersistentProjectileEntityDamageAccessor {
    @Shadow
    private double baseDamage;

    @Override
    public double mcendgame$getDamage() {
        return this.baseDamage;
    }
}
