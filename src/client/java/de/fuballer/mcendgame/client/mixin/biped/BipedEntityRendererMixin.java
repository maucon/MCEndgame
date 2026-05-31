package de.fuballer.mcendgame.client.mixin.biped;

import de.fuballer.mcendgame.client.accessor.BipedEntityRenderStateAccessor;
import de.fuballer.mcendgame.client.accessor.LivingEntityCapeDataAccessor;
import de.fuballer.mcendgame.main.component.item.custom.armor.interfaces.HideBipedBoneArmor;
import de.fuballer.mcendgame.main.component.item.custom.armor.interfaces.HideOtherArmorArmor;
import de.fuballer.mcendgame.main.util.extension.EntityExtension;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.network.ClientPlayerLikeState;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.stream.Collectors;

@Mixin(BipedEntityRenderer.class)
public class BipedEntityRendererMixin {
    @Inject(method = "updateBipedRenderState", at = @At("TAIL"))
    private static void updateBipedRenderState(
            LivingEntity entity,
            BipedEntityRenderState state,
            float tickDelta,
            ItemModelManager itemModelResolver,
            CallbackInfo ci
    ) {
        var accessor = (BipedEntityRenderStateAccessor) state;

        var armorItems = List.of(
                entity.getEquippedStack(EquipmentSlot.HEAD).getItem(),
                entity.getEquippedStack(EquipmentSlot.CHEST).getItem(),
                entity.getEquippedStack(EquipmentSlot.LEGS).getItem(),
                entity.getEquippedStack(EquipmentSlot.FEET).getItem()
        );

        var hiddenBones = armorItems.stream()
                .filter(item -> item instanceof HideBipedBoneArmor)
                .flatMap(item -> ((HideBipedBoneArmor) item).getHiddenBones().stream())
                .collect(Collectors.toSet());
        accessor.mcendgame$setHiddenBones(hiddenBones);

        var hiddenArmor = armorItems.stream()
                .filter(item -> item instanceof HideOtherArmorArmor)
                .flatMap(item -> ((HideOtherArmorArmor) item).getHiddenArmor().stream())
                .collect(Collectors.toSet());
        accessor.mcendgame$setHiddenArmor(hiddenArmor);

        accessor.mcendgame$setVelocity(entity.getVelocity());

        if (EntityExtension.INSTANCE.needsCapeData(entity)) {
            updateCape(entity, state, tickDelta);
        }
    }

    @Unique
    private static void updateCape(LivingEntity entity, BipedEntityRenderState state, float tickProgress) {
        var capeDataAccessor = (LivingEntityCapeDataAccessor) entity;
        ClientPlayerLikeState capeState = capeDataAccessor.mcendgame$getCapeState();

        double d = capeState.lerpX(tickProgress) - MathHelper.lerp(tickProgress, entity.lastX, entity.getX());
        double e = capeState.lerpY(tickProgress) - MathHelper.lerp(tickProgress, entity.lastY, entity.getY());
        double f = capeState.lerpZ(tickProgress) - MathHelper.lerp(tickProgress, entity.lastZ, entity.getZ());

        float g = MathHelper.lerpAngleDegrees(tickProgress, entity.lastBodyYaw, entity.bodyYaw);
        double h = MathHelper.sin((g * ((float) Math.PI / 180F)));
        double i = -MathHelper.cos((g * ((float) Math.PI / 180F)));

        var renderStateAccessor = (BipedEntityRenderStateAccessor) state;

        var verticalLift = (float) e * 10.0F;
        verticalLift = MathHelper.clamp(verticalLift, -6.0F, 32.0F);
        float j = capeState.lerpMovement(tickProgress);
        float k = capeState.getLerpedDistanceMoved(tickProgress);
        verticalLift += MathHelper.sin((k * 6.0F)) * 32.0F * j;
        renderStateAccessor.mcendgame$setCapeVerticalLift(verticalLift);

        var forwardDrag = (float) (d * h + f * i) * 100.0F;
        if (state instanceof PlayerEntityRenderState playerState) forwardDrag *= 1.0F - playerState.getGlidingProgress();
        forwardDrag = MathHelper.clamp(forwardDrag, 0.0F, 150.0F);
        renderStateAccessor.mcendgame$setCapeForwardDrag(forwardDrag);

        var sidewaysSway = (float) (d * i - f * h) * 100.0F;
        sidewaysSway = MathHelper.clamp(sidewaysSway, -20.0F, 20.0F);
        renderStateAccessor.mcendgame$setCapeSidewaysSway(sidewaysSway);
    }
}
