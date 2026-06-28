package de.fuballer.mcendgame.client.mixin.renderer;

import de.fuballer.mcendgame.client.accessor.BipedEntityRenderStateAccessor;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ArmorFeatureRenderer.class)
public abstract class ArmorFeatureRendererMixin<S extends BipedEntityRenderState, M extends BipedEntityModel<S>, A extends BipedEntityModel<S>> {
    @Redirect(
            method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;ILnet/minecraft/client/render/entity/state/BipedEntityRenderState;FF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/entity/feature/ArmorFeatureRenderer;renderArmor(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/EquipmentSlot;ILnet/minecraft/client/render/entity/state/BipedEntityRenderState;)V"
            )
    )
    private void mcendgame$redirectRenderArmorPiece(
            ArmorFeatureRenderer instance,
            MatrixStack matrices,
            OrderedRenderCommandQueue queue,
            ItemStack stack,
            EquipmentSlot slot,
            int light,
            S state
    ) {
        var stateAccessor = (BipedEntityRenderStateAccessor) state;
        if (stateAccessor.mcendgame$getHiddenArmor().contains(slot)) return;

        invokeRenderArmor(matrices, queue, stack, slot, light, state);
    }

    @Invoker
    public abstract void invokeRenderArmor(
            MatrixStack matrices,
            OrderedRenderCommandQueue queue,
            ItemStack stack,
            EquipmentSlot slot,
            int light,
            S state
    );
}