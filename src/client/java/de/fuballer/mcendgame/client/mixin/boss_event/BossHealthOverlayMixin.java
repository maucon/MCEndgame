package de.fuballer.mcendgame.client.mixin.boss_event;

import de.fuballer.mcendgame.client.component.boss_event.ClientBossEventTypes;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.BossHealthOverlay;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.protocol.game.ClientboundBossEventPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import org.jspecify.annotations.NonNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(BossHealthOverlay.class)
public class BossHealthOverlayMixin {
    @Inject(
            method = "extractBar(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IILnet/minecraft/world/BossEvent;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    void extractBeastweaverBar(
            GuiGraphicsExtractor graphics,
            int x,
            int y,
            BossEvent event,
            CallbackInfo ci
    ) {
        var type = ClientBossEventTypes.INSTANCE.get(event.getId());
        if (type == null) return;

        var width = type.getSpriteWidth();
        var height = type.getSpriteHeight();
        var offsetX = x + type.getXOffset();
        var offsetY = y + type.getYOffset();

        graphics.blitSprite(RenderPipelines.GUI_TEXTURED,
                type.getBackgroundSprite(),
                width,
                height,
                0,
                0,
                offsetX,
                offsetY,
                width,
                height
        );

        int progressWidth = Mth.lerpDiscrete(event.getProgress(), 0, width);
        if (progressWidth > 0) graphics.blitSprite(
                RenderPipelines.GUI_TEXTURED,
                type.getProgressSprite(),
                width,
                height,
                0,
                0,
                offsetX,
                offsetY,
                progressWidth,
                height
        );

        var foreground = type.getForegroundSprite();
        if (foreground != null) {
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED,
                    foreground,
                    width,
                    height,
                    0,
                    0,
                    offsetX,
                    offsetY,
                    width,
                    height
            );
        }

        ci.cancel();
    }

    @Inject(method = "update", at = @At("HEAD"))
    void removeEventType(ClientboundBossEventPacket packet, CallbackInfo ci) {
        packet.dispatch(new ClientboundBossEventPacket.Handler() {
            @Override
            public void remove(final @NonNull UUID id) {
                ClientBossEventTypes.INSTANCE.remove(id);
            }
        });
    }
}
