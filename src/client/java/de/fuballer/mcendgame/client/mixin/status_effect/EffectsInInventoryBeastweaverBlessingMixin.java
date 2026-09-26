package de.fuballer.mcendgame.client.mixin.status_effect;

import com.llamalad7.mixinextras.sugar.Local;
import de.fuballer.mcendgame.main.component.status_effect.beastweaver_blessings.BeastweaverBlessingEffect;
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.EffectsInInventory;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EffectsInInventory.class)
public abstract class EffectsInInventoryBeastweaverBlessingMixin {
    @Unique
    private static final Identifier EFFECT_BACKGROUND_BEASTWEAVER_BLESSING_SPRITE = IdentifierUtil.INSTANCE.defaultJava("container/inventory/effect_background_beastweaver_blessing");

    @Shadow
    protected abstract int extractBackground(GuiGraphicsExtractor graphics, Font font, Component effectName, Component duration, int x0, int y0, boolean isAmbient, int maxTextureWidth);

    @Redirect(
            method = "extractEffects",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screens/inventory/EffectsInInventory;extractBackground(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/Component;IIZI)I"
            )
    )
    int mcendgame$extractBeastweaverBlessingEffects(
            EffectsInInventory instance,
            GuiGraphicsExtractor graphics,
            Font font,
            Component effectName,
            Component duration,
            int x0,
            int y0,
            boolean isAmbient,
            int maxTextureWidth,
            @Local(name = "effect") MobEffectInstance effect
    ) {
        if (!(effect.getEffect().value() instanceof BeastweaverBlessingEffect))
            return extractBackground(graphics, font, effectName, duration, x0, y0, isAmbient, maxTextureWidth);

        return mcendgame$extractBeastweaverBlessingBackground(graphics, font, effectName, duration, x0, y0, maxTextureWidth);
    }

    @Unique
    private int mcendgame$extractBeastweaverBlessingBackground(
            final GuiGraphicsExtractor graphics,
            final Font font,
            final Component effectName,
            final Component duration,
            final int x0,
            final int y0,
            final int maxTextureWidth
    ) {
        int nameWidth = 32 + font.width(effectName) + 7;
        int durationWidth = 32 + font.width(duration) + 7;
        int textureWidth = Math.min(maxTextureWidth, Math.max(nameWidth, durationWidth));
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, EFFECT_BACKGROUND_BEASTWEAVER_BLESSING_SPRITE, x0, y0, textureWidth, 32);
        return textureWidth;
    }
}
