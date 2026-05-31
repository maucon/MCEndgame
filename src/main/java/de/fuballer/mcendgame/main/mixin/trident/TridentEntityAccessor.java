package de.fuballer.mcendgame.main.mixin.trident;

import net.minecraft.entity.projectile.TridentEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TridentEntity.class)
public interface TridentEntityAccessor {
    @Accessor("dealtDamage")
    void mcendgame$setDealtDamage(boolean dealtDamage);
}
