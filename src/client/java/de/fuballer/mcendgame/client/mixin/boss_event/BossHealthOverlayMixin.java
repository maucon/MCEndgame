package de.fuballer.mcendgame.client.mixin.boss_event;

import de.fuballer.mcendgame.client.component.boss_event.ClientBossEventTypes;
import de.fuballer.mcendgame.main.component.boss_event.BossEventType;
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.BossHealthOverlay;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.protocol.game.ClientboundBossEventPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import org.jspecify.annotations.NonNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(BossHealthOverlay.class)
public class BossHealthOverlayMixin {
    @Unique
    private static final Identifier BEASTWEAVER_BACKGROUND_SPRITE = IdentifierUtil.INSTANCE.defaultJava("boss_bar/beastweaver_background");
    @Unique
    private static final Identifier BEASTWEAVER_PROGRESS_SPRITE = IdentifierUtil.INSTANCE.defaultJava("boss_bar/beastweaver_progress");

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
        var id = event.getId();
        if (ClientBossEventTypes.INSTANCE.get(id) != BossEventType.BEASTWEAVER) return;

        graphics.blitSprite(RenderPipelines.GUI_TEXTURED,
                BEASTWEAVER_BACKGROUND_SPRITE,
                188,
                11,
                0,
                0,
                x - 3,
                y - 1,
                188,
                11
        );

        int width = Mth.lerpDiscrete(event.getProgress(), 0, 188);
        if (width > 0) graphics.blitSprite(
                RenderPipelines.GUI_TEXTURED,
                BEASTWEAVER_PROGRESS_SPRITE,
                188,
                11,
                0,
                0,
                x - 3,
                y - 1,
                width,
                11
        );

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
