package de.fuballer.mcendgame.client.mixin.living_entity;

import de.fuballer.mcendgame.client.accessor.LivingEntityRenderStateWebbedAccessor;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LivingEntityRenderState.class)
public class LivingEntityRenderStateWebbedMixin implements LivingEntityRenderStateWebbedAccessor {
    @Unique
    public boolean isWebbed;

    @Override
    public void mcendgame$setWebbed(boolean webbed) {
        isWebbed = webbed;
    }

    @Override
    public boolean mcendgame$isWebbed() {
        return isWebbed;
    }
}
