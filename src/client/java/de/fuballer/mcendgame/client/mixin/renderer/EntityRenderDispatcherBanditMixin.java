package de.fuballer.mcendgame.client.mixin.renderer;

import com.llamalad7.mixinextras.sugar.Local;
import de.fuballer.mcendgame.client.component.entity.custom.entities.bandit.BanditRenderState;
import de.fuballer.mcendgame.client.component.entity.custom.entities.bandit.BanditRenderer;
import de.fuballer.mcendgame.main.component.entity.custom.CustomEntities;
import de.fuballer.mcendgame.main.component.entity.custom.entities.bandit.BanditEntity;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityRenderLayerRegistrationCallback;
import net.fabricmc.fabric.impl.client.rendering.RegistrationHelperImpl;
import net.fabricmc.fabric.mixin.client.rendering.LivingEntityRendererAccessor;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.PlayerModelType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherBanditMixin {
    @Unique
    private Map<PlayerModelType, BanditRenderer> banditRenderers = Map.of();

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Inject(
            method = "getRenderer(Lnet/minecraft/world/entity/Entity;)Lnet/minecraft/client/renderer/entity/EntityRenderer;",
            at = @At("HEAD"),
            cancellable = true
    )
    <T extends Entity> void mcendgame$getBanditRenderer(
            T entity,
            CallbackInfoReturnable<EntityRenderer<? super T, ?>> cir
    ) {
        if (!(entity instanceof BanditEntity banditEntity)) return;

        var model = banditEntity.getBanditType().getModelType();
        var renderer = banditRenderers.get(model);
        cir.setReturnValue((EntityRenderer) renderer);
    }

    @SuppressWarnings({"unchecked"})
    @Inject(
            method = "getRenderer(Lnet/minecraft/client/renderer/entity/state/EntityRenderState;)Lnet/minecraft/client/renderer/entity/EntityRenderer;",
            at = @At("HEAD"),
            cancellable = true
    )
    <S extends EntityRenderState> void mcendgame$getBanditRenderer(
            S entityRenderState,
            CallbackInfoReturnable<EntityRenderer<?, ? super S>> cir
    ) {
        if (!(entityRenderState instanceof BanditRenderState banditRenderState)) return;

        var model = banditRenderState.getBanditType().getModelType();
        var renderer = banditRenderers.get(model);
        cir.setReturnValue((EntityRenderer<?, ? super S>) renderer);
    }

    @Inject(
            method = "onResourceManagerReload",
            at = @At("TAIL")
    )
    void mcendgame$onResourceManagerReload(
            ResourceManager resourceManager,
            CallbackInfo ci,
            @Local(name = "context") EntityRendererProvider.Context context
    ) {
        banditRenderers = mcendgame$createBanditRenderers(context);
    }

    @Unique
    private Map<PlayerModelType, BanditRenderer> mcendgame$createBanditRenderers(
            final EntityRendererProvider.Context context
    ) {
        try {
            return Map.of(
                    PlayerModelType.WIDE, mcendgame$createBanditRenderer(context, false),
                    PlayerModelType.SLIM, mcendgame$createBanditRenderer(context, true)
            );
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to create bandit models", e);
        }
    }

    @SuppressWarnings({"rawtypes", "UnstableApiUsage", "unchecked"})
    @Unique
    private BanditRenderer mcendgame$createBanditRenderer(
            final EntityRendererProvider.Context context,
            boolean slim
    ) {
        var renderer = new BanditRenderer(context, slim);
        var accessor = (LivingEntityRendererAccessor) (Object) renderer;
        LivingEntityRenderLayerRegistrationCallback.EVENT.invoker()
                .registerLayers(CustomEntities.INSTANCE.getBANDIT(), renderer, new RegistrationHelperImpl(accessor::callAddLayer), context);
        return renderer;
    }
}
