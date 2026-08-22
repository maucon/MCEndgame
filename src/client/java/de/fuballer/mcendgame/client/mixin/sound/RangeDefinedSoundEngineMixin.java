package de.fuballer.mcendgame.client.mixin.sound;

import de.fuballer.mcendgame.client.component.sound.RangeDefinedSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(SoundEngine.class)
public class RangeDefinedSoundEngineMixin {
    @ModifyVariable(
            method = "play",
            at = @At(
                    value = "STORE"
            ),
            name = "attenuationDistance"
    )
    float modifyAttenuationDistance(
            float attenuationDistance,
            SoundInstance instance
    ) {
        if (!(instance instanceof RangeDefinedSoundInstance rangeDefinedInstance)) return attenuationDistance;
        return (float) rangeDefinedInstance.getRange();
    }
}
