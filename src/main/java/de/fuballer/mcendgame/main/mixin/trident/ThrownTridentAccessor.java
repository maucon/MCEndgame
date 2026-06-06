package de.fuballer.mcendgame.main.mixin.trident;

import net.minecraft.world.entity.projectile.arrow.ThrownTrident;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ThrownTrident.class)
public interface ThrownTridentAccessor {
    @Accessor("dealtDamage")
    void mcendgame$setDealtDamage(boolean dealtDamage);
}
