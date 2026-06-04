package de.fuballer.mcendgame.main.mixin.living_entity;

import de.fuballer.mcendgame.main.accessor.LivingEntityDodgedRecentlyAccessor;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LivingEntity.class)
public class LivingEntityDodgedRecentlyMixin implements LivingEntityDodgedRecentlyAccessor {
    @Unique
    private long lastDodgeAge = -1;

    @Override
    public void mcendgame$updateDodge() {
        var entity = (LivingEntity) (Object) this;
        lastDodgeAge = entity.tickCount;
    }

    @Override
    public boolean mcendgame$hasDodged(int ticks) {
        var entity = (LivingEntity) (Object) this;
        return (entity.tickCount - lastDodgeAge) <= ticks;
    }
}