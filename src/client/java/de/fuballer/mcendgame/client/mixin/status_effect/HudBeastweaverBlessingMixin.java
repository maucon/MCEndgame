package de.fuballer.mcendgame.client.mixin.status_effect;

import com.llamalad7.mixinextras.sugar.Local;
import de.fuballer.mcendgame.main.component.status_effect.beastweaver_blessings.BeastweaverBlessingEffect;
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil;
import net.minecraft.client.gui.Hud;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Hud.class)
public class HudBeastweaverBlessingMixin {
    @Unique
    private static final Identifier EFFECT_BACKGROUND_BEASTWEAVER_BLESSING_SPRITE = IdentifierUtil.INSTANCE.defaultJava("hud/effect_background_beastweaver_blessing");

    @ModifyArg(
            method = "extractEffects",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIII)V"
            )
    )
    Identifier mcendgame$blitBeastweaverBlessingSprite(
            Identifier location,
            @Local(name = "effect") Holder<MobEffect> effect
    ) {
        if (!(effect.value() instanceof BeastweaverBlessingEffect)) return location;
        return EFFECT_BACKGROUND_BEASTWEAVER_BLESSING_SPRITE;
    }
}
