package de.fuballer.mcendgame.client.mixin.living_entity.isolated;

import de.fuballer.mcendgame.client.accessor.LivingEntityRenderStateIsolatedAccessor;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LivingEntityRenderState.class)
public class LivingEntityRenderStateIsolatedMixin implements LivingEntityRenderStateIsolatedAccessor {
    @Unique
    public boolean isIsolated;

    @Override
    public void mcendgame$setIsolated(boolean isolated) {
        isIsolated = isolated;
    }

    @Override
    public boolean mcendgame$isIsolated() {
        return isIsolated;
    }
}

