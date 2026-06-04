package de.fuballer.mcendgame.client.mixin.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import de.fuballer.mcendgame.client.accessor.BipedEntityRenderStateAccessor;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidArmorLayer.class)
public abstract class HumanoidArmorLayerMixin<S extends HumanoidRenderState, M extends HumanoidModel<S>, A extends HumanoidModel<S>> {
    @Inject(
            method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/HumanoidRenderState;FF)V",
            at = @At("HEAD"),
            cancellable = true
    )
    public void render(PoseStack matrixStack, SubmitNodeCollector queue, int i, S renderState, float f, float g, CallbackInfo ci) {
        var stateAccessor = (BipedEntityRenderStateAccessor) renderState;

        if (!stateAccessor.mcendgame$getHiddenArmor().contains(EquipmentSlot.CHEST)) {
            invokeRenderArmorPiece(
                    matrixStack,
                    queue,
                    renderState.chestEquipment,
                    EquipmentSlot.CHEST,
                    renderState.lightCoords,
                    renderState
            );
        }

        if (!stateAccessor.mcendgame$getHiddenArmor().contains(EquipmentSlot.LEGS)) {
            invokeRenderArmorPiece(
                    matrixStack,
                    queue,
                    renderState.legsEquipment,
                    EquipmentSlot.LEGS,
                    renderState.lightCoords,
                    renderState
            );
        }

        if (!stateAccessor.mcendgame$getHiddenArmor().contains(EquipmentSlot.FEET)) {
            invokeRenderArmorPiece(
                    matrixStack,
                    queue,
                    renderState.feetEquipment,
                    EquipmentSlot.FEET,
                    renderState.lightCoords,
                    renderState
            );
        }

        if (!stateAccessor.mcendgame$getHiddenArmor().contains(EquipmentSlot.HEAD)) {
            invokeRenderArmorPiece(
                    matrixStack,
                    queue,
                    renderState.headEquipment,
                    EquipmentSlot.HEAD,
                    renderState.lightCoords,
                    renderState
            );
        }

        ci.cancel();
    }

    @Invoker
    public abstract void invokeRenderArmorPiece(
            PoseStack matrices,
            SubmitNodeCollector queue,
            ItemStack stack,
            EquipmentSlot slot,
            int light,
            S state
    );
}