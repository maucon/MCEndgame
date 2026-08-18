package de.fuballer.mcendgame.client.mixin.model_submit;

import de.fuballer.mcendgame.client.accessor.ModelSubmitAccessor;
import de.fuballer.mcendgame.client.component.entity.custom.entities.beastweaver.BeastweaverGradientData;
import net.minecraft.client.renderer.SubmitNodeStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(SubmitNodeStorage.ModelSubmit.class)
public class ModelSubmitMixin implements ModelSubmitAccessor {
    @Unique
    private BeastweaverGradientData beastweaverGradientData = null;

    @Override
    public void mcendgame$setBeastweaverGradientData(BeastweaverGradientData gradientData) {
        beastweaverGradientData = gradientData;
    }

    @Override
    public BeastweaverGradientData mcendgame$getBeastweaverGradientData() {
        return beastweaverGradientData;
    }
}
