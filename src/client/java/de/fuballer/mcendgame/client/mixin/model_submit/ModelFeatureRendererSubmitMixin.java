package de.fuballer.mcendgame.client.mixin.model_submit;

import de.fuballer.mcendgame.client.accessor.ModelFeatureRendererSubmitAccessor;
import de.fuballer.mcendgame.client.component.entity.custom.entities.beastweaver.BeastweaverGradientData;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ModelFeatureRenderer.Submit.class)
public class ModelFeatureRendererSubmitMixin implements ModelFeatureRendererSubmitAccessor {
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
